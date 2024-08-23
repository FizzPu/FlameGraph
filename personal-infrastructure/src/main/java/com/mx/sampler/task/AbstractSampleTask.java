package com.mx.sampler.task;

import com.mx.sampler.context.SampleTaskContext;
import com.mx.sampler.stack.SampleResultCollector;
import com.mx.sampler.stack.StackTraceTruncateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author FizzPu
 * @since 2024/5/23 上午10:57
 */
public abstract class AbstractSampleTask implements SampleTask {
  private static final Logger log = LoggerFactory.getLogger(AbstractSampleTask.class);
  
  protected String taskId;
  
  protected long endTimeMillis;
  protected long startTimeMillis;
  protected int sampleIntervalMillis;
  protected long sampleStartTimeMills;
  
  private final AtomicReference<TaskState> taskStateAtomicReference = new AtomicReference<>(TaskState.PENDING);
  
  protected SampleTaskContext sampleTaskContext;
  protected final SampleResultCollector sampleResultCollector;
  protected final StackTraceTruncateHandler stackTraceTruncateHandler;
  
  public AbstractSampleTask(SampleTaskContext sampleTaskContext, SampleResultCollector sampleResultCollector,
    long startTimeMillis, StackTraceTruncateHandler stackTraceTruncateHandler) {
    this.taskId = initTaskId();
    this.startTimeMillis = startTimeMillis;
    this.sampleTaskContext = sampleTaskContext;
    this.sampleResultCollector = sampleResultCollector;
    this.stackTraceTruncateHandler = stackTraceTruncateHandler;
  }
  
  protected String initTaskId() {
    return UUID.randomUUID().toString();
  }
  
  @Override
  public String getTaskId() {
    return taskId;
  }
  
  @Override
  public TaskState getTaskState() {
    return taskStateAtomicReference.get();
  }
  
  @Override
  public boolean start() {
    if (this.taskStateAtomicReference.compareAndSet(TaskState.PENDING, TaskState.STARTING)) {
      // 变量延迟初始化，因此使用前需要先确保task处于start状态
      sampleStartTimeMills = System.currentTimeMillis();
      this.taskStateAtomicReference.set(TaskState.STARTED);
      return true;
    } else {
      log.info("Sample task has already started. [taskId={}, context={}]", this.taskId, this.sampleTaskContext);
    }
    return false;
  }
  
  @Override
  public SampleTaskContext getTaskContext() {
    return this.sampleTaskContext;
  }
  
  @Override
  public long elapsedTimeMillis() {
    if (TaskState.STOPPED.equals(this.taskStateAtomicReference.get()) && endTimeMillis > 0) {
      return endTimeMillis - startTimeMillis;
    }
    return System.currentTimeMillis() - startTimeMillis;
  }
  
  @Override
  public void stop() {
    if (this.taskStateAtomicReference.compareAndSet(TaskState.STARTED, TaskState.STOPPED)) {
      endTimeMillis = System.currentTimeMillis();
    } else {
      this.taskStateAtomicReference.set(TaskState.DISCARD);
    }
  }
}
