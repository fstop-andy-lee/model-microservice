package tw.com.firstbank.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"apId", "serviceId", "ltId", "sessionNo", "seqNo"})
public class SwiftBasicHeader {

  public static String APID_FIN = "F"; // 金融業電文
  
  public static String APID_GPA = "A"; // 一般用途電文
  
  public static String APID_LOGIN = "L"; // 系統簽入電文
  
  public static Integer SWIFT_MESSAGE = 1;

  private String apId = "";  // 1 位 字元
  
  private Integer serviceId; // 2 位 數字
  
  private String ltId = ""; // 12位 字元 logical terminal id 邏輯終端機代號
  
  private Integer sessionNo; // 4 位 數字
  
  private Integer seqNo; // 6 位 數字
  
  public SwiftBasicHeader(String msg) {
    this.apId = msg.substring(0, 1);
    this.serviceId = parseInt(msg, 1, 2);
    this.ltId = msg.substring(3,  12);
    this.sessionNo = parseInt(msg, 15, 4);
    // apID = "L" 無 SeqNo
    if (msg.length() < 19) {
      return;
    }
    this.seqNo = parseInt(msg, 19, 6);
  }

  public Boolean isValidApId(String id) {
    switch (id) {
      case "F":
        return true;
      case "A":
        return true;
      case "L":
        return true;
      default:
        return false;
    }
  }
  
  private Integer parseInt(String msg, Integer start, Integer end) {
    try {
      return Integer.parseInt(msg.substring(start, end+1));
    } catch(Exception e) {
      //ignored
      //e.printStackTrace();
    }
    return 0;
  }
  
}
