package tw.com.firstbank.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.ServiceLog0;
import tw.com.firstbank.entity.ServiceLogKey;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.MasterRepository;
import tw.com.firstbank.repository.ServiceLog0Repository;

@Service
public class MasterServiceImpl implements MasterService {

  @Autowired
  MasterRepository masterRepo;
  
  @Autowired
  ServiceLog0Repository log0Repo;
  
  @Override
  @Transactional
  public void save(MasterDto dto) {

    if (needExecute(dto) == false) {
      return;
    }
    
    checkAndLock(dto);
    
    Optional<Master> opt = masterRepo.findById(dto.getId());
    Master master = opt.get();
    System.out.println(master.toString());
   
    master.setBalance(dto.getBalance());
    master.setStatus(dto.getStatus());
    masterRepo.save(master);
    masterRepo.flush();
    
    unLock(dto);
  }
  
  @Transactional(value = TxType.REQUIRES_NEW)
  public void checkAndLock(MasterDto dto) {
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      throw new IllegalStateException("Not Found!");      
    }
    if (opt.get().getHoldMark() == 1) {
      throw new IllegalStateException("Lock by Others!");            
    }
    
    Master master = opt.get();
    master.setHoldMark(1);
    masterRepo.save(master);
    masterRepo.flush();
  }
  
  @Transactional(value = TxType.REQUIRES_NEW) 
  public void unLock(MasterDto dto) {
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      return;     
    }    
    
    Master master = opt.get();
    master.setHoldMark(0);
    masterRepo.save(master);
    masterRepo.flush();
  }
  
  
  private Optional<ServiceLog0> getLog0(String id, Integer seq) {
    ServiceLogKey key = new ServiceLogKey(id, seq);
    Optional<ServiceLog0> opt = log0Repo.findById(key);
    return opt;    
  }
  
  public boolean logExist(MasterDto dto) {
    Optional<ServiceLog0> opt = getLog0(dto.getUuid(), dto.getSeq());
    if (opt.isPresent()) return true;
    return false;
  }
  
  public boolean compensateComplete(MasterDto dto) {
    Optional<ServiceLog0> opt = getLog0(dto.getUuid(), dto.getSeq());
    if (opt.isPresent() && opt.get().getStatus() == 1) return true;
    return false;    
  }
  
  public boolean allowCompensate(MasterDto dto) {
    Optional<ServiceLog0> opt = getLog0(dto.getUuid(), dto.getSeq());
    if (opt.isPresent() && opt.get().getStatus() == 0) return true;
    return false;
  }
  
  public boolean needExecute(MasterDto dto) {
    if (dto.isCompensate() == false && logExist(dto) == false) {
      return true;
    }
    
    if (dto.isCompensate() && allowCompensate(dto)) {
      return true;
    }
    return false;    
  }
  
  @Override
  public List<MasterDto> findAll() {
    return null;
  }
  
}
