package tw.com.firstbank.service;

import java.util.List;

import tw.com.firstbank.model.MasterDto;

public interface MasterService {

  public void save(MasterDto dto);
  
  public List<MasterDto> findAll();
  
}
