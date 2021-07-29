package tw.com.firstbank.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "seq", "ts", "status"})
@MappedSuperclass
@IdClass(ServiceLogKey.class)
public class ServiceLog implements Serializable {

  private static final long serialVersionUID = 98583267379159615L;

  @Id
  private String id;
  
  @Id
  private Integer seq;
  
  private LocalDateTime ts;

  // 0 = normal, 1 = compensate
  private Integer status = 0;
  
  private String input;

  private String output;
  
  private String before;
  
  private String after;

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
    ServiceLog other = (ServiceLog) obj;
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
  
  
}
