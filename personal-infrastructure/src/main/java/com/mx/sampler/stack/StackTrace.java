package com.mx.sampler.stack;

import java.util.Arrays;

/**
 * @author FizzPu
 * @since 2024/8/18 下午6:10
 */
public class StackTrace implements OnceSampleResult {
  private final StackTraceElement[] elements;
  private final StackTraceTruncateHandler stackTraceTruncateHandler;
  
  public StackTrace(StackTraceElement[] elements, StackTraceTruncateHandler stackTraceTruncateHandler) {
    this.elements = elements;
    this.stackTraceTruncateHandler = stackTraceTruncateHandler;
  }
  
  /**
   * stack trace format:
   * {className}:{methodName}({fileName}:{lineNumber});
   * example:
   * com.demo:func1(Test.java:1);
   */
  public String dump() {
    if (elements.length == 0) {
      return "unknown";
    }
    
    int truncateFromIndex = stackTraceTruncateHandler.truncateFromIndex(elements);
    
    StringBuilder output = new StringBuilder();
    for (int i = truncateFromIndex; i >= 0; i--) {
      StackTraceElement element = elements[i];
      
      String classId;
      classId = element.getClassName();
      
      output.append(classId);
      output.append("(");
      output.append(element.getMethodName());
      output.append(":");
      output.append(element.getLineNumber());
      output.append(");");
    }
    return output.toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StackTrace that = (StackTrace) o;
    return Arrays.equals(elements, that.elements);
  }
  
  @Override
  public int hashCode() {
    return Arrays.hashCode(elements);
  }
}
