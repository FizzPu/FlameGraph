package com.mx.sampler;

import java.util.Objects;

/**
 * @author FizzPu
 * @since 2024/8/19 下午4:03
 */
public class ElementMatchedTruncateHandler implements StackTraceTruncateHandler {
  private final StackTraceElement targetElement;
  private volatile int cachedIndex = -1;
  
  public ElementMatchedTruncateHandler(StackTraceElement targetElement) {
    this.targetElement = targetElement;
  }
  
  @Override
  public int truncateFromIndex(StackTraceElement[] elements) {
    if (cachedIndex >= 0 && elements.length > cachedIndex && isMatched(elements[cachedIndex])) {
      return cachedIndex;
    }
    
    cachedIndex = -1;
    for (int i = elements.length - 1; i >= 0; i--) {
      if (isMatched(elements[i])) {
        cachedIndex = i;
        break;
      }
    }
    
    if (cachedIndex == -1) {
      cachedIndex = elements.length - 1;
    }
    return cachedIndex;
  }
  
  private boolean isMatched(StackTraceElement element) {
    return Objects.equals(element.getMethodName(), targetElement.getMethodName())
      && Objects.equals(element.getClassName(), targetElement.getClassName());
  }
}
