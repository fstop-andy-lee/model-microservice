package tw.com.firstbank.service;

import java.util.List;

import tw.com.firstbank.entity.SwiftMessageLog;

public interface RepositoryHelper {
  List<SwiftMessageLog> findInactiveMsgs(Integer records);
  public String saveInactiveSwiftMessageLog(String content);
  public void markPending(SwiftMessageLog msgLog);
  public void markDone(SwiftMessageLog msgLog);  
  public void markDone(String msgKey);
  
  public void parseComplete(SwiftMessageLog msg);
  public void parsePending(SwiftMessageLog msg);
}
