package tw.com.firstbank.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.entity.Master;
import tw.com.firstbank.entity.ServiceLog0;
import tw.com.firstbank.entity.ServiceLogKey;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.repository.MasterRepository;
import tw.com.firstbank.repository.ServiceLog0Repository;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {

  @Autowired
  MasterRepository masterRepo;
  
  @Autowired
  ServiceLog0Repository log0Repo;
  
  String updateMasterUrl = "http://localhost:8080/api/master";
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  
  @Transactional
  public void sagaUpdate(MasterDto dto) {
    
    try {
      // init hold mark
      dto.setHoldMark(dto.getUuid());
      dto.setSeq(0);
      
      checkAndLock(dto);

      // 1
      dto.setSeq(1);
      save(dto);
      
      Integer threadCount = 1;
      Integer timeoutSeconds = 5;
      ExecutorService executor = Executors.newFixedThreadPool(threadCount);
      CountDownLatch cdt = new CountDownLatch(1);
      
      executor.submit(() -> {
        String threadName = Thread.currentThread().getName();
        log.debug("Thread name: {}", threadName);
        // 呼叫 
        
        //cdt.countDown();
        try {
          Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {          
          e.printStackTrace();
        }
        
        return;
      });
      
      try {
        Boolean finished = cdt.await(timeoutSeconds, TimeUnit.SECONDS);
        if (finished) {
          log.debug("CountDownLatch finished");    
        } else {
          // 補償
          log.debug("CountDownLatch timeout");  
          this.compensateSave(dto);
        }
        
        executor.shutdown();
        log.debug("Executor shutdown");
        executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
        log.debug("Executor termination");
      } catch (InterruptedException e) {
        log.error("tasks interrupted");
      } finally {
        if (!executor.isTerminated()) {
          log.error("cancel non-finished tasks");
        }
        executor.shutdownNow();
        log.debug("shutdown finished");
      }

      
    } finally {
      unLock(dto);      
    }
    
  }
 
  private void updateMaster(MasterDto dto) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    String req = null;
    try {
      req = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Convert json error ", e);
      throw new IllegalStateException("Convert Fail"); 
    }
    log.debug("Req = {}", req);
    HttpEntity<String> request = new HttpEntity<String>(req, headers);
    
    String rep = restTemplate.postForObject(updateMasterUrl, request, String.class);
    log.debug("Rep = {}", rep);
    if ("OK".equalsIgnoreCase(rep) == false) {
      throw new IllegalStateException("Update Fail");    
    }
  }
  
  @Transactional(value = TxType.REQUIRES_NEW)
  public void checkAndLock(MasterDto dto) {
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      throw new IllegalStateException("Not Found!");      
    }
    if (StringUtils.hasText(opt.get().getHoldMark()) == true &&
        opt.get().getHoldMark().equalsIgnoreCase(dto.getUuid()) == false
        ) {
      throw new IllegalStateException("Lock by Others!");            
    }
    
    Master master = opt.get();
    master.setHoldMark(dto.getUuid());
    masterRepo.save(master);
    masterRepo.flush();
  }
  
  @Transactional(value = TxType.REQUIRES_NEW) 
  public void unLock(MasterDto dto) {
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      return;     
    }    
    
    if (StringUtils.hasText(opt.get().getHoldMark()) == true &&
        opt.get().getHoldMark().equalsIgnoreCase(dto.getUuid()) == true
        ) {
      Master master = opt.get();
      master.setHoldMark(null);
      masterRepo.save(master);
      masterRepo.flush();
      log.debug("Unlock result master = {}", master.toString());
    }    
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
  @Transactional
  public void save(MasterDto dto) {

    if (needExecute(dto) == false) {
      return;
    }
    
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (opt.isPresent() == false) {
      throw new IllegalStateException("Not Found!");      
    }
    
    Master master = opt.get();
    log.debug("Before save master = {}", master.toString());
   
    master.setBalance(Float.valueOf(dto.getBalance()));
    master.setStatus(dto.getStatus());
    masterRepo.save(master);
    masterRepo.flush();
  }
  
  @Override
  @Transactional
  public void compensateSave(MasterDto dto) {
    if (needExecute(dto) == false) {
      return;
    }
    
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (opt.isPresent() == false) {
      throw new IllegalStateException("Not Found!");      
    }
    
    Master master = opt.get();
    log.debug("Before Compensate master = {} ", master.toString());
   
    master.setBalance(master.getBalance().floatValue() - Float.valueOf(dto.getBalance()));
    master.setStatus(0);
    
    masterRepo.save(master);
    masterRepo.flush();
  }
  
  @Override
  public List<MasterDto> findAll() {
    return null;
  }
  
}
