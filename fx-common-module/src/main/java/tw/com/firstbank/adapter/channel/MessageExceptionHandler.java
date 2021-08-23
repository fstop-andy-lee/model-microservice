package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.core.Message;

public interface MessageExceptionHandler {
  void handle(Message message, Throwable cause);
}
