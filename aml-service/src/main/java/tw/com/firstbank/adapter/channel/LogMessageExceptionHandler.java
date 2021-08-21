package tw.com.firstbank.adapter.channel;

import java.util.Arrays;
import java.util.Map;

import org.springframework.amqp.core.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogMessageExceptionHandler implements MessageExceptionHandler {

    @Override
    public void handle(Message message, Throwable cause) {
          Map<String, Object> headers = message.getMessageProperties().getHeaders();
          log.warn("Dead letter message from queue  , message  , headers  :  cause",
          headers.get("x-original-queue"), getMessageString(message), headers, cause);
    }
    
    protected String getMessageString(Message message) {
        String contentType = message.getMessageProperties() != null?message.getMessageProperties().getContentType():null;
        if("text/plain".equals(contentType) || "application/json".equals(contentType) || 
            "text/x-json".equals(contentType) || "application/xml".equals(contentType)) {
            return new String(message.getBody());
        }
        else {
            return Arrays.toString(message.getBody()) + "(byte[" + message.getBody().length + "])";
        }
    }
}
