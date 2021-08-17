package tw.com.firstbank.service;

import org.apache.commons.lang3.StringUtils;

import jline.internal.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.model.SwiftApHeader;
import tw.com.firstbank.model.SwiftBasicHeader;
import tw.com.firstbank.model.SwiftBlock;
import tw.com.firstbank.model.SwiftMessage;
import tw.com.firstbank.model.SwiftTask;
import tw.com.firstbank.model.SwiftTextBlock;
import tw.com.firstbank.model.SwiftTextTag;

/**
 * 用來解析 SWIFT 電文的主要類別
 * SwiftParser ->
 * SwiftTask -> SwiftMessage -> SwiftBlock -> SwiftBasicHeader , SwiftApHeader , SwiftUserBlock , SwiftTextBlock -> SwiftTextTag 
 * 1: basic header
 * 2: ap header
 * 3: user header
 * 4: text block
 * 5: trailer block
 * 
 */
@Getter
@Setter
@Slf4j
public class SwiftParser {
  private SwiftTask task;
  
  public SwiftTask parse(String content) throws ParseException {
    task = createTask(content);
    
    createBlock(task);
    
    return task;
  }
  
  
  public SwiftTask createTask(String content) throws ParseException {
    SwiftTask task = new SwiftTask();
    
    SwiftBlockParser blockParser = new SwiftBlockParser();
    SwiftMessage msg = null;
    Boolean isOk = false;
    Boolean isAdd = false;
    while(true) {
      String blkStr = blockParser.split(content);
      
      // 終止條件
      if (StringUtils.isEmpty(blkStr)) {
        if (isAdd == false && msg != null) {
          task.append(msg);
        }
        break;
      }
      log.debug("blkstr {}", blkStr);
      SwiftBlock block = new SwiftBlock();
      blockParser.parse(blkStr, 0, block);
      // 判斷是否為 basic header, 我們需利用 basic header 解資料
      // 目前只解 SWIFT_MESSAGE ，其它系統及控制訊息暫不解
      if (block.isBasicHeader()) {
        log.debug("block {}", block.getBlockContent());
        SwiftBasicHeader hdr = new SwiftBasicHeader(block.getBlockContent());
        log.debug("Service id {}", hdr.getServiceId());
        if (SwiftBasicHeader.SWIFT_MESSAGE == hdr.getServiceId()) {
          isOk = true;
          msg = new SwiftMessage();
        }
      }
      
      // 處理各個區塊，直到遇到最後一個區塊
      if (isOk) {
        msg.append(block);
        if (block.isTrailBlock()) {
          isOk = false;
          // 將組成的訊息加入TASK 物件中
          task.append(msg);
          isAdd = true;
        }
      }
    }
    return task;
  }
  
  /**
   * 將 SwiftTask 的內容再解析為各類 SwiftBlock
   * @param task
   * @return 產生的Block個數
   */
  public Integer createBlock(SwiftTask task) {
    Integer ret = 0;
    
    SwiftTagParser tagParser = new SwiftTagParser();
    for(SwiftMessage msg : task.getMessages()) {
      if (msg == null) {
        continue;
      }
      
      for(SwiftBlock block : msg.getBlocks()) {
        if (block == null) {
          continue;
        }
        
        if (block.isBasicHeader()) {
          SwiftBasicHeader hdr = new SwiftBasicHeader(block.getBlockContent());
          msg.setBasicHeader(hdr);
        }
        if (block.isApHeader()) {
          SwiftApHeader hdr = new SwiftApHeader(block.getBlockContent());
          msg.setApHeader(hdr);
        }
        if (block.isTextBlock()) {
          tagParser.resetMark();
          SwiftTextBlock textBlock = new SwiftTextBlock();
          while(true) {
            SwiftTextTag textTag = tagParser.parse(block.getBlockContent());
            if (textTag == null) {
              break;
            }
            textBlock.append(textTag);            
          }
          msg.setTextBlock(textBlock);
          ret = ret + 1;
        }
      }
    }
    
    return ret;
  }
}
