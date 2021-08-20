package tw.com.firstbank.service;

import java.io.IOException;
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
import tw.com.firstbank.entity.SwiftMessageLog;
import tw.com.firstbank.model.SwiftMessage;
import tw.com.firstbank.model.SwiftTask;
import tw.com.firstbank.model.SwiftTextTag;
import tw.com.firstbank.repository.SwiftMessageRepository;

@Slf4j
@Service
public class SwiftServiceImpl implements SwiftService {
  
  @Autowired
  private SwiftMessageRepository repo;
  
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
    repo.save(msgLog);
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
      
      // save InwardRmt
      
      // update status to done if success
    }
        
    return logs.size();
  }
  
  private List<SwiftMessageLog> findInactiveMsgs(Integer records) {
    return repo.findInactive(records);
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
