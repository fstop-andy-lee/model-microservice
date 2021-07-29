package tw.com.firstbank.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "seq"})
public class JournalKey implements Serializable {

  private static final long serialVersionUID = 2735181615941572048L;

  private String id;
  
  private Integer seq;
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((seq == null) ? 0 : seq.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JournalKey other = (JournalKey) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (seq == null) {
      if (other.seq != null)
        return false;
    } else if (!seq.equals(other.seq))
      return false;
    return true;
  }

  public JournalKey(String id, Integer seq) {
    super();
    this.id = id;
    this.seq = seq;
  }
  
}
