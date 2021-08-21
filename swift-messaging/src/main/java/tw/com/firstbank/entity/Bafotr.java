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
@Table(name = "bafotr")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "bic", "ccy", "acct", "dbAmt", "crAmt"})
public class Bafotr  implements Serializable {
  
  private static final long serialVersionUID = -6423095334629526550L;
  
  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "bic", nullable=true)
  private String bic;
  
  @Column(name = "ccy", nullable=true)
  private String ccy;
  
  @Column(name = "acct", nullable=true)
  private String acct;
  
  // -
  @Column(name = "db_amt", nullable=true)
  private BigDecimal dbAmt;
  
  // +
  @Column(name = "cr_amt", nullable=true)
  private BigDecimal crAmt;
  
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
    Bafotr other = (Bafotr) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
    
}
