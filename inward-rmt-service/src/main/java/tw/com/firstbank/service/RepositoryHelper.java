package tw.com.firstbank.service;


import tw.com.firstbank.entity.Bafotr;
import tw.com.firstbank.entity.InwardRmt;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.RmtAdvice;

public interface RepositoryHelper {

  public void saveInwardRmt(InwardRmt rmt);
  public void saveRmtAdvice(RmtAdvice advice);
  public Master findMasterByAcct(String acct);
  public void markVerifyPending(InwardRmt rmt);
  public void markVerifyDone(InwardRmt rmt);
  public void markPayment(InwardRmt rmt);
  
  public void parseComplete(InwardRmt rmt, Bafotr otr);
  public void payment(InwardRmt rmt);
}
