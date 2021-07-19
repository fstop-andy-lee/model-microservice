package tw.com.firstbank.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "holdMark", "balance", "status", "cif"})
public class MasterDto {

  private String id;
  
  private Integer holdMark;
  
  private BigDecimal balance;
  
  private Integer status;
  
  private String cif;
  
  private String uuid = UUID.randomUUID().toString();
  
  private Integer seq = 0;
  
  private boolean compensate = false;
  
  public void setBalance(BigDecimal amt) {
    this.balance = amt;
  }
  
  public void setBalance(Integer amt) {
    this.balance = BigDecimal.valueOf(amt);
  }

  public void setBalance(Float amt) {
    this.balance = BigDecimal.valueOf(amt);
  }  
}
