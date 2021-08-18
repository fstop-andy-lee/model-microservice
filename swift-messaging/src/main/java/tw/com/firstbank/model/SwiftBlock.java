package tw.com.firstbank.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {"blockId", "blockContent"})
public class SwiftBlock {
  
  public static Integer UNKNOW_BLOCK = 0;
  public static Integer BASIC_HEADER = 1;
  public static Integer AP_HEADER = 2;
  public static Integer USER_HEADER = 3;
  public static Integer TEXT_BLOCK= 4;
  public static Integer TRAILER_BLOCK = 5;
  
  private String blockId;
  
  private String blockContent;
  
  // 巢狀結構
  private List<SwiftBlock> childBlocks;
  
  public SwiftBlock() {
    this.childBlocks = new ArrayList<>();
  }

  public void appendBlock(SwiftBlock block) {
    this.childBlocks.add(block);
  }
  
  public Boolean isBasicHeader() {
    if ("1".equals(blockId)) {
      return true;
    }
    return false;
  }

  public Boolean isApHeader() {
    if ("2".equals(blockId)) {
      return true;
    }
    return false;
  }
  
  public Boolean isUserHeader() {
    if ("3".equals(blockId)) {
      return true;
    }
    return false;
  }

  public Boolean isTextBlock() {
    if ("4".equals(blockId)) {
      return true;
    }
    return false;
  }
  
  public Boolean isTrailBlock() {
    if ("5".equals(blockId)) {
      return true;
    }
    return false;
  }

}
