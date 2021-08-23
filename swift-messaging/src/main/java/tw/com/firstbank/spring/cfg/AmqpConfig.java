package tw.com.firstbank.spring.cfg;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tw.com.firstbank.adapter.channel.ChannelConstants;
import tw.com.firstbank.adapter.channel.LogMessageExceptionHandler;
import tw.com.firstbank.adapter.channel.MessageExceptionHandler;

@Configuration
public class AmqpConfig {
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private ConnectionFactory connectionFactory;
    
  @Bean
  public Queue inwardRmtQueue() {
    return new Queue(ChannelConstants.INWARD_RMT_REQ, false);
  }

  @Bean
  @Order(1)
  public MessageExceptionHandler logMessageExceptionHandler() {
      return new LogMessageExceptionHandler();
  }
  
  @Bean
  public AsyncRabbitTemplate template() {
      SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
      container.setQueueNames(ChannelConstants.INWARD_RMT_REP);
      return new AsyncRabbitTemplate(rabbitTemplate, container);
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
