package tw.com.firstbank.entity;

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
@Table(name = "position")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "dbAmt", "crAmt"})
public class Position {

  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  // -
  @Column(name = "db_amt", nullable=true)
  private BigDecimal dbAmt = BigDecimal.ZERO;

  // +
  @Column(name = "cr_amt", nullable=true)
  private BigDecimal crAmt = BigDecimal.ZERO;
    
  @Column(name = "net_amt", nullable=true)
  private BigDecimal netAmt = BigDecimal.ZERO;
 
  public void setDbAmt(BigDecimal amt) {
    this.dbAmt = amt;
  }
  
  public void setDbAmt(Integer amt) {
    this.dbAmt = BigDecimal.valueOf(amt);
  }

  public void setDbAmt(Float amt) {
    this.dbAmt = BigDecimal.valueOf(amt);
  }
  
  public void setCrAmt(BigDecimal amt) {
    this.crAmt = amt;
  }
  
  public void setCrAmt(Integer amt) {
    this.crAmt = BigDecimal.valueOf(amt);
  }

  public void setCrAmt(Float amt) {
    this.crAmt = BigDecimal.valueOf(amt);
  }

  public void setNetAmt(BigDecimal amt) {
    this.netAmt = amt;
  }
  
  public void setNetAmt(Integer amt) {
    this.netAmt = BigDecimal.valueOf(amt);
  }

  public void setNetAmt(Float amt) {
    this.netAmt = BigDecimal.valueOf(amt);
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
    Position other = (Position) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  
}
