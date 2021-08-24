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
@Table(name = "billrpt")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "unino", "benefAcct", "benefName", "instAmt"})
public class BillRpt implements Serializable {
  
  private static final long serialVersionUID = -2871991111328530872L;

  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "ccy", nullable=true)
  private String ccy;

  @Column(name = "inst_amt", nullable=true)
  private BigDecimal instAmt = BigDecimal.ZERO;
  
  @Column(name = "order_cust", nullable=true)
  private String orderCust;
  
  @Column(name = "benef_cust", nullable=true)
  private String benefCust;
  
  @Column(name = "benef_acct", nullable=true)
  private String benefAcct;
  
  @Column(name = "benef_name", nullable=true)
  private String benefName;
  
  @Column(name = "unino", nullable=true)
  private String unino;

  @Column(name = "addr", nullable=true)
  private String addr;

  @Column(name = "phone", nullable=true)
  private String phone;
  
  @Column(name = "rmt_type", nullable=true)
  private Integer rmtType;
  
  @Column(name = "exchg_rate", nullable=true)
  private BigDecimal exchgRate = BigDecimal.ZERO;
  
  @Column(name = "fee", nullable=true)
  private BigDecimal fee = BigDecimal.ZERO;  
  
  public void setInstAmt(BigDecimal amt) {
    this.instAmt = amt;
  }
  
  public void setInstAmt(Integer amt) {
    this.instAmt = BigDecimal.valueOf(amt);
  }

  public void setInstAmt(Float amt) {
    this.instAmt = BigDecimal.valueOf(amt);
  }
  //  
  public void setExchgRate(BigDecimal amt) {
    this.exchgRate = amt;
  }
  
  public void setExchgRate(Integer amt) {
    this.exchgRate = BigDecimal.valueOf(amt);
  }
  
  public void setExchgRate(Float amt) {
    this.exchgRate = BigDecimal.valueOf(amt);
  }  
  //  
  public void setFee(BigDecimal amt) {
    this.fee = amt;
  }
  
  public void setFee(Integer amt) {
    this.fee = BigDecimal.valueOf(amt);
  }
  
  public void setFee(Float amt) {
    this.fee = BigDecimal.valueOf(amt);
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
    BillRpt other = (BillRpt) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  
}
