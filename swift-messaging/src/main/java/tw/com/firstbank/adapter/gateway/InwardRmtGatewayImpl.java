package tw.com.firstbank.adapter.gateway;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.adapter.channel.ChannelConstants;
import tw.com.firstbank.model.InwardRmtDto;
import tw.com.firstbank.service.RepositoryHelper;

@Slf4j
@Service
public class InwardRmtGatewayImpl implements InwardRmtGateway, ListenableFutureCallback<InwardRmtDto> {

  private static final String CHANNEL_NAME = ChannelConstants.INWARD_RMT_REQ;
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private AsyncRabbitTemplate asyncRabbitTemplate;
  
  @Autowired
  private RepositoryHelper repoHelper;
  
  @Value("${inward-rmt-url:http://localhost:8070/inward-rmt/v1/}")
  private String url;
  
  @Override
  public InwardRmtDto processInwardRmtByEvent(InwardRmtDto dto) {
    InwardRmtDto ret = null;
    
    //呼叫 
    Object obj = rabbitTemplate.convertSendAndReceive(CHANNEL_NAME, dto); 
    if (obj == null) {
      return ret;        
    }

    ret = (InwardRmtDto) obj;      
    
    return ret;
  }
  
  public void processInwardRmtByAsyncEvent(InwardRmtDto dto) {
    AsyncRabbitTemplate.RabbitConverterFuture<InwardRmtDto> future =
        asyncRabbitTemplate.convertSendAndReceive(ChannelConstants.INWARD_RMT_REQ, dto);
    future.addCallback(this);
  }
  
  @Override
  public InwardRmtDto processInwardRmtByApi(InwardRmtDto dto) {
    // TBD
    return null;
  }

  @Override
  public void onSuccess(InwardRmtDto result) {
    log.debug("Result = {}", result.getReplyStatus());
    if (result.getReplyStatus() > 0) {      
      repoHelper.markDone(result.getId());
    }
  }

  @Override
  public void onFailure(Throwable ex) {
    log.error(ex.getMessage());    
  }

}
