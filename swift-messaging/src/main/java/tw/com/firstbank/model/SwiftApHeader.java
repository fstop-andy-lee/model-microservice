package tw.com.firstbank.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"io", "msgType", "rcvAddr", "priority", "monitor", "period"})
public class SwiftApHeader {

  public static String SWIFT_INPUT = "I";  // We are Sender
  public static String SWIFT_OUTPUT = "O"; // We are Recipient
  
  public static String PRIORITY_UERGEN = "U";
  public static String PRIORITY_NORMAL = "N";
  public static String PRIORITY_SYSTEM = "S";

  public static String MONITOR_NON_WARNING = "1";
  public static String MONITOR_NOTIFICATION = "2";
  public static String MONITOR_BOTH = "3";
  
  public static String PERIOD_NORMAL = "020";
  public static String PERIOD_UERGEN = "030";
  
  private String io = "";
  
  private String msgType = "";
  
  private String rcvAddr = "";
  
  private String priority = PRIORITY_NORMAL;
  
  private String monitor = MONITOR_BOTH;
  
  private String period = PERIOD_NORMAL;
  
  private String inputTime = "";
  
  private String mir = ""; // message input reference
  
  private String outputDate = ""; // YYMMDD
  
  private String outputTime = ""; // HHMM
  
  public SwiftApHeader(String msg) {
    this.io = msg.substring(0,  0+1);
    this.msgType = msg.substring(1, 1+3);
    if ("I".equalsIgnoreCase(this.io)) {
      this.rcvAddr = msg.substring(4, 4+12);
      if (msg.length() >= 17) {
        this.priority = msg.substring(16, 16+1);
      }
      if (msg.length() >= 18) {
        this.monitor = msg.substring(17, 17+1);
      }
      if (msg.length() >= 21) {
        this.period = msg.substring(18, 18+3);
      }      
    } else {
      this.inputTime = msg.substring(4, 4+4);
      this.mir = msg.substring(8, 8+28);
      this.outputDate = msg.substring(36, 36+6);
      this.outputTime = msg.substring(42, 42+4);
      this.priority = msg.substring(46, 46+1);
    }
  }
  
  public Boolean isValidIO(String id) {
    switch (id) {
      case "I":
        return true;
      case "O":
        return true;
      default:
        return false;
    }
  }
  
  public Boolean isValidPriority(String id) {
    switch (id) {
      case "S":
        return true;
      case "U":
        return true;
      case "N":
        return true;
      default:
        return false;
    }
  }
  
  public Boolean isValidMonitor(String id) {
    switch (id) {
      case "1":
        return true;
      case "2":
        return true;
      case "3":
        return true;
      default:
        return false;
    }
  }  

  /**
   * 電文輸入單位代號
   * @param mir
   * @return
   */
  public String getMirBic(String mir) {
    // MIR BIC 組成 = BBBBBBBB + L + XXX
    // 轉換過 BIC 組成 = BBBBBBBB + XXXX + L
    String bic = subStr(mir, 6, 8) + subStr(mir, 15, 3) + subStr(mir, 14, 1);
    return bic;
  }
  
  /**
   * 電文輸入通道編號
   * @param mir
   * @return
   */
  public String getMirSessionNo(String mir) {
    return subStr(mir, 18, 4);
  }
  
  /**
   * 電文輸入序號
   * @param mir
   * @return
   */
  public String getMirSeqNo(String mir) {
    return subStr(mir, 22, 6);
  }
  
  private String subStr(String str, Integer start, Integer end) {
    try {
      return str.substring(start, end);
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
