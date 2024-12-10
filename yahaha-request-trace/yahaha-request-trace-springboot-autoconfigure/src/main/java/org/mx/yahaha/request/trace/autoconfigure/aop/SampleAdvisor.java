package org.mx.yahaha.request.trace.autoconfigure.aop;

import org.aopalliance.aop.Advice;
import org.mx.yahaha.request.trace.autoconfigure.RequestTraceProperties;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;

/**
 * @author FizzPu
 * @since 2024/12/10 下午7:27
 */
public class SampleAdvisor implements PointcutAdvisor {
  private final RequestTraceProperties requestTraceProperties;
  
  public SampleAdvisor(RequestTraceProperties requestTraceProperties) {
    this.requestTraceProperties = requestTraceProperties;
  }
  
  @Override
  public Pointcut getPointcut() {
    return null;
  }
  
  @Override
  public Advice getAdvice() {
    return null;
  }
  
  @Override
  public boolean isPerInstance() {
    return false;
  }
}
