package com.mx.context;

import com.google.common.base.Preconditions;

/**
 * 支持自动继承指定上下文Runnable对象，一般用于将请求内使用异步线程执行任务的场景，例如线程池、异步线程
 * 注意：一个InheritContextVarRunnable对象只能关联一个runnable对象
 */
public class InheritContextVarRunnable implements Runnable {
  private ChainableWithContext withContext;
  private Runnable wrappedRunnable;

  public InheritContextVarRunnable() {
    this(null);
  }

  public InheritContextVarRunnable(Runnable runnable) {
    this.wrappedRunnable = runnable;
  }

  public static InheritContextVarRunnable of(Runnable runnable) {
    return new InheritContextVarRunnable(runnable);
  }

  /**
   * 指定需要继承的上下文变量
   *
   * @param contextVar 上下文变量
   */
  public <T> InheritContextVarRunnable withContext(ContextVar<T> contextVar) {
    if (withContext == null) {
      withContext = new ChainableWithContextImpl<>(contextVar, contextVar.get());
    } else {
      withContext = withContext.withContext(contextVar, contextVar.get());
    }
    return this;
  }

  /**
   * 指定需要在请求上下文内执行的runnable对象
   *
   * @param runnable 需要在请求上下文内执行的runnable对象
   * @return 包装后的对象
   */
  public InheritContextVarRunnable wrap(Runnable runnable) {
    Preconditions.checkState(this.wrappedRunnable == null);
    this.wrappedRunnable = runnable;
    return this;
  }

  @Override
  public void run() {
    withContext.call(this.wrappedRunnable);
  }
}
