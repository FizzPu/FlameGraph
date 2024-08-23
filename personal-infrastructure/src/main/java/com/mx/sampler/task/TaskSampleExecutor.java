package com.mx.sampler.task;

import java.util.concurrent.Executor;

/**
 * @author FizzPu
 * @since 2024/8/18 下午10:20
 */
public interface TaskSampleExecutor extends Executor {
  void close();
}
