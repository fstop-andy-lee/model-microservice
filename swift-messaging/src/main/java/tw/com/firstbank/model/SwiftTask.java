package tw.com.firstbank.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 用來表示一個完整的SWIFT 電文解析工作 
 * 一個電文解析工作可能包含多封不同電文
 *
 */
@Setter
@Getter
public class SwiftTask {
  
  private String taskName;
  
  private List<SwiftMessage> messages;
  
  public SwiftTask() {
    messages = new ArrayList<>();
  }
  
  public SwiftMessage indexOf(Integer idx) {
    if (idx >= messages.size()) {
      return null;
    }
    return messages.get(idx);
  }
  
  public Integer count() {
    return messages.size();
  }
  
  public void append(SwiftMessage msg) {
    messages.add(msg);
  }

}
