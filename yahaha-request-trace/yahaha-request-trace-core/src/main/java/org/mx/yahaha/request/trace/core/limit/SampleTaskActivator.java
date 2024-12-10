package org.mx.yahaha.request.trace.core.limit;

import org.mx.yahaha.request.trace.core.task.SampleTask;

/**
 * 采样任务的启动器；
 * 启动采样任务之前，调用activate方法判断是否激活该采样任务；
 *
 * @author FizzPu
 * @since 2024/2/28 10:50
 */
public interface SampleTaskActivator {
  SampleTaskActivator DEFAULT_ACTIVATOR = task -> true;

  /**
   * 是否允许激活采样任务；
   *
   * @param task 采样任务
   * @return true: 激活采样任务
   */
  boolean activate(SampleTask task);
}
