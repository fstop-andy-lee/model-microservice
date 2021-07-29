package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import tw.com.firstbank.model.MasterDto;

public interface TestChannel {
  
  @RabbitListener(queues = "testQueue")  
  //public void listen(Message msg);
  public void listen(MasterDto dto);
}
