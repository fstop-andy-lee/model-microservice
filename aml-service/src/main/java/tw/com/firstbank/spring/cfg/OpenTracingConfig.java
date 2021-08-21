package tw.com.firstbank.spring.cfg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jaegertracing.internal.metrics.InMemoryMetricsFactory;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Configuration
public class OpenTracingConfig {
  
  @Value("${spring.application.name}")
  private String applicationName;
  @Value("${opentracing.jaeger.udp-sender.host}")
  private String jaegerHost;
  @Value("${opentracing.jaeger.udp-sender.port}")
  private Integer jaegerPort;
  
  // disable tracing
  @Bean
  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "false", matchIfMissing = true)
  public Tracer noopTracer() {
      return io.opentracing.noop.NoopTracerFactory.create();
  }
  
  @Bean
  @ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "true", matchIfMissing = false)
  public Tracer tracer() {
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

  @PostConstruct
  public void registerToGlobalTracer() {
      if (!GlobalTracer.isRegistered()) {
          GlobalTracer.registerIfAbsent(tracer());
      }
  }
}
