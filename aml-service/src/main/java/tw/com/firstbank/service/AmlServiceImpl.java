package tw.com.firstbank.service;

import java.util.List;

//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.adapter.channel.AmlChannel;
import tw.com.firstbank.adapter.repository.BlackListRepository;

@Slf4j
@Service
public class AmlServiceImpl implements AmlService, AmlChannel {
  
  //@Autowired
  //private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private BlackListRepository repo;
  
  public Integer screen(String input) {
    Integer ret = 0;
    String [] names = input.split("\\R| ");
   
    for(String name : names) {
      log.debug("Screen {}", name);
      List<String> list = repo.find(name);
      if (list != null && list.isEmpty() == false) {
        ret = ret + list.size();
      }
    }
    
    return ret;
  }
  


}
