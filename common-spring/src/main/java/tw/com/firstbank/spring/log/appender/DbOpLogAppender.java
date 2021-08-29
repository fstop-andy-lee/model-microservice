package tw.com.firstbank.spring.log.appender;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class DbOpLogAppender extends AppenderBase<Object> {

  public static final String LOGGER_NAME = "db_op_logger";
  
  @Override
  protected void append(Object eventObject) {
    LoggingEvent event = (LoggingEvent) eventObject;
    final String loggerName = event.getLoggerName();
    if (!(LOGGER_NAME.equals(loggerName))) {
      return;
    }
    
    Map<String, String> fields = new HashMap<String, String>();
    fields.put("level", event.getLevel().toString());
    fields.put("logger", loggerName);
    fields.put("content", event.getFormattedMessage());
    
  }

}
