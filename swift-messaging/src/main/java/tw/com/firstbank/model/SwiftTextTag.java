package tw.com.firstbank.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用來表示 Text Block 中的每個 tag
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"name", "value", "majorTag", "minorTag"})
public class SwiftTextTag {

  private String name = "";
  
  private String value = "";
  
  private String majorTag = "";
  
  private String minorTag = "";
  
  public SwiftTextTag(String name, String value) {
    this.name = name;
    this.value = value;
    if (this.name.length() > 2) {
      this.majorTag = this.name.substring(0, 2);
      this.minorTag = this.name.substring(2);
    } else {
      this.majorTag = this.name;
    }
  }
  
}
