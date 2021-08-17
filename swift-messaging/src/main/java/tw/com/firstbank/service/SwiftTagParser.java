package tw.com.firstbank.service;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import tw.com.firstbank.model.SwiftTextTag;

/**
 * 用來解析標籤的內容
 * 
 */
@Setter
@Getter
public class SwiftTagParser {
   
  public static String CRLF = "\r\n";
  
  private Integer mark = 0;
  
  /**
   * 重設解析位置
   */
  public void resetMark() {
    this.mark = 0;
  }
    
  public SwiftTextTag parse(String msg) {
    String tagStr = "";
    String content = "";
    SwiftTextTag textTag = null; 
    
    if (!msg.startsWith(CRLF)) {
      return textTag;
    }
    
    String currentData = msg.substring(this.mark);
    // 以換行符號為切割範圍
    String [] strArray = currentData.split(CRLF);
    for(String str : strArray) {
      if (StringUtils.isEmpty(str)) {
        continue;
      }
      
      // 當字串以 ":" 開頭，則表示為欄位起始，需解析 tag
      // 否則將字串當作內容 text 處理
      if (str.startsWith(":")) {
        
        // 終止條件
        if (!StringUtils.isEmpty(tagStr)) {
          break;
        }
        
        char [] chars = str.toCharArray();
        for(int i=1; i < chars.length -1; i++) {
          if (chars[i] != ':') {
            tagStr = tagStr + chars[i];            
          } else {
            // +1 不包含 tag 結尾的 ":"
            content = str.substring(i+1);
            break;
          }
        }        
      } else {
        content = content + CRLF + str;
      }
      
      this.mark = this.mark + str.length();      
    }
    
    if (StringUtils.isEmpty(tagStr)) {
      textTag = null;
    } else {
      textTag = new SwiftTextTag(tagStr, content);
    }
        
    return textTag;
  }
  
}
