package tw.com.firstbank.service;

import java.util.List;

import tw.com.firstbank.model.MasterDto;

public interface MasterService {

  public void sagaUpdate(MasterDto dto);  
  public boolean logExist(MasterDto dto);
  public boolean compensateComplete(MasterDto dto);
  public boolean allowCompensate(MasterDto dto);
  public boolean needExecute(MasterDto dto);
  public void compensateSave(MasterDto dto);
  
  public void save(MasterDto dto);  
  public List<MasterDto> findAll();
  
  public void sendTestMsg(String msg);
}
