package com.mx.context;

public interface ContextVar<T> {
  /**
   * 获取变量名
   */
  String name();

  /**
   * 获取当前请求上下文的变量值
   */
  T get();

  /**
   * 设置变量值到当前请求上下文
   */
  void set(T value);

  /**
   * 从当前请求上下文中移除变量值
   */
  void remove();

  /**
   * 注册观察者
   *
   * @param observer context var观察者
   */
  void registerObserver(ContextVarObserver<T> observer);

  /**
   * 移除观察者
   *
   * @param observer context var观察者
   */
  void removeObserver(ContextVarObserver<T> observer);

  /**
   * 移除所有观察者
   */
  void removeAllObservers();
}
