package tw.com.firstbank.service;

import lombok.Getter;
import lombok.Setter;
import tw.com.firstbank.model.SwiftBlock;

@Getter
@Setter
public class SwiftBlockParser {
  
  Integer marker = 0;
  
  // 一個電文解析工作可能包含多封不同電文
  // z 字元集含有 "{" 字元
  private Boolean textBlockStart = false;
  
  public Integer parse(String input, Integer idx, SwiftBlock parentBlock) throws ParseException {
    Integer ret = 0;    
    try {
      ret = parseBlock(input, idx, parentBlock);
    } catch(Exception e) {
      throw new ParseException("Parse Swift Block Error");
    }    
    return ret;
  }
  
  public String split(String input) throws ParseException {
    String ret = "";
    try {
      ret = splitBlock(input);
    } catch(Exception e) {
      throw new ParseException("Split Swift Block Error");
    }
    return ret;
  }
  
  private String splitBlock(String input) {
    String ret = "";
    Integer start = 0;
    Integer ending = 0;
    Integer id = 0;
    Boolean isTagArea = false;
    
    StringBuilder sb = new StringBuilder();
    
    if (this.marker > input.length()) {
      return ret;
    }
    
    char[] chars = input.toCharArray();
    char ch;
    
    // 讀取資料直到結束
    while(this.marker < chars.length) {
      ch = chars[this.marker];
      this.marker += 1;
      // 記錄起始符號個數，應與結束符號相同
      if (ch == '{') {
        // z 字元集含有 "{" 字元，誤判， 若是在 text block 中遇到 "{" 不記錄為 block 起始符號
        if (isTagArea == false) {
          start += 1;
        }
                
      } else if (ch == '}') {
        ending += 1;        
        isTagArea = false;        
        // 錯誤，表示缺少起始符號
        if (start < ending) {
          ret = "";
          break;
        }
        if (start >= ending) {
          // id 符號至少需要和起始符號個數相同
          if (start <= id) {
            start -= 1;
            ending -= 1;
            id -= 1;
          }
          if (start > id) {
            ret = "";
            break;
          }
          // 若是配對的符號為 0 表示可以將結果輸出
          if (start == 0) {
            sb.append(ch);
            ret = sb.toString();
            break;
          }
        }
      } else if (ch == ':') {
        id += 1;
      } else if (ch == 0x13) {
        // 判斷是否進入 text block， 進入 text block 後 isTagArea = True
        if (isTagArea == false) {
          if (sb.toString().equals("{4:")) {
            isTagArea = true;
          }
        }
      }
      
      // 串接內容
      if (start > 0) {
        sb.append(ch);
      }      
    }  // while
    
    // 表示缺少結束符號
    if (start > 0) {
      ret = "";
    }
    
    return ret;
  }
  
  private Integer parseBlock(String input, Integer idx, SwiftBlock parentBlock) {
    Integer ret = 0;

    char[] chars = input.toCharArray();
    StringBuilder sb = new StringBuilder();
    
    Boolean isStart = false;
    Boolean getId = false;
    SwiftBlock block = null;
    String content = "";
    String id = "";
    
    for(ret = idx; ret < input.length(); ret++) {
      char c = chars[ret];
      if (c == '{') {
        if (textBlockStart) {
          sb.append(c);
          continue;
        }

        if (isStart == false) {
          if (idx != 0) {
            block = new SwiftBlock();
          }
          isStart = true;
          continue;
        } else {
          // 遞迴呼叫
          if (idx != 0) {
            ret = parseBlock(input, ret, block);
          } else {
            ret = parseBlock(input, ret, parentBlock);
          }
        }
        continue;
      }  
      
      if (c == '}') {
        content = sb.toString();
        sb = new StringBuilder();
        if (idx != 0) {
          // 去掉結尾的 "-"
          if (content.endsWith("-")) {
            content = content.substring(0, content.length() - 1);
          }
          block.setBlockContent(content);
          parentBlock.appendBlock(block);
        } else {
          // 去掉結尾的 "-"
          if (content.endsWith("-")) {
            content = content.substring(0, content.length() - 1);
          }
          parentBlock.setBlockContent(content);
        }
        this.textBlockStart = false;
        break;
      }
      
      if (c == ':' && getId == false) {
        id = sb.toString();
        sb = new StringBuilder();
        getId = true;
        if (idx != 0) {
          block.setBlockId(id);          
        } else {
          parentBlock.setBlockId(id);
        }
        
        if ("4".equals(id)) {
          this.textBlockStart = true;
        }
        continue;
      }
      
      sb.append(c);
    }
    
    return ret;
  }
}
