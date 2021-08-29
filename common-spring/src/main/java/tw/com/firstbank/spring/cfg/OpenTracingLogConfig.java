package tw.com.firstbank.spring.cfg;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.cloud.log.SpanLogsAppender;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;

@Configuration
@AutoConfigureAfter(TracerAutoConfiguration.class)
@ConditionalOnClass(ch.qos.logback.classic.Logger.class)
@ConditionalOnProperty(name = "opentracing.spring.cloud.log.enabled", havingValue = "true", matchIfMissing = true)
//@ConditionalOnProperty(name = "opentracing.jaeger.enabled", havingValue = "true", matchIfMissing = false)
public class OpenTracingLogConfig {
  
  public OpenTracingLogConfig(final Tracer tracer) {
    SpanLogsAppender spanLogsAppender = new SpanLogsAppender(tracer);
    spanLogsAppender.start();
    Logger rootLogger = (Logger)LoggerFactory.getLogger("tw.com.firstbank");
    rootLogger.addAppender(spanLogsAppender);
    System.out.println("<< OpenTracingLogConfig >>");
  }
}
