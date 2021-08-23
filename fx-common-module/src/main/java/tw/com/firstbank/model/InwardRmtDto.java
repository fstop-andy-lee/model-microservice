package tw.com.firstbank.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "status", "verifyStatus", "valueDate", "ccy", "instAmt", "benefCust"})
public class InwardRmtDto implements Serializable {

  private static final long serialVersionUID = 1978247554504847660L;

  private String id;
  
  private Integer status = 0;
  
  private Integer verifyStatus = 0;
  
  private String txnRefNo;

  private String bankOpCode;

  private Integer valueDate;
  
  private String ccy;

  private BigDecimal instAmt;
  
  private String orderCust;
  
  private String benefCust;
  
  private String benefAcct;
  
  private String benefName;

  private String senderCorr;
  
  private String receiverCorr;
  
  private String rmtInfo;
  
  private String detailCharge;
  
  private String senderChargeCcy;
  
  private BigDecimal senderCharge;
  
  private String receiverChargeCcy;
  
  private BigDecimal receiverCharge;

  private String acctInst;
  
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

  
}
