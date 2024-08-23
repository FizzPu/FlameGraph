package com.mx.sampler.task;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;

/**
 * @author FizzPu
 * @since 2024/8/19 上午11:10
 */
public class SingleThreadTaskSampleExecutor implements TaskSampleExecutor {
  private final ThreadFactory threadFactory = new DemonThreadFactory();
  
  @Override
  public void execute(@Nonnull Runnable command) {
    Thread thread = threadFactory.newThread(command);
    thread.start();
  }
  
  @Override
  public void close() {
    // 等待command 采样完成
  
  }
}
