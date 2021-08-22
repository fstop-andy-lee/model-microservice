package tw.com.firstbank.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rmt_cb_rpt3")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "valueDate", "idno", "ccy", "dataAmt", "swiftBank"})
public class RmtCbRpt3 implements Serializable {
  
  private static final long serialVersionUID = -4257129931235371776L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  
  @Column(name = "job_code")
  private String jobCode = "3";
  
  @Column(name = "bank")
  private String bank;
  
  @Column(name = "ccy")
  private String ccy;
  
  @Column(name = "rem_type")
  private String remType;
  
  @Column(name = "value_date")
  private String valueDate;
  
  @Column(name = "data_no")
  private String dataNo;
  
  @Column(name = "ban_idno")
  private String idno;
  
  @Column(name = "birth_date")
  private String birthDate;
 
  @Column(name = "issue_date")
  private String issueDate;

  @Column(name = "exp_date")
  private String expDate;
  
  @Column(name = "kind_code")
  private String kindCode;
  
  @Column(name = "data_kind")
  private String dataKind;
  
  @Column(name = "data_amt_sign")
  private String dataAmtSign;
  
  @Column(name = "data_amt")
  private BigDecimal dataAmt = BigDecimal.ZERO;
  
  @Column(name = "forward_split")
  private String forwardSplit;

  @Column(name = "class")
  private String clazz;

  @Column(name = "top")
  private String top;
  
  @Column(name = "sub_code_690")
  private String subCode690;

  @Column(name = "cntry")
  private String cntry;

  @Column(name = "no_rep_flag")
  private String noRepFlag;

  @Column(name = "q_id")
  private String qId;

  @Column(name = "natural_flag")
  private String naturalFlag;

  @Column(name = "aprov_no_flag")
  private String aprovNoFlag;

  @Column(name = "spec_bank")
  private String specBank;

  @Column(name = "docu_no")
  private String docNo;
  
  @Column(name = "nt_exchg_rate")
  private BigDecimal ntExchgRate = BigDecimal.ZERO;

  @Column(name = "oppo_name")
  private String oppoName;
  
  @Column(name = "swift_bank")
  private String swiftBank;  // input bank
  
  @Column(name = "oppo_bank")
  private String oppoBank;

  @Column(name = "trade_type")
  private String tradeType;

  @Column(name = "trade_date")
  private String tradeDate;

  @Column(name = "data_source")
  private String dataSource;

  public void setDataAmt(BigDecimal amt) {
    this.dataAmt = amt;
  }
  
  public void setDataAmt(Integer amt) {
    this.dataAmt = BigDecimal.valueOf(amt);
  }

  public void setDataAmt(Float amt) {
    this.dataAmt = BigDecimal.valueOf(amt);
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
    RmtCbRpt3 other = (RmtCbRpt3) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  
}
