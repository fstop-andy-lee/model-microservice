package tw.com.firstbank.service;

import tw.com.firstbank.model.MasterDto;

public interface JournalService {
  public void sagaUpdate(MasterDto dto); 
  
  public void checkAndLock(MasterDto dto);
  public void unLock(MasterDto dto);
  
  public boolean logExist(MasterDto dto);
  public boolean compensateComplete(MasterDto dto);
  public boolean allowCompensate(MasterDto dto);
  public boolean needExecute(MasterDto dto);
  public void compensateSave(MasterDto dto);
  
  public void save(MasterDto dto);  
}
