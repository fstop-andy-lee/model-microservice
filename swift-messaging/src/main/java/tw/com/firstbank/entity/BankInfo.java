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
@Table(name = "bank_info")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "bic", "isCorr"})
public class BankInfo implements Serializable {
  
  private static final long serialVersionUID = 9131989457993100598L;
  
  @Id
  @Column(name = "id", nullable=false)
  private String id;
  
  @Column(name = "bic", nullable=true)
  private String bic;
  
  // International Bank Account Number (EUR)
  @Column(name = "iban", nullable=true)
  private String iban;
  
  // American Ba​​nkers Association (US)
  @Column(name = "aba_no", nullable=true)
  private String abaNo;
  
  // Bank State Branch Number (AU)
  @Column(name = "bsb_no", nullable=true)
  private String bsbNo;
  
  @Column(name = "name", nullable=true)
  private String name;
  
  @Column(name = "addr", nullable=true)
  private String addr;
  
  @Column(name = "corr_flag", nullable=true)
  private Boolean isCorr;

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
    BankInfo other = (BankInfo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  

}
