package tw.com.firstbank.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ToString(of = {"blockId", "blockContent"})
public class SwiftMessage {

  public static String MSG_WAIT_FOR_RELEASE = "E";  
  public static String MSG_WAIT_FOR_SEND = "G";
  public static String MSG_SENDED = "I";
  public static String MSG_WAIT_FOR_ACK = "K";
  public static String MSG_ACKED = "M";
  public static String MSG_NACKED = "O";
  public static String MSG_CANCELED = "Q";
  
  private List<SwiftBlock> blocks = new ArrayList<>();
  
  private String msgType = "";
  
  private SwiftBasicHeader basicHeader;
  
  private SwiftApHeader apHeader;

  private SwiftTextBlock textBlock;
  
  public SwiftBlock find(String blockId) {
    for(SwiftBlock b : blocks) {
      if (blockId.equalsIgnoreCase(b.getBlockId())) {
        return b;
      }
    }
    return null;
  }
  
  public SwiftBlock indexOf(Integer idx) {
    return blocks.get(idx);
  }
  
  public Integer count() {
    return blocks.size();
  }
  
  public void append(SwiftBlock block) {
    this.blocks.add(block);
  }
  
}
