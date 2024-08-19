package com.mx.context;

/**
 * 支持链式调用的上下文管理器，用法如下：
 * <pre>
 * {@code
 *    chainableWithContext
 *      .withContext(var1, value1)
 *      .withContext(var2, value2)
 *      .withContext(var3, value3)
 * }
 * </pre>
 * 上述代码的变量设置顺序为var1 -> var2 -> var3，变量的移除顺序为var3 -> var2 -> var1
 */
public interface ChainableWithContext extends WithContext {
  <T> ChainableWithContext withContext(ContextVar<T> var, T value);
}
