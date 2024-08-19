package com.mx.sampler;

/**
 * 采样任务的启动器；
 * 启动采样任务之前，调用activate方法判断是否激活该采样任务；
 *
 * @author xiejian
 * @version 1.0.0
 * @since 2022/11/21 16:35
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
