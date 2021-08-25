package tw.com.firstbank.adapter.gateway;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  
  @Value("${inward-rmt-url:http://localhost:8070/inward-rmt/v1/send}")
  private String url;
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  
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
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    String req = null;
    try {
      // convert for api call instAmt == null issue
      dto.setApiInstAmt(dto.getInstAmt());
      req = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Convert json error ", e);
      throw new IllegalStateException("Convert Fail"); 
    }
    log.debug("Req = {}", req);
    HttpEntity<String> request = new HttpEntity<String>(req, headers);
    
    InwardRmtDto rep = restTemplate.postForObject(url, request, InwardRmtDto.class);
    log.debug("Rep = {}", rep);
    
    return rep;
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
