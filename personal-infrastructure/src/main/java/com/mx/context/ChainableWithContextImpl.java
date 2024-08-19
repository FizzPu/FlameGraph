package com.mx.context;

import java.util.function.Supplier;

/**
 * 支持链式调用的上下文管理器
 */
public class ChainableWithContextImpl<V> extends WithContextImpl<V> implements ChainableWithContext {
  private final WithContext upstream;

  public ChainableWithContextImpl(ContextVar<V> var, V value) {
    this(null, var, value);
  }

  public ChainableWithContextImpl(WithContext upstream, ContextVar<V> var, V value) {
    super(var, value);
    this.upstream = upstream;
  }

  @Override
  public <T> ChainableWithContext withContext(ContextVar<T> var, T value) {
    return new ChainableWithContextImpl<>(this, var, value);
  }

  @Override
  public <R> R call(Supplier<R> supplier) {
    if (upstream == null) {
      return super.call(supplier);
    }
    return upstream.call(() -> super.call(supplier));
  }
}
