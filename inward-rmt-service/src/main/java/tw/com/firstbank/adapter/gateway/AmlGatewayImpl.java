package tw.com.firstbank.adapter.gateway;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AmlGatewayImpl implements AmlGateway{
  
  private static final String AML_CHANNEL = "amlQueue";
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private RestTemplate restTemplate;
  
  @Value("${aml-url:http://localhost:8090/aml/v1/screen}")
  private String amlUrl;
  
  public Integer screenByEvent(String name) {
    Integer ret = 0;
    
    //呼叫 
    Object obj = rabbitTemplate.convertSendAndReceive(AML_CHANNEL, name); 
    if (obj == null) {
      return ret;        
    }

    ret = (Integer) obj;      
    
    return ret;
  }
  
  public Integer screenByApi(String name) {
    ResponseEntity<Integer> ret = restTemplate.getForEntity(amlUrl + "?name=" + name, Integer.class);
    return ret.getBody();
  }
  
}
