package tw.com.firstbank.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
  /**
   * 0: 停用, 失敗(Meas Status)
   */
  INACTIVE(0),
  /**
   * 1: 正常, 成功(Meas Status)
   */
  ACTIVE(1),
  /**
   * 2: 待驗證(新增模式下為此預設值)
   */
  DISCREDIT(2);

  private Integer value;

  private UserStatus(Integer value)  {
     this.value = value;
  }

  @JsonValue
  public Integer getValue()  {
     return value;
  }
  
  @JsonCreator
  public static UserStatus fromInteger(Integer value){
     if (value == null)  return null;

     switch(value)  {
        case 0 : return INACTIVE;
        case 1 : return ACTIVE;
        case 2 : return DISCREDIT;
        default: return DISCREDIT;
     }
 }
}
