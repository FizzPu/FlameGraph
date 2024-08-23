package com.mx.sampler.task;

import com.mx.sampler.context.SampleTaskContext;
import com.mx.sampler.stack.SampleResultCollector;
import com.mx.sampler.stack.StackTrace;
import com.mx.sampler.stack.StackTraceTruncateHandler;
import com.mx.sampler.storage.DumpResult;
import com.mx.sampler.storage.StackTraceDumpResult;

/**
 * @author FizzPu
 * @since 2024/2/26 17:14
 */
public class BasedThreadSimpleTask extends AbstractSampleTask {
  private final Thread targetThread;
  
  public BasedThreadSimpleTask(SampleTaskContext sampleTaskContext, SampleResultCollector sampleResultCollector,
    long startTimeMillis, Thread targetThread, StackTraceTruncateHandler stackTraceTruncateHandler) {
    super(sampleTaskContext, sampleResultCollector, startTimeMillis, stackTraceTruncateHandler);
    this.targetThread = targetThread;
  }
  
  public Thread getTargetThread() {
    return targetThread;
  }
  
  @Override
  public void doSample() {
    if (this.targetThread == null) {
      return;
    }
    StackTrace stackTrace = new StackTrace(this.targetThread.getStackTrace(), stackTraceTruncateHandler);
    if (TaskState.STARTED.equals(getTaskState())) {
      this.sampleResultCollector.collect(stackTrace);
    }
  }
  
  @Override
  public DumpResult dumpSampleResult() {
    String threadName = "Unknown";
    if (this.targetThread != null) {
      threadName = this.targetThread.getName();
    }
    
    String stackTrace = this.sampleResultCollector.dump();
    return new StackTraceDumpResult(
      getTaskId(),
      stackTrace,
      elapsedTimeMillis(),
      getTaskContext(),
      threadName,
      this.startTimeMillis,
      this.sampleStartTimeMills,
      this.sampleIntervalMillis);
  }
}

