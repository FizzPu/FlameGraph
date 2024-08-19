package com.mx.sampler;

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
    StackTrace stackTrace = StackTrace.fromThread(this.targetThread);
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

