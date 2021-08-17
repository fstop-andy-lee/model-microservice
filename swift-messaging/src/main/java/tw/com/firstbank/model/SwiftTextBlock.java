package tw.com.firstbank.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SwiftTextBlock {

  private List<SwiftTextTag> tags = new ArrayList<>();
  
  public SwiftTextTag indexOf(Integer idx) {
    return tags.get(idx);
  }
  
  public Integer count() {
    return tags.size();
  }
  
  public SwiftTextTag find(String name) {
    for(SwiftTextTag tag : tags) {
      if (name.equalsIgnoreCase(tag.getName())) {
        return tag;
      }
    }
    return null;
  }
  
  public void append(SwiftTextTag tag) {
    this.tags.add(tag);
  }
}
