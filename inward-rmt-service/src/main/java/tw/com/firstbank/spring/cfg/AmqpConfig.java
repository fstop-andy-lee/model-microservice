package tw.com.firstbank.spring.cfg;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tw.com.firstbank.adapter.channel.ChannelConstants;
import tw.com.firstbank.adapter.channel.LogMessageExceptionHandler;
import tw.com.firstbank.adapter.channel.MessageExceptionHandler;

@Configuration
public class AmqpConfig {
    
  @Bean
  public Queue inwardRmtReqQueue() {
    return new Queue(ChannelConstants.INWARD_RMT_REQ, false);
  }
  
  @Bean
  @Order(1)
  public MessageExceptionHandler logMessageExceptionHandler() {
      return new LogMessageExceptionHandler();
  }
  
  /*
  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
      final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
      rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
      return rabbitTemplate;
  }
  
  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
      return new Jackson2JsonMessageConverter();
  }
  */
}
