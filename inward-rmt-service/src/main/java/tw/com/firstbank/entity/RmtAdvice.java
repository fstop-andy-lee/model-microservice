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
@Table(name = "rmt_advice")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "valueDate", "ccy", "instAmt", "benefCust"})
public class RmtAdvice implements Serializable {

  private static final long serialVersionUID = 3476344853203195213L;

  @Id
  @Column(name = "id", nullable=false)
  private String id;

  @Column(name = "value_date", nullable=true)
  private Integer valueDate;
  
  // 轉換金額
  @Column(name = "amt", nullable=true)
  private BigDecimal amt = BigDecimal.ZERO;

  @Column(name = "ccy", nullable=true)
  private String ccy;
  
  // 匯款金額
  @Column(name = "inst_amt", nullable=true)
  private BigDecimal instAmt = BigDecimal.ZERO;

  @Column(name = "benef_name", nullable=true)
  private String benefName;

  @Column(name = "benef_acct", nullable=true)
  private String benefAcct;

  @Column(name = "benef_cust", nullable=true)
  private String benefCust;
  
  @Column(name = "order_cust", nullable=true)
  private String orderCust;
    
  @Column(name = "order_bank", nullable=true)
  private String orderBank;
  
  @Column(name = "rmt_info", nullable=true)
  private String rmtInfo;
  
  @Column(name = "status", nullable=true)
  private Integer status = 0;
    
  public void setAmt(BigDecimal amt) {
    this.amt = amt;
  }
  
  public void setAmt(Integer amt) {
    this.amt = BigDecimal.valueOf(amt);
  }

  public void setAmt(Float amt) {
    this.amt = BigDecimal.valueOf(amt);
  }

  public void setInstAmt(BigDecimal amt) {
    this.instAmt = amt;
  }
  
  public void setInstAmt(Integer amt) {
    this.instAmt = BigDecimal.valueOf(amt);
  }

  public void setInstAmt(Float amt) {
    this.instAmt = BigDecimal.valueOf(amt);
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
    RmtAdvice other = (RmtAdvice) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }  

  
}
