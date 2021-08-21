package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface AmlChannel {
  
  @RabbitListener(queues = "amlQueue")  
  public Integer screen(String name);
}
