package tw.com.firstbank.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tw.com.firstbank.domain.type.InwardRmtStatus;
import tw.com.firstbank.domain.type.InwardRmtStatusConverter;
import tw.com.firstbank.domain.type.VerifyStatus;
import tw.com.firstbank.domain.type.VerifyStatusConverter;

@Entity
@Table(name = "inward_rmt")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "status", "verifyStatus", "instAmt", "valueDate", "ccy", "benefCust"})
public class InwardRmt implements Serializable {

  private static final long serialVersionUID = 3479500368711395238L;

  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "status", nullable=true)
  @Convert(converter = InwardRmtStatusConverter.class)
  private InwardRmtStatus status = InwardRmtStatus.INACTIVE;
  
  @Column(name = "verify_status", nullable=true)
  @Convert(converter = VerifyStatusConverter.class)
  private VerifyStatus verifyStatus = VerifyStatus.INACTIVE;
  
  @Column(name = "txn_ref_no", nullable=true)
  private String txnRefNo;

  @Column(name = "bank_op_code", nullable=true)
  private String bankOpCode;

  @Column(name = "value_date", nullable=true)
  private Integer valueDate;
  
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

  @Column(name = "sender_corr", nullable=true)
  private String senderCorr;
  
  @Column(name = "receiver_corr", nullable=true)
  private String receiverCorr;
  
  @Column(name = "rmt_info", nullable=true)
  private String rmtInfo;
  
  @Column(name = "detail_charge", nullable=true)
  private String detailCharge;
  
  @Column(name = "sender_charge_ccy", nullable=true)
  private String senderChargeCcy;
  
  @Column(name = "sender_charge", nullable=true)
  private BigDecimal senderCharge;
  
  @Column(name = "receiver_charge_ccy", nullable=true)
  private String receiverChargeCcy;
  
  @Column(name = "receiver_charge", nullable=true)
  private BigDecimal receiverCharge;

  @Column(name = "acct_inst", nullable=true)
  private String acctInst;
  
  @Column(name = "bank_info", nullable=true)
  private String bankInfo;

  public void setInstAmt(BigDecimal amt) {
    this.instAmt = amt;
  }
  
  public void setInstAmt(Integer amt) {
    this.instAmt = BigDecimal.valueOf(amt);
  }

  public void setInstAmt(Float amt) {
    this.instAmt = BigDecimal.valueOf(amt);
  }
    
  public void setSenderCharge(BigDecimal amt) {
    this.senderCharge = amt;
  }
  
  public void setSenderCharge(Integer amt) {
    this.senderCharge = BigDecimal.valueOf(amt);
  }

  public void setSenderCharge(Float amt) {
    this.senderCharge = BigDecimal.valueOf(amt);
  }  
  
  public void setReceiverCharge(BigDecimal amt) {
    this.receiverCharge = amt;
  }
  
  public void setReceiverCharge(Integer amt) {
    this.receiverCharge = BigDecimal.valueOf(amt);
  }

  public void setReceiverCharge(Float amt) {
    this.receiverCharge = BigDecimal.valueOf(amt);
  }
  
  public Boolean isVerifyDone() {
    return this.verifyStatus == VerifyStatus.DONE;
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
    InwardRmt other = (InwardRmt) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }  

  
}
