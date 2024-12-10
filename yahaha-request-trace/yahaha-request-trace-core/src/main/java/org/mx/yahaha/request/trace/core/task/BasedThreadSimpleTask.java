package org.mx.yahaha.request.trace.core.task;

import org.mx.yahaha.request.trace.core.context.SampleTaskContext;
import org.mx.yahaha.request.trace.core.stack.SampleResultCollector;
import org.mx.yahaha.request.trace.core.stack.StackTrace;
import org.mx.yahaha.request.trace.core.stack.StackTraceTruncateHandler;
import org.mx.yahaha.request.trace.core.storage.DumpResult;
import org.mx.yahaha.request.trace.core.storage.StackTraceDumpResult;

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

