package tw.com.firstbank.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.model.SagaGenericDto;

@Service
@Slf4j
public class CamundaTaskServiceImpl implements CamundaTaskService {

  //@Autowired
  private ZeebeClient zeebeClient;
  
  @Autowired
  private ZeebeClientBuilder zeebeClientBuilder;
  
  //@Autowired
  //private RuntimeService camundaRuntimeService;

  final String pid = "Process_Transaction_Saga_Orchestrator";
  
  final String workerName = "MainOrchestrator";
  
  final Integer timeoutSeconds = 45;
  
  private CountDownLatch cdt = new CountDownLatch(1);
  
  public CamundaTaskServiceImpl() {}
  
  public void startTask(SagaGenericDto<MasterDto> body) {

    zeebeClient = zeebeClientBuilder
        .defaultJobWorkerName(workerName)
        .defaultJobTimeout(Duration.ofSeconds(timeoutSeconds))
        .build();
    
    // 啟動流程
    ProcessInstanceEvent processInstanceEvent = zeebeClient.newCreateInstanceCommand().bpmnProcessId(pid).latestVersion()
        // .requestTimeout(null)
        // .variables(null) //Input Stream, Object, String, Map<String, Object>
        .variables(body)
        .send()
        .join()        
        ;

    log.debug("zeebe client join");
    // And then retrieve the UserTask and complete it with 'approved = true'
    try {
      waitForTaskAndComplete();
      
    } catch (InterruptedException e) {
      log.error("InterruptedException", e);
      e.printStackTrace();
    } catch (TimeoutException e) {
      log.error("TimeoutException", e);
      e.printStackTrace();
    } catch (ExecutionException e) {
      log.error("ExecutionException", e);
      e.printStackTrace();
    }
  }

  public void waitForTaskAndComplete() throws InterruptedException, TimeoutException, ExecutionException {
    
//    List<ActivatedJob> jobs = zeebeClient
//        .newActivateJobsCommand()
//        .jobType("service3")
//        .maxJobsToActivate(10)
//        .workerName(workerName)
//        .send()
//        .join()
//        .getJobs();
//    log.debug("Jobs {}", jobs.isEmpty());

//    ActivateJobsResponse ajs;
//    ajs = zeebeClient
//          .newActivateJobsCommand()
//          .jobType("service3")
//          .maxJobsToActivate(10)
//          .workerName(workerName)
//          .send()
//          .get()
//          ;
//    List<ActivatedJob> jobs = ajs.getJobs();    
//    log.debug("Jobs is empty {}", jobs.isEmpty());
    
    cdt.await(timeoutSeconds, TimeUnit.SECONDS);
    log.debug("cdt await done");
    
  }

  // 做為 service task 來接收
  @ZeebeWorker(type = "service1")
  public void handleCamundaSagaJobService1(final JobClient jobClient, final ActivatedJob job) {
    log.debug("Service1 Job Key = {}", job.getKey());
    jobClient.newCompleteCommand(job.getKey()).variables("{\"service1Result\": 1}").send()
    // .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job,
    // throwable); })
    ;
  }

  @ZeebeWorker(type = "service2")
  public void handleCamundaSagaJobService2(final JobClient jobClient, final ActivatedJob job) {
    log.debug("Service2 Job Key = {}", job.getKey());
    jobClient.newCompleteCommand(job.getKey()).variables("{\"service2Result\": 1}").send()
    // .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job,
    // throwable); })
    ;
  }

  @ZeebeWorker(type = "service3")
  public void handleCamundaSagaJobService3(final JobClient jobClient, final ActivatedJob job) {
    log.debug("Service3 Job Key = {}", job.getKey());
    jobClient.newCompleteCommand(job.getKey()).variables("{\"service3Result\": 1}").send()
    // .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job,
    // throwable); })
    ;
    
    cdt.countDown();
  }

}
