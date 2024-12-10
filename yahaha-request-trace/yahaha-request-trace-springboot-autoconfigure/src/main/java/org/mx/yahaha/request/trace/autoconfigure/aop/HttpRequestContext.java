package org.mx.yahaha.request.trace.autoconfigure.aop;

import org.mx.yahaha.request.trace.core.context.SampleTaskContext;

import java.util.Map;

/**
 * @author FizzPu
 * @since 2024/11/30 下午5:57
 */
public class HttpRequestContext implements SampleTaskContext {
  private final String url;
  private final String path;
  private final String method;
  private final String requestUuid;
  private Integer statusCode;
  
  public HttpRequestContext(String url, String path, String method, String requestUuid) {
    this.url = url;
    this.path = path;
    this.method = method;
    this.requestUuid = requestUuid;
  }
  
  private Map<String, Object> extra;
  
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }
  
  public void addExtraInfo(String key, Object value) {
  
  }
}
