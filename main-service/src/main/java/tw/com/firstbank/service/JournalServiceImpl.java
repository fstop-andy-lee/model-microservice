package tw.com.firstbank.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.entity.Journal;
import tw.com.firstbank.entity.JournalKey;

import tw.com.firstbank.entity.ServiceLog2;
import tw.com.firstbank.entity.ServiceLogKey;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.JournalRepository;
import tw.com.firstbank.repository.ServiceLog2Repository;

@Service
@Slf4j
public class JournalServiceImpl implements JournalService {

  @Autowired
  JournalRepository journalRepo;
  
  @Autowired
  ServiceLog2Repository log2Repo;
  
  @Autowired
  RabbitTemplate rabbitTemplate;
  
  private Optional<ServiceLog2> getLog2(String id, Integer seq) {
    ServiceLogKey key = new ServiceLogKey(id, seq);
    Optional<ServiceLog2> opt = log2Repo.findById(key);
    return opt;    
  }
  
  @Override
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
    

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public void checkAndLock(MasterDto dto) {    
    //Journal journal = null;
    
    // journal 只有新增、刪除  
    // always success
    
    /*
    Optional<Journal> opt = journalRepo.findById(new JournalKey(dto.getUuid(), dto.getSeq()));
    if (!opt.isPresent()) {
      //throw new IllegalStateException("Not Found!");
      journal = new Journal();
      journal.setId(dto.getUuid());
      journal.setSeq(dto.getSeq());
      
    } else {
      journal = opt.get();
    }
    
    journal.setStatus(dto.getStatus());
    journal.setBalance(Float.valueOf(dto.getBalance()));
    journalRepo.save(journal);
    journalRepo.flush();    
    */
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public void unLock(MasterDto dto) {
    // dummy    
  }

  @Override
  public boolean logExist(MasterDto dto) {
    Optional<ServiceLog2> opt = getLog2(dto.getUuid(), dto.getSeq());
    if (opt.isPresent()) return true;
    return false;
  }

  @Override
  public boolean compensateComplete(MasterDto dto) {
    Optional<ServiceLog2> opt = getLog2(dto.getUuid(), dto.getSeq());
    if (opt.isPresent() && opt.get().getStatus() == 1) return true;
    return false;    
  }

  @Override
  public boolean allowCompensate(MasterDto dto) {
    Optional<ServiceLog2> opt = getLog2(dto.getUuid(), dto.getSeq());
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
  public void compensateSave(MasterDto dto) {
    if (needExecute(dto) == false) {
      log.debug("No need execute");
      return;
    }
    
    Optional<Journal> opt = journalRepo.findById(new JournalKey(dto.getUuid(), dto.getSeq()));
    if (opt.isPresent()) {
      log.debug("Journal = {}", opt.get().toString());
      journalRepo.delete(opt.get());
      journalRepo.flush();
    }
    
    // 如果 journal 不在就不處理
    
    // log2
    ServiceLog2 log2 = getLog2(dto.getUuid(), dto.getSeq()).get();
    log2.setStatus(1);
    log2Repo.save(log2);
    log2Repo.flush();    
  }

  @Override
  public void save(MasterDto dto) {
    if (needExecute(dto) == false) {
      log.debug("No need execute");
      return;
    }
    
    // journal save append only
    Journal journal = null;
    journal = new Journal();
    journal.setId(dto.getUuid());
    journal.setSeq(dto.getSeq());
   
    journal.setStatus(dto.getStatus());
    journal.setBalance(Float.valueOf(dto.getBalance()));
    journalRepo.save(journal);
    journalRepo.flush();   
    
    // log2
    ServiceLog2 log2 = new ServiceLog2();
    log2.setId(dto.getUuid());
    log2.setSeq(dto.getSeq());
    log2.setTs(LocalDateTime.now());
    log2.setStatus(0);
    log2Repo.save(log2);
    log2Repo.flush();    
    
  }
  
  @SuppressWarnings("unused")
  private void sendSagaEvent(String queueName, MasterDto dto) {
    //rabbitTemplate.setChannelTransacted(true);
    rabbitTemplate.convertAndSend(queueName, dto);    
  }
  
  @Transactional
  @RabbitListener(queues = "journalQueue")  
  public MasterDto sagaUpdateByEvent(MasterDto dto) {    
    try {
      log.debug("Journal Queue input = {}", dto.toString());
      checkAndLock(dto);

      // for test
      if (dto.isCompensate() == false) {
        if (dto.getTestCase() == 2) {
          log.debug("Test case 2: journal timeout");
          try { Thread.sleep(13 * 1000); } catch (InterruptedException e) { e.printStackTrace(); }  
          return null;
        } else if (dto.getTestCase() == 1) {
          log.debug("Test case 1: journal hold");
          try { Thread.sleep(5 * 1000); } catch (InterruptedException e) { e.printStackTrace(); }        
        }
      }
      
      if (dto.isCompensate()) {
        log.debug("Journal Compensate");
        this.compensateSave(dto);
      } else {
        this.save(dto);
      }
      
      log.debug("Journal Rep to Orch Queue");
      //sendSagaEvent("orchQueue", dto);
      return dto;
      
    } finally {
      unLock(dto);
    }
    
  }
}
