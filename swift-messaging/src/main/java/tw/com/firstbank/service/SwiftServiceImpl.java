package tw.com.firstbank.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.adapter.gateway.InwardRmtGateway;
import tw.com.firstbank.entity.BankInfo;
import tw.com.firstbank.entity.SwiftMessageLog;
import tw.com.firstbank.model.InwardRmtDto;
import tw.com.firstbank.model.SwiftMessage;
import tw.com.firstbank.model.SwiftTask;
import tw.com.firstbank.model.SwiftTextTag;
import tw.com.firstbank.repository.BankInfoRepository;

import tw.com.firstbank.util.DateTimeUtil;

@Slf4j
@Service
public class SwiftServiceImpl implements SwiftService {
  
  @Autowired
  private BankInfoRepository bankInfoRepo;
  
  @Autowired
  private RepositoryHelper repoHelper;
    
  @Autowired
  private InwardRmtGateway inwardRmtGateway;  

  @Value("${process-type:0}")
  private Integer processType;

  
  @Override
  public Integer uploadSwiftFiles(List<MultipartFile> files) throws IOException {
    Integer ret = 0;
    for(MultipartFile f : files) {
      
      if (f.isEmpty()) {
        continue; // 繼續下一個檔案
      }
          
      String fileName = f.getOriginalFilename();
      log.debug("Attach File Name {}", fileName);
    
      // 若未輸入檔名則以上傳檔名為主
      String baseFileName = FilenameUtils.getBaseName(fileName);    
      String ext = FilenameUtils.getExtension(fileName);
      
      if (StringUtils.isEmpty(ext) == false) {
        ext = ext.toLowerCase();
      }
      log.debug("Attach {} {}", baseFileName, ext);
      
      String fileContent = new String(f.getBytes());
      SwiftParser p = new SwiftParser();
      try {
        
        SwiftTask task = p.parse(fileContent);
        
        log.debug("count = {}", task.getMessages().size());
        
        for(SwiftMessage msg : task.getMessages()) {
          String d = msg.getBasicHeader().toString();
          log.debug(d);
          
          d = msg.getApHeader().toString();
          log.debug(d);
          
          log.debug("text cnt {}", msg.getTextBlock().count());
          
          for(SwiftTextTag tag : msg.getTextBlock().getTags()) {
            log.debug("Name={} Value={}", tag.getName(), tag.getValue());
          }
        }
        
        repoHelper.saveInactiveSwiftMessageLog(fileContent);

        ret++;
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
      }

    }
    
    return ret;
  }
  
  @Override
  public Integer send(Integer records) {
    List<SwiftMessageLog> logs = repoHelper.findInactiveMsgs(records);
    
    log.debug("records = {}", logs.size());
    
    for(SwiftMessageLog msg : logs) {
      log.debug(msg.getMsg());

      //TODO: 5 倍量的擴展方式: service? thread?
      // 共用檔案均是 IO 瓶頸
      // 但做 event sourcing 會複雜化      
       
      SwiftTask task = this.parseSwiftMessage(msg.getMsg());
      
      List<InwardRmtDto> rmts = from103(msg.getId(), task);   
      for(InwardRmtDto rmt : rmts) {
        processInwardRmt(msg, rmt);
      }
      
    }
        
    return logs.size();
  }
  
  private void processInwardRmt(SwiftMessageLog msg, InwardRmtDto rmt) {
    try {
      // check corr
      if (isValidReceiverCorr(rmt.getReceiverCorr()) == false) {               
        repoHelper.parsePending(msg);
        return;
      }
      
      log.debug("process type = {}", processType);
      
      if (processType == 0) {
        // async
        inwardRmtGateway.processInwardRmtByAsyncEvent(rmt);        
      } else if (processType == 1) {
        // sync
        InwardRmtDto ret = inwardRmtGateway.processInwardRmtByEvent(rmt);      
        if (ret.getReplyStatus() > 0) {
          repoHelper.parseComplete(msg);        
        }        
      } else {
        // api      
        InwardRmtDto ret = inwardRmtGateway.processInwardRmtByApi(rmt);
        if (ret != null && ret.getReplyStatus() > 0) {
          repoHelper.parseComplete(msg);        
        }        
      }
      
    } catch(Exception e) {
      log.error(e.getMessage(), e);
    }    
  }
    
  private Boolean isValidReceiverCorr(String bic) {
    Optional<BankInfo> opt = bankInfoRepo.findByBicAndIsCorr(bic, true);
    return opt.isPresent();
  }
  
  private List<InwardRmtDto> from103(String taskId, SwiftTask task) {
    List<InwardRmtDto> ret = new ArrayList<>();
    log.debug("msg cnt = {}", task.getMessages().size());
    
    for(SwiftMessage msg : task.getMessages()) {
      String msgType = msg.getApHeader().getMsgType();
      if (!"103".equals(msgType)) {
        continue;
      }
      
      InwardRmtDto rmt = new InwardRmtDto();
      
      log.debug("text cnt {}", msg.getTextBlock().count());
      for(SwiftTextTag tag : msg.getTextBlock().getTags()) {
        
        log.debug("Name={} Value={}", tag.getName(), tag.getValue());
        
        String tagName = tag.getName();
        String tagValue = tag.getValue();
        
        if (tagName.startsWith("20")) {
          rmt.setTxnRefNo(tagValue);
        } else if (tagName.startsWith("23B")) {
          rmt.setBankOpCode(tagValue);
        } else if (tagName.startsWith("32A")) {
          rmt.setValueDate(valueDateFrom32A(tagValue));
          rmt.setCcy(ccyFrom32A(tagValue));
          rmt.setInstAmt(amtFrom32A(tagValue));          
        } else if (tagName.startsWith("50")) {
          // A, F, K
          rmt.setOrderCust(tagValue);
        } else if (tagName.startsWith("59")) {
          // 59, 59A, 59F
          rmt.setBenefCust(tagValue);
          rmt.setBenefAcct(acctFromTag(tagValue));          
        } else if (tagName.startsWith("71A")) {
          rmt.setDetailCharge(tagValue);
        } else if (tagName.startsWith("53A")) { //-- optional
          rmt.setSenderCorr(tagValue);
        } else if (tagName.startsWith("54A")) {
          rmt.setReceiverCorr(tagValue);
        } else if (tagName.startsWith("71F")) {
          rmt.setSenderChargeCcy(chargeCcy(tagValue));
          rmt.setSenderCharge(chargeAmt(tagValue));          
        } else if (tagName.startsWith("71G")) {
          rmt.setReceiverChargeCcy(chargeCcy(tagValue));
          rmt.setReceiverCharge(chargeAmt(tagValue));          
        }        
        
      }  // for tags
      
      rmt.setId(taskId);
      
      ret.add(rmt);
      
    }  // for msg
    
    return ret;
  }
  
  private Integer valueDateFrom32A(String tag) {
    Integer ret = null;
    // append yyyy
    String value = DateTimeUtil.getYYYY().substring(0, 2) + tag.substring(0, 6);
    ret = Integer.parseInt(value);    
    return ret;
  }
  
  private String ccyFrom32A(String tag) {
    String ret = tag.substring(6, 6+3);    
    return ret;
  }
  
  private Float amtFrom32A(String tag) {
    Float ret = null;
    String value = tag.substring(9);
    ret = amtFromTag(value);
    return ret;
  }
  
  private String chargeCcy(String tag) {
    return tag.substring(0, 3);
  }
  
  private Float chargeAmt(String tag) {
    return amtFromTag(tag.substring(3));
  }
  
  private String acctFromTag(String tag) {
    String ret = "";
    if (tag.startsWith("/")) {
      ret = tag.split("\\R")[0].substring(1);
    }
    return ret;
  }
  
  private Float amtFromTag(String tag) {
    Float ret = null;
    String value = tag;    
    value = value.replaceAll(",", ".");
    ret = Float.valueOf(value);    
    return ret;
  }
    
  private SwiftTask parseSwiftMessage(String input){
    SwiftParser p = new SwiftParser();
    SwiftTask task = null;
    
    try {
      task = p.parse(input);
      
      log.debug("count = {}", task.getMessages().size());
      
      for(SwiftMessage msg : task.getMessages()) {
        String d = msg.getBasicHeader().toString();
        log.debug(d);
        
        d = msg.getApHeader().toString();
        log.debug(d);
        
        log.debug("text cnt {}", msg.getTextBlock().count());
        
        for(SwiftTextTag tag : msg.getTextBlock().getTags()) {
          log.debug("Name={} Value={}", tag.getName(), tag.getValue());
        }
      }
    } catch(Exception e) {
      log.error(e.getMessage(), e);
    }
    
    return task;
  }
    

}
