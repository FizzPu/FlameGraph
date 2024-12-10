package org.mx.yahaha.request.trace.autoconfigure;

import org.mx.yahaha.request.trace.autoconfigure.aop.SampleAdvisor;
import org.mx.yahaha.request.trace.core.RequestTraceBuilder;
import org.mx.yahaha.request.trace.core.StackTraceSampler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;

/**
 * @author FizzPu
 * @since 2024/11/30 下午3:33
 */
@Configuration
@ConditionalOnClass(RequestTraceBuilder.class)
@EnableConfigurationProperties(RequestTraceProperties.class)
public class RequestTraceAutoConfig implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(RequestTraceAutoConfig.class);
  
  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("The requestTraceAutoConfig is ready");
  }
  
  @ConditionalOnMissingBean
  @Bean
  public StackTraceSampler configStackTraceSampler(RequestTraceProperties requestTraceProperties) {
    return new RequestTraceBuilder(
      requestTraceProperties.getRequestEntranceDeclareClass(),
      requestTraceProperties.getRequestEntranceMethodName(),
      requestTraceProperties.getRequestEntranceFileName(),
      requestTraceProperties.getRequestEntranceLineNumber())
      .build();
  }
  
  @Bean
  public Advisor configAdvisor(RequestTraceProperties requestTraceProperties) {
    return new SampleAdvisor(requestTraceProperties);
  }
}
