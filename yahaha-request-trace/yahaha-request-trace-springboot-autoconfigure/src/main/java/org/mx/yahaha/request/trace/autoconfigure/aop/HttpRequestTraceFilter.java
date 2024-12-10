package org.mx.yahaha.request.trace.autoconfigure.aop;

import org.mx.yahaha.request.trace.core.StackTraceSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author FizzPu
 * @since 2024/11/30 下午5:51
 */
public class HttpRequestTraceFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(HttpRequestTraceFilter.class);
  private final StackTraceSampler sampler;
  
  public HttpRequestTraceFilter(StackTraceSampler sampler) {
    this.sampler = sampler;
  }
  
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    
    String taskId = null;
    HttpRequestContext taskContext = null;
    try {
      taskContext = buildHttpRequestTaskContext((HttpServletRequest) servletRequest);
      taskId = sampler.startSample(Thread.currentThread(), taskContext);
    } catch (Throwable throwable) {
      log.warn("unexpected error when starting a sampler task and discard this task", throwable);
    }
    
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      if (taskContext != null) {
        setHttpRequestContext(taskContext, (HttpServletResponse) servletResponse);
      }
      if (taskId != null) {
        try {
          sampler.stopSample(taskId);
        } catch (Throwable throwable) {
          log.warn("unexpected error when closing a sampler task and discard this task", throwable);
        }
      }
    }
  }
  
  private HttpRequestContext buildHttpRequestTaskContext(HttpServletRequest servletRequest) {
    String url = servletRequest.getRequestURI();
    String path = servletRequest.getServletPath();
    String method = servletRequest.getMethod();
    return new HttpRequestContext(url, path, method, UUID.randomUUID().toString());
  }
  
  private void setHttpRequestContext(HttpRequestContext requestContext, HttpServletResponse httpServletResponse) {
    requestContext.setStatusCode(httpServletResponse.getStatus());
  }
}
