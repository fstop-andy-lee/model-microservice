package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import tw.com.firstbank.model.InwardRmtDto;

public interface InwardRmtChannel {
  @RabbitListener(queues = "inwardRmtQueue")  
  public Integer processInwardRmt(InwardRmtDto dto);
}
