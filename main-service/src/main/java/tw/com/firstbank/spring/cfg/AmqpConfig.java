package tw.com.firstbank.spring.cfg;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
  
  @Bean
  public Queue orchQueue() {
    return new Queue("orchQueue", false);
  }

  @Bean
  public Queue masterQueue() {
    return new Queue("masterQueue", false);
  }

  @Bean
  public Queue tempQueue() {
    return new Queue("tempQueue", false);
  }

  @Bean
  public Queue journalQueue() {
    return new Queue("journalQueue", false);
  }

}
