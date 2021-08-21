package tw.com.firstbank.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.domain.type.SwiftMessageStatus;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.SwiftMessageLog;
import tw.com.firstbank.model.SwiftMessage;
import tw.com.firstbank.model.SwiftTask;
import tw.com.firstbank.model.SwiftTextTag;
import tw.com.firstbank.repository.BankInfoRepository;
import tw.com.firstbank.repository.InwardRmtRepository;
import tw.com.firstbank.repository.SwiftMessageRepository;
import tw.com.firstbank.util.DateTimeUtil;

@Slf4j
@Service
public class SwiftServiceImpl implements SwiftService {
  
  @Autowired
  private SwiftMessageRepository swiftMsgRepo;
  
  @Autowired
  private InwardRmtRepository inwardRmtRepo;
  
  @Autowired
  private BankInfoRepository bankInfoRepo;
  
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
        
        saveInactiveSwiftMessageLog(fileContent);

        ret++;
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
      }

    }
    
    return ret;
  }
  
  private String saveInactiveSwiftMessageLog(String content) {
    String id = generateId();
    SwiftMessageLog msgLog = new SwiftMessageLog();
    msgLog.setId(id);
    msgLog.setMsg(content);
    msgLog.setStatus(SwiftMessageStatus.INACTIVE);
    swiftMsgRepo.save(msgLog);
    return id;
  }
  
  private String generateId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public Integer send(Integer records) {
    //Page<SwiftMessageLog> page = repo.findAll(
    //    PageRequest.of(0, records, Sort.by(Sort.Direction.ASC, "id")));
    List<SwiftMessageLog> logs = findInactiveMsgs(records);
    
    log.debug("records = {}", logs.size());
    
    for(SwiftMessageLog msg : logs) {
      log.debug(msg.getMsg());
      
      
      SwiftTask task = this.parseSwiftMessage(msg.getMsg());
      // convert task to InwardRmt
      from103(msg.getId(), task);
      
      // save InwardRmt
      
      // update status to done if success
    }
        
    return logs.size();
  }
  
  private List<InwardRmt> from103(String taskId, SwiftTask task) {
    List<InwardRmt> ret = new ArrayList<>();
    log.debug("msg cnt = {}", task.getMessages().size());
    
    for(SwiftMessage msg : task.getMessages()) {
      String msgType = msg.getApHeader().getMsgType();
      if (!"103".equals(msgType)) {
        continue;
      }
      
      InwardRmt rmt = new InwardRmt();
      
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
          rmt.setAmt(amtFrom32A(tagValue));          
        } else if (tagName.startsWith("50")) {
          // A, F, K
          rmt.setOrderCust(tagValue);
        } else if (tagName.startsWith("59")) {
          // 59, 59A, 59F
          rmt.setBenefCust(tagValue);
        } else if (tagName.startsWith("71A")) {
          rmt.setDetailCharge(tagValue);
        }
        
        
      }  // for tags
      
      rmt.setId(taskId);
      
      inwardRmtRepo.save(rmt);
      
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
  
  private Float amtFromTag(String tag) {
    Float ret = null;
    String value = tag;    
    value = value.replaceAll(",", ".");
    ret = Float.valueOf(value);    
    return ret;
  }
  
  private List<SwiftMessageLog> findInactiveMsgs(Integer records) {
    return swiftMsgRepo.findInactive(records);
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
