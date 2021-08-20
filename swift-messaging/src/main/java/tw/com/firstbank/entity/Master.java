package tw.com.firstbank.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "master")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "holdMark", "balance", "status", "cif"})
public class Master implements Serializable {

  private static final long serialVersionUID = -347185184625668388L;
  
  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "hold_mark", nullable=true)
  private String holdMark;
  
  @Column(name = "balance", nullable=true)
  private BigDecimal balance;
  
  @Column(name = "status", nullable=true)
  private Integer status;
  
  @Column(name = "cif", nullable=true)
  private String cif;

  public void setBalance(BigDecimal amt) {
    this.balance = amt;
  }
  
  public void setBalance(Integer amt) {
    this.balance = BigDecimal.valueOf(amt);
  }

  public void setBalance(Float amt) {
    this.balance = BigDecimal.valueOf(amt);
  }
  
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
    Master other = (Master) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
