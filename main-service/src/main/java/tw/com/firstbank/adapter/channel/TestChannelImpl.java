package tw.com.firstbank.adapter.channel;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tw.com.firstbank.model.MasterDto;


@Component
@Slf4j
public class TestChannelImpl implements TestChannel {

  @Override
  //public void listen(Message msg) {
  public void listen(MasterDto msg) {  
    log.debug("Message read from Queue : " + msg.toString());
  }

  @SuppressWarnings("unused")
  private String getMsgContentType(Message msg) {
    String contentType = msg.getMessageProperties() != null ? msg.getMessageProperties().getContentType() : null;
    return contentType;
  }
  
}
