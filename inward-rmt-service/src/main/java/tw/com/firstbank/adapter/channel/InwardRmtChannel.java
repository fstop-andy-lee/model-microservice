package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import tw.com.firstbank.model.InwardRmtDto;

public interface InwardRmtChannel {
  //@RabbitListener(queues = "inwardRmtQueue")    
  @RabbitListener(queues = ChannelConstants.INWARD_RMT_REQ)  
  public InwardRmtDto processInwardRmt(InwardRmtDto dto);
}
