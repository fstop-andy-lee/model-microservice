package tw.com.firstbank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  
  @Autowired
  RabbitTemplate rabbitTemplate;
  
  @Value("${service1-url:http://localhost:8080/api/master}")
  String updateMasterUrl;
  
  @Value("${service2-url:http://localhost:8081/api/temp}")
  String updateTempUrl;

  @Value("${service3-url:http://localhost:8082/api/journal}")
  String updateJournalUrl;
  
  @Value("${timeout-second:10}")
  Integer timeoutSeconds;
  
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

      // 2, 3
      Boolean isFinished = sagaProcess(dto);

      if (isFinished == false) {
        log.debug("Saga Compensate");
        dto.setCompensate(true);
        this.compensateSave(dto);
        sagaCompensateProcess(dto);
      }
      
    } finally {
      log.debug("Unlock");
      unLock(dto);      
    }
    
  }
  
  private Boolean sagaProcess(MasterDto dto) {    
    Integer threadCount = 2;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch cdt = new CountDownLatch(threadCount);
    
    // 2
    MasterDto dto2 = new MasterDto();
    BeanUtils.copyProperties(dto, dto2);      
    dto2.setSeq(2);      
    executor.submit(() -> {
      String threadName = Thread.currentThread().getName();
      log.debug("Thread name: {}", threadName);
      // 呼叫 
      updateTemp(dto2);      
      cdt.countDown();
       
      return;
    });
    
    // 3
    MasterDto dto3 = new MasterDto();
    BeanUtils.copyProperties(dto, dto3);      
    dto3.setSeq(3);      
    executor.submit(() -> {
      String threadName = Thread.currentThread().getName();
      log.debug("Thread name: {}", threadName);
      // 呼叫 
      updateJournal(dto3);
      // for test
      if (dto3.isCompensate() == false) {
        if (dto3.getTestCase() == 2) {
          log.debug("Test case 2: journal timeout");
          try { Thread.sleep((timeoutSeconds + 3) * 1000); } catch (InterruptedException e) { e.printStackTrace(); }          
        } else if (dto3.getTestCase() == 1) {
          log.debug("Test case 1: journal hold");
          try { Thread.sleep((timeoutSeconds / 2) * 1000); } catch (InterruptedException e) { e.printStackTrace(); }        
        }
      }
      // --
      cdt.countDown();      
      return;
    });
    
    Boolean isFinished = false;
    try {
      isFinished = cdt.await(timeoutSeconds, TimeUnit.SECONDS);
      if (isFinished) {
        log.debug("Saga process finished");    
      } else {
        log.debug("Saga process timeout");  
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
    return isFinished;
  }
  
  private void sagaCompensateProcess(MasterDto dto) {
    this.sagaProcess(dto);    
  }
  
  @SuppressWarnings("unused")
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
  
  private void updateTemp(MasterDto dto) {
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
    log.debug("Temp Req = {}", req);
    HttpEntity<String> request = new HttpEntity<String>(req, headers);
    
    String rep = restTemplate.postForObject(updateTempUrl, request, String.class);
    log.debug("Temp Rep = {}", rep);
    if ("OK".equalsIgnoreCase(rep) == false) {
      throw new IllegalStateException("Update Fail");    
    }
  }
  
  private void updateJournal(MasterDto dto) {
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
    log.debug("Journal Req = {}", req);
    HttpEntity<String> request = new HttpEntity<String>(req, headers);
    
    String rep = restTemplate.postForObject(updateJournalUrl, request, String.class);
    log.debug("Journal Rep = {}", rep);
    if ("OK".equalsIgnoreCase(rep) == false) {
      throw new IllegalStateException("Update Fail");    
    }
  }
  
  @Transactional(value = TxType.REQUIRES_NEW)
  public void checkAndLock(MasterDto dto) {
    Master master = null;
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (!opt.isPresent()) {
      //throw new IllegalStateException("Not Found!"); 
      master = new Master();
      master.setId(dto.getId());
    } else {
      master = opt.get();
    }
        
    if (StringUtils.hasText(master.getHoldMark()) == true &&
        master.getHoldMark().equalsIgnoreCase(dto.getUuid()) == false
        ) {
      throw new IllegalStateException("Lock by Others!");            
    }
    
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
      log.debug("save no need execute");
      return;
    }
    
    Optional<Master> opt = masterRepo.findById(dto.getId());
    if (opt.isPresent() == false) {
      throw new IllegalStateException("Not Found!");      
    }
    
    Master master = opt.get();
    log.debug("Before save master = {}", master.toString());
   
    master.setBalance(master.getBalance().add(BigDecimal.valueOf(Float.valueOf(dto.getBalance()))));
    master.setStatus(dto.getStatus());
    masterRepo.save(master);
    masterRepo.flush();
    
    // log0
    ServiceLog0 log0 = new ServiceLog0();
    log0.setId(dto.getUuid());
    log0.setSeq(dto.getSeq());
    log0.setTs(LocalDateTime.now());
    log0.setStatus(0);
    log0Repo.save(log0);
    log0Repo.flush();
    
  }
  
  @Override
  @Transactional
  public void compensateSave(MasterDto dto) {
    if (needExecute(dto) == false) {
      log.debug("Compensate no need execute");
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
    
    // log0
    ServiceLog0 log0 = getLog0(dto.getUuid(), dto.getSeq()).get();
    log0.setStatus(1);
    log0Repo.save(log0);
    log0Repo.flush();    
  }
  
  @Override
  public List<MasterDto> findAll() {
    return null;
  }

  //------------------------------------------------
  Integer serviceCount = 2;
  CountDownLatch eventCdt = null;
  
  @Override
  public void sendTestMsg(String msg) {
    rabbitTemplate.setChannelTransacted(true);
    MasterDto dto = new MasterDto();
    // only supports String, byte[] and Serializable payloads
    dto.setId(msg);
    rabbitTemplate.convertAndSend("testQueue", dto);    
  }

  @RabbitListener(queues = "orchQueue")  
  public void listenOrchEvent(MasterDto dto) {
    log.debug("Receive Event = {}", dto.toString());
    eventCdt.countDown();
  }
  
  @Override
  @Transactional
  public void sagaUpdateByEvent(MasterDto dto) {
    try {
      // init hold mark
      dto.setHoldMark(dto.getUuid());
      dto.setSeq(0);
      
      checkAndLock(dto);

      // 1
      dto.setSeq(1);
      save(dto);
      
      // 2, 3
      Boolean isFinished = sagaProcessByEvent(dto);

      if (isFinished == false) {
        log.debug("Saga Compensate By Event");
        dto.setCompensate(true);
        this.compensateSave(dto);
        sagaCompensateProcessByEvent(dto);
      }
      
    } finally {
      log.debug("Unlock");
      unLock(dto);      
    }
  }
  
  @SuppressWarnings("unused")
  private void sendSagaEvent(String queueName, MasterDto dto) {
    //rabbitTemplate.setChannelTransacted(true);
    rabbitTemplate.convertAndSend(queueName, dto);    
  }
  
  private void sagaCompensateProcessByEvent(MasterDto dto) {
    this.sagaProcessByEvent(dto);    
  }
  
  public Boolean sagaProcessByEvent(MasterDto dto) {
    ExecutorService executor = Executors.newFixedThreadPool(serviceCount);
    eventCdt = new CountDownLatch(serviceCount);
    
    // 2
    MasterDto dto2 = new MasterDto();
    BeanUtils.copyProperties(dto, dto2);      
    dto2.setSeq(2);      
    //sendSagaEvent("tempQueue", dto2);
    executor.submit(() -> {
      String threadName = Thread.currentThread().getName();
      log.debug("Thread name: {}", threadName);
      // 呼叫 
      Object obj = rabbitTemplate.convertSendAndReceive("tempQueue", dto2);    
      if (obj == null) {
        return;        
      }
      MasterDto rep = (MasterDto) obj; 
      log.debug("Temp {} receive {}", threadName, rep.toString());
      eventCdt.countDown();       
      return;
    });
    
    // 3
    MasterDto dto3 = new MasterDto();
    BeanUtils.copyProperties(dto, dto3);      
    dto3.setSeq(3);      
    //sendSagaEvent("journalQueue", dto3);
    executor.submit(() -> {
      String threadName = Thread.currentThread().getName();
      log.debug("Thread name: {}", threadName);
      // 呼叫 
      Object obj = rabbitTemplate.convertSendAndReceive("journalQueue", dto3); 
      if (obj == null) {
        return;        
      }
      MasterDto rep = (MasterDto) obj;      
      log.debug("Journal {} receive {}", threadName, rep.toString());
      eventCdt.countDown();       
      return;
    });
    
    Boolean isFinished = false;
    try {
      isFinished = eventCdt.await(timeoutSeconds, TimeUnit.SECONDS);
      if (isFinished) {
        log.debug("Saga event process finished");    
      } else {
        log.debug("Saga event process timeout");  
      }      
      
      executor.shutdown();
      log.debug("Executor shutdown");
      executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
      log.debug("Executor termination");
      
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (!executor.isTerminated()) {
        log.error("cancel non-finished tasks");
      }
      executor.shutdownNow();
      log.debug("shutdown finished");
    }
    return isFinished;
  }
  
}
