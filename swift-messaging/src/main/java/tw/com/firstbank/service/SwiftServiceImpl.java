package tw.com.firstbank.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.model.SwiftMessage;
import tw.com.firstbank.model.SwiftTask;

@Slf4j
@Service
public class SwiftServiceImpl implements SwiftService {
  
  
  
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
        }
        
        
        ret++;
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
      }

    }
    
    return ret;
  }

}
