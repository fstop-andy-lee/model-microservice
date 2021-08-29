package tw.com.firstbank.spring.log.appender;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import io.jaegertracing.internal.metrics.InMemoryMetricsFactory;
import io.jaegertracing.internal.samplers.ConstSampler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

@Component
@ConditionalOnClass(ch.qos.logback.classic.Logger.class)
@ConditionalOnProperty(name = "opentracing.jaeger.enabled", havingValue = "true", matchIfMissing = false)
public class OpenTracingLogAppender extends AppenderBase<Object> {
  
  private Tracer tracer = null; 
  
  private String applicationName;
  
  private String jaegerHost;
  
  private Integer jaegerPort;
  
  @Override
  protected void append(Object eventObject) {
    LoggingEvent event = (LoggingEvent) eventObject; 
    final String loggerName = event.getLoggerName();
    if (tracer == null) {     
      System.out.println("Open Tracing = " + applicationName + " " + jaegerHost + ":" + jaegerPort);
      tracer = getTracer();
      if (tracer == null) {
        System.out.println("Null open tracer");                
      }     
      return;
    }
    System.out.println("LEVEL=" + event.getLevel());
    // ERROR only
    if (Level.ERROR.equals(event.getLevel())) {       
      System.out.println("LEVEL>>=" + event.getLevel());
      error(event.getLevel().toString()
          , loggerName
          , event.getFormattedMessage()
          );
    }
   
  }
  
  private Span getSpan(String loggerName) {
    //return tracer.scopeManager().activeSpan();
    Span sp = tracer.scopeManager().activeSpan();
    if (sp == null) {
      sp = startSpan(loggerName);
    }
    return sp;
  }
  
  private void error(String level, String loggerName, String message) {
    System.out.println("error");
    Span sp = getSpan(loggerName); 
    
    // set error tag
    Tags.ERROR.set(sp, true);
    sp.setTag("ERROR", true);
    
    // output
    Map<String, Object> fields = immutableMap();
    fields.put(Fields.EVENT, level);
    //fields.put(Fields.ERROR_OBJECT, e);
    fields.put(Fields.MESSAGE, message);
    sp.log(fields);
    sp.log(message);
    System.out.println("error finish");
    sp.finish();
  }
  
  private <K, V> Map<K, V> immutableMap() {
    Map<K, V> fields = new HashMap<>();
    return Collections.unmodifiableMap(fields);
  }

  public Tracer getTracer() {
    
    return io.jaegertracing.Configuration.fromEnv(applicationName)
    .withSampler(
            io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                    .withType(ConstSampler.TYPE)
                    .withParam(1))
    .withReporter(
            io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                    .withLogSpans(true)
                    .withFlushInterval(1000)
                    .withMaxQueueSize(10000)
                    .withSender(
                            io.jaegertracing.Configuration.SenderConfiguration.fromEnv()
                                    .withAgentHost(jaegerHost)
                                    .withAgentPort(jaegerPort)
                    ))
    .withMetricsFactory(new InMemoryMetricsFactory())
    .getTracer();
  }
  
  public Span startSpan(String spanName) {
    System.out.println("startSpan");

    Span span = tracer.buildSpan(spanName)
        //.asChildOf("")
        //.withStartTimestamp(Instant.now().toEpochMilli() * 1000)  // microsecond
        //.ignoreActiveSpan()
        .asChildOf(GlobalTracer.get().activeSpan())
        .start()        
        ;
    return span;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getJaegerHost() {
    return jaegerHost;
  }

  public void setJaegerHost(String jaegerHost) {
    this.jaegerHost = jaegerHost;
  }

  public Integer getJaegerPort() {
    return jaegerPort;
  }

  public void setJaegerPort(Integer jaegerPort) {
    this.jaegerPort = jaegerPort;
  }

  
}
