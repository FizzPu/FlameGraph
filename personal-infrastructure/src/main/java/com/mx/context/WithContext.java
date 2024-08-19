package com.mx.context;

import java.util.function.Supplier;

/**
 * 上下文管理器，可以方便的实现{@link Runnable}或{@link Supplier}
 * 在指定的上下文内运行；
 *
 */
public interface WithContext {
  /**
   * 在指定的上下文内调用supplier对象
   */
  <R> R call(Supplier<R> supplier);

  /**
   * 在指定的上下文内调用runnable对象
   */
  void call(Runnable runnable);

  /**
   * 将supplier对象包装成"在指定的上下文内调用supplier对象"
   */
  default <R> Supplier<R> wrap(Supplier<R> supplier) {
    return () -> call(supplier);
  }

  /**
   * 将runnable对象包装成"在指定的上下文内调用runnable对象"
   */
  default Runnable wrap(Runnable runnable) {
    return () -> call(runnable);
  }
}
