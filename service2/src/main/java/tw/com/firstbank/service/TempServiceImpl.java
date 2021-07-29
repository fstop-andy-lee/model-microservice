package tw.com.firstbank.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import tw.com.firstbank.entity.ServiceLog1;
import tw.com.firstbank.entity.ServiceLogKey;
import tw.com.firstbank.entity.Temp;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.ServiceLog1Repository;
import tw.com.firstbank.repository.TempRepository;

@Service
@Slf4j
public class TempServiceImpl implements TempService {

  @Autowired
  TempRepository tempRepo;
  
  @Autowired
  ServiceLog1Repository log1Repo;
  
  @Autowired
  RabbitTemplate rabbitTemplate;
  
  private Optional<ServiceLog1> getLog1(String id, Integer seq) {
    ServiceLogKey key = new ServiceLogKey(id, seq);
    Optional<ServiceLog1> opt = log1Repo.findById(key);
    return opt;    
  }
  
  @Override
  public boolean logExist(MasterDto dto) {
    Optional<ServiceLog1> opt = getLog1(dto.getUuid(), dto.getSeq());
    if (opt.isPresent()) return true;
    return false;
  }

  @Override
  public boolean compensateComplete(MasterDto dto) {
    Optional<ServiceLog1> opt = getLog1(dto.getUuid(), dto.getSeq());
    if (opt.isPresent() && opt.get().getStatus() == 1) return true;
    return false;    
  }

  @Override
  public boolean allowCompensate(MasterDto dto) {
    Optional<ServiceLog1> opt = getLog1(dto.getUuid(), dto.getSeq());
    if (opt.isPresent() && opt.get().getStatus() == 0) return true;
    return false;
  }

  @Override
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
  @Transactional
  public void compensateSave(MasterDto dto) {
    if (needExecute(dto) == false) {
      log.debug("Compensate no need execute");
      return;
    }
    
    Optional<Temp> opt = tempRepo.findById(dto.getId());
    if (opt.isPresent() == false) {
      throw new IllegalStateException("Not Found!");      
    }
    
    Temp temp = opt.get();
    log.debug("Before Compensate temp = {}", temp.toString());
   
    temp.setStatus(0);
    tempRepo.save(temp);
    tempRepo.flush(); 
    
    // log1
    ServiceLog1 log1 = getLog1(dto.getUuid(), dto.getSeq()).get();
    log1.setStatus(1);
    log1Repo.save(log1);
    log1Repo.flush();        
  }

  @Override
  @Transactional
  public void save(MasterDto dto) {
    if (needExecute(dto) == false) {
      log.debug("save no need execute");
      return;
    }
    
    Optional<Temp> opt = tempRepo.findById(dto.getId());
    if (opt.isPresent() == false) {
      throw new IllegalStateException("Not Found!");      
    }
    
    Temp temp = opt.get();
    log.debug("Before save temp = {}", temp.toString());
   
    temp.setStatus(dto.getStatus());
    tempRepo.save(temp);
    tempRepo.flush();    
    
    // log1
    ServiceLog1 log1 = new ServiceLog1();
    log1.setId(dto.getUuid());
    log1.setSeq(dto.getSeq());
    log1.setTs(LocalDateTime.now());
    log1.setStatus(0);
    log1Repo.save(log1);
    log1Repo.flush();    
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public void checkAndLock(MasterDto dto) {
    Optional<Temp> opt = tempRepo.findById(dto.getId());
    Temp temp = null;
    if (!opt.isPresent()) {
      //throw new IllegalStateException("Not Found!");
      temp = new Temp();
      temp.setId(dto.getId());      
    } else {
      temp = opt.get();
    }
    
    if (StringUtils.hasText(temp.getHoldMark()) == true &&
        temp.getHoldMark().equalsIgnoreCase(dto.getUuid()) == false
        ) {
      throw new IllegalStateException("Lock by Others!");            
    }
        
    temp.setHoldMark(dto.getUuid());
    tempRepo.save(temp);
    tempRepo.flush();    
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public void unLock(MasterDto dto) {
    Optional<Temp> opt = tempRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      return;     
    }    
    
    if (StringUtils.hasText(opt.get().getHoldMark()) == true &&
        opt.get().getHoldMark().equalsIgnoreCase(dto.getUuid()) == true
        ) {
      Temp temp = opt.get();
      temp.setHoldMark(null);
      tempRepo.save(temp);
      tempRepo.flush();
      log.debug("Unlock result temp = {}", temp.toString());
    }        
  }

  @Override
  @Transactional
  public void sagaUpdate(MasterDto dto) {

    try {
      checkAndLock(dto);
      
      if (dto.isCompensate()) {
        this.compensateSave(dto);
      } else {
        this.save(dto);
      }
      
    } finally {
      unLock(dto);      
    }    
  }
  
  @SuppressWarnings("unused")
  private void sendSagaEvent(String queueName, MasterDto dto) {
    //rabbitTemplate.setChannelTransacted(true);
    rabbitTemplate.convertAndSend(queueName, dto);    
  }
  
  @Transactional
  @RabbitListener(queues = "tempQueue")  
  public MasterDto sagaUpdateByEvent(MasterDto dto) {
    try {
      log.debug("Temp Queue input = {}", dto.toString());
      checkAndLock(dto);
      
      if (dto.isCompensate()) {
        log.debug("Temp Compensate");
        this.compensateSave(dto);
      } else {
        this.save(dto);
      }
      log.debug("Temp Rep to Orch Queue");
      //sendSagaEvent("orchQueue", dto);
      return dto;
      
    } finally {
      unLock(dto);      
    }    
  }

}
