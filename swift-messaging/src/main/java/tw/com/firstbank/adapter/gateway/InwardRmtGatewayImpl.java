package tw.com.firstbank.adapter.gateway;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tw.com.firstbank.model.InwardRmtDto;

@Service
public class InwardRmtGatewayImpl implements InwardRmtGateway {

  private static final String CHANNEL_NAME = "inwardRmtQueue";
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Value("${inward-rmt-url:http://localhost:8070/inward-rmt/v1/}")
  private String url;
  
  @Override
  public Integer processInwardRmtByEvent(InwardRmtDto dto) {
    Integer ret = 0;
    
    //呼叫 
    Object obj = rabbitTemplate.convertSendAndReceive(CHANNEL_NAME, dto); 
    if (obj == null) {
      return ret;        
    }

    ret = (Integer) obj;      
    
    return ret;
  }

  @Override
  public Integer processInwardRmtByApi(InwardRmtDto dto) {
    
    return null;
  }

}
