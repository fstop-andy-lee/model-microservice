package tw.com.firstbank.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/*
 * 0 = 待處理
   1 = 待人工處理
   2 = 電文錯誤
   3 = 註銷
   9 = 已處理  
 */
public enum SwiftMessageStatus {
  /**
   * 0 = 待處理
   */
  INACTIVE(0),
  /**
   * 1 = 待人工處理
   */
  PENDING(1),
  /**
   * 2 = 電文錯誤
   */
  ERROR(2),
  /**
   * 3 = 註銷
   */
  DELETED(3),
  /**
   * 9 = 已處理
   */
  DONE(9),  
  ;

  private Integer value;

  private SwiftMessageStatus(Integer value)  {
     this.value = value;
  }

  @JsonValue
  public Integer getValue()  {
     return value;
  }
  
  @JsonCreator
  public static SwiftMessageStatus fromInteger(Integer value){
     if (value == null)  return null;

     switch(value)  {
        case 0 : return INACTIVE;
        case 1 : return PENDING;
        case 2 : return ERROR;
        case 3 : return DELETED;
        case 9 : return DONE;
        default: return INACTIVE;
     }
 }
}
