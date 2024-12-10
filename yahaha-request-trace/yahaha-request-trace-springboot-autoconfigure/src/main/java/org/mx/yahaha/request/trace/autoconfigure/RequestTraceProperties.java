package org.mx.yahaha.request.trace.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author FizzPu
 * @since 2024/11/30 下午3:57
 */
@ConfigurationProperties(prefix = "yahaha.request.trace")
public class RequestTraceProperties {
  private String requestTraceJointPointSPEL = "";
  
  private final String requestEntranceFileName = "DispatcherServlet.java";
  
  private final String requestEntranceDeclareClass = "org.springframework.web.servlet.DispatcherServlet";
  
  private final String requestEntranceMethodName = "doDispatch";
  
  private final int requestEntranceLineNumber = 1038;
  
  private final String stackTraceDataDir = "/tmp/stack-trace";
  
  public String getRequestEntranceFileName() {
    return requestEntranceFileName;
  }
  
  public String getRequestEntranceDeclareClass() {
    return requestEntranceDeclareClass;
  }
  
  public String getRequestEntranceMethodName() {
    return requestEntranceMethodName;
  }
  
  public int getRequestEntranceLineNumber() {
    return requestEntranceLineNumber;
  }
  
  public String getStackTraceDataDir() {
    return stackTraceDataDir;
  }
  
  
}
