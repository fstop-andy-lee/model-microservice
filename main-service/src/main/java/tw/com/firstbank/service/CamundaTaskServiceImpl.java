package tw.com.firstbank.service;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.model.MasterDto;
import tw.com.firstbank.model.SagaGenericDto;

@Service
@Slf4j
public class CamundaTaskServiceImpl implements CamundaTaskService {

  @Autowired
  private ZeebeClient zeebeClient;

  final String pid = "Process_Transaction_Saga_Orchestrator";
  
  public CamundaTaskServiceImpl() {}
  
  public void startTask(SagaGenericDto<MasterDto> body) {

    // 啟動流程
    zeebeClient.newCreateInstanceCommand().bpmnProcessId(pid).latestVersion()
        // .requestTimeout(null)
        // .variables(null) //Input Stream, Object, String, Map<String, Object>
        .variables(body).send().join();

    // And then retrieve the UserTask and complete it with 'approved = true'
    try {
      waitForTaskAndComplete();
    } catch (InterruptedException e) {
      log.error("InterruptedException", e);
      e.printStackTrace();
    } catch (TimeoutException e) {
      log.error("TimeoutException", e);
      e.printStackTrace();
    }
  }

  public void waitForTaskAndComplete() throws InterruptedException, TimeoutException {
    final String workerName = "Main-Orchestrator";

    List<ActivatedJob> jobs = zeebeClient.newActivateJobsCommand().jobType("service1")
        .maxJobsToActivate(1).workerName(workerName).send().join().getJobs();

    log.debug("Jobs {}", jobs.isEmpty());

  }

  // 做為 service task 來接收
  @ZeebeWorker(type = "service1")
  public void handleCamundaSagaJob(final JobClient jobClient, final ActivatedJob job) {
    log.debug("Job Key = {}", job.getKey());
    jobClient.newCompleteCommand(job.getKey()).variables("{\"fooResult\": 1}").send()
    // .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job,
    // throwable); })
    ;
  }

}
