package tw.com.firstbank.service;

import java.util.List;

import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.SwiftMessageLog;

public interface RepositoryHelper {
  List<SwiftMessageLog> findInactiveMsgs(Integer records);
  public String saveInactiveSwiftMessageLog(String content);
  public void markPending(SwiftMessageLog msgLog);
  public void markDone(SwiftMessageLog msgLog);  
  public void saveInwardRmt(InwardRmt rmt);
  
  public void parseComplete(SwiftMessageLog msg, InwardRmt rmt, Bafotr otr);
  public void parsePending(SwiftMessageLog msg);
}
