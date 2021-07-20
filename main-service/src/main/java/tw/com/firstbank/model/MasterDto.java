package tw.com.firstbank.model;

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
  
  private String holdMark;
  
  private String balance;
  
  private Integer status;
  
  private String cif;
  
  private String uuid = UUID.randomUUID().toString();
  
  private Integer seq = 0;
  
  private Integer cond;
  
  private boolean compensate = false;
  
}
