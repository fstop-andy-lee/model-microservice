package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface OrchChannel {
  
  @RabbitListener(queues = "orchQueue")  
  public void listen(String in);
}
