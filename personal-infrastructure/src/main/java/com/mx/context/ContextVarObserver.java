package com.mx.context;

public interface ContextVarObserver<T> {
  /**
   * 观察context var的设置
   *
   * @param oldValue 旧值
   * @param newValue 新值
   */
  default void observeContextVarSet(T oldValue, T newValue) {}

  /**
   * 观察context var的移除
   *
   * @param oldValue 旧值
   */
  default void observeContextVarRemove(T oldValue) {}
}
