package tw.com.firstbank.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/*
 *      
   0 = 未審核
   1 = 待審核
   2 = 不通過
   9 = 已通過
 */
public enum VerifyStatus {
  /**
   * 0 = 未審核
   */
  INACTIVE(0),
  /**
   * 1 = 待審核
   */
  PENDING(1),
  /**
   * 2 = 不通過
   */
  REJECT(2),
  /**
   * 9 = 已通過
   */
  DONE(9),  
  ;

  private Integer value;

  private VerifyStatus(Integer value)  {
     this.value = value;
  }

  @JsonValue
  public Integer getValue()  {
     return value;
  }
  
  @JsonCreator
  public static VerifyStatus fromInteger(Integer value){
     if (value == null)  return null;

     switch(value)  {
        case 0 : return INACTIVE;
        case 1 : return PENDING;
        case 2 : return REJECT;
        case 9 : return DONE;
        default: return INACTIVE;
     }
 }
}
