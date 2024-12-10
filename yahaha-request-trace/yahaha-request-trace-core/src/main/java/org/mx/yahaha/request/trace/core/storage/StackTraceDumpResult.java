package org.mx.yahaha.request.trace.core.storage;

import org.mx.yahaha.request.trace.core.JsonUtils;
import org.mx.yahaha.request.trace.core.context.SampleTaskContext;

/**
 *
 * @author FizzPu
 * @since 2024/5/20 下午7:30
 */
public class StackTraceDumpResult implements DumpResult {
  private final String taskId;
  private final String stackTrace;
  private final long taskExecutionTime;
  private final SampleTaskContext taskContext;
  private final String threadName;
  private final long startTimeMills;
  private final long sampleStartTimeMills;
  private final int sampleIntervalInMills;
  
  
  public StackTraceDumpResult(
    String taskId,
    String stackTrace,
    long taskExecutionTime,
    SampleTaskContext taskContext,
    String threadName,
    long startTimeMills,
    long sampleStartTimeMills,
    int sampleIntervalInMills) {
    this.taskId = taskId;
    this.stackTrace = stackTrace;
    this.taskExecutionTime = taskExecutionTime;
    this.taskContext = taskContext;
    this.threadName = threadName;
    this.startTimeMills = startTimeMills;
    this.sampleStartTimeMills = sampleStartTimeMills;
    this.sampleIntervalInMills = sampleIntervalInMills;
  }
  
  public String getTaskId() {
    return taskId;
  }
  
  public String getStackTrace() {
    return stackTrace;
  }
  
  public long getTaskExecutionTime() {
    return taskExecutionTime;
  }
  
  public SampleTaskContext getTaskContext() {
    return taskContext;
  }
  
  public String getThreadName() {
    return threadName;
  }
  
  public long getStartTimeMills() {
    return startTimeMills;
  }
  
  public long getSampleStartTimeMills() {
    return sampleStartTimeMills;
  }
  
  public int getSampleIntervalInMills() {
    return sampleIntervalInMills;
  }
  
  @Override
  public String toString() {
    return "SampleResult{" +
      "taskId='" + taskId + '\'' +
      ", stackTrace='" + stackTrace + '\'' +
      ", taskExecutionTime=" + taskExecutionTime +
      ", taskContext=" + JsonUtils.toJson(taskContext) +
      ", threadName='" + threadName + '\'' +
      '}';
  }
}
