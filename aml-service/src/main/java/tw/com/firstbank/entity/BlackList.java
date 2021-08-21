package tw.com.firstbank.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "black_list")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "name", "screenType"})
public class BlackList implements Serializable  {
  
  private static final long serialVersionUID = 7317175579586797827L;

  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "name", nullable=true)
  private String name;
  
  @Column(name = "screen_type", nullable=true)
  private Integer screenType;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    BlackList other = (BlackList) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
    
}
