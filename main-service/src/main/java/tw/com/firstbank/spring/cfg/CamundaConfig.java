package tw.com.firstbank.spring.cfg;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import io.camunda.zeebe.client.api.worker.BackoffSupplier;
import io.camunda.zeebe.client.impl.worker.ExponentialBackoffBuilderImpl;


@Configuration
public class CamundaConfig {

  /**
   * workaround for
   * No visible constructors in class java.util.concurrent.Executors$DelegatedScheduledExecutorService
   * @return
   */
  @Bean()
  public ScheduledExecutorService scheduledExecutorService() {
    return Executors.newScheduledThreadPool(1);    
  }
  

}
