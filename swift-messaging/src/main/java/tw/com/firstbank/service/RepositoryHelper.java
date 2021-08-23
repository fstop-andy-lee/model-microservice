package tw.com.firstbank.service;

import java.util.List;

import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.RmtAdvice;
import tw.com.firstbank.entity.SwiftMessageLog;

public interface RepositoryHelper {
  List<SwiftMessageLog> findInactiveMsgs(Integer records);
  public String saveInactiveSwiftMessageLog(String content);
  public void markPending(SwiftMessageLog msgLog);
  public void markDone(SwiftMessageLog msgLog);  
  public void markDone(String msgKey);
  
  public void saveInwardRmt(InwardRmt rmt);
  public void saveRmtAdvice(RmtAdvice advice);
  public Master findMasterByAcct(String acct);
  public void markVerifyPending(InwardRmt rmt);
  public void markVerifyDone(InwardRmt rmt);
  public void markPayment(InwardRmt rmt);
  
  public void parseComplete(SwiftMessageLog msg);
  public void parseComplete(SwiftMessageLog msg, InwardRmt rmt, Bafotr otr);
  public void parsePending(SwiftMessageLog msg);
  public void payment(InwardRmt rmt);
}
