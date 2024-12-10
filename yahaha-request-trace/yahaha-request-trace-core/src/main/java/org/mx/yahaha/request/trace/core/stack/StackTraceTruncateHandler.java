package org.mx.yahaha.request.trace.core.stack;

/**
 * @author FizzPu
 * @since 2024/8/19 下午4:02
 */
public interface StackTraceTruncateHandler {
  /**
   * 计算裁剪的索引值，大于索引值的部分都需要被裁剪掉
   *
   * @param elements stack trace elements
   * @return 索引值
   */
  int truncateFromIndex(StackTraceElement[] elements);
}
