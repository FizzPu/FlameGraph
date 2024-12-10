package org.mx.yahaha.request.trace.core;

import org.mx.yahaha.request.trace.core.limit.SampleRateLimiter;
import org.mx.yahaha.request.trace.core.limit.SampleTaskActivator;
import org.mx.yahaha.request.trace.core.stack.ElementMatchedTruncateHandler;
import org.mx.yahaha.request.trace.core.stack.StackTraceTruncateHandler;
import org.mx.yahaha.request.trace.core.storage.BasedLoggerSampleResultStorage;
import org.mx.yahaha.request.trace.core.storage.SampleResultStorage;
import org.mx.yahaha.request.trace.core.task.DefaultSampleTaskPool;
import org.mx.yahaha.request.trace.core.task.SampleTaskPool;
import org.mx.yahaha.request.trace.core.task.SingleThreadTaskSampleExecutor;
import org.mx.yahaha.request.trace.core.task.TaskSampleExecutor;

/**
 * builder
 */
public final class RequestTraceBuilder {
  private final SampleTaskPool taskPool;
  private SampleTaskActivator activator;
  private SampleRateLimiter rateLimiter;
  private final SampleResultStorage sampleResultStorage;
  private final TaskSampleExecutor taskSampleExecutor;
  private final StackTraceTruncateHandler stackTraceTruncateHandler;
  
  public RequestTraceBuilder(String declaringClass, String methodName, String fileName, int lineNumber) {
    this.stackTraceTruncateHandler = new ElementMatchedTruncateHandler(
      new StackTraceElement(declaringClass, methodName, fileName, lineNumber)
    );
    this.taskSampleExecutor = new SingleThreadTaskSampleExecutor();
    this.taskPool = new DefaultSampleTaskPool();
    this.activator = SampleTaskActivator.DEFAULT_ACTIVATOR;
    this.rateLimiter = SampleRateLimiter.DEFAULT_RATE_LIMITER;
    this.sampleResultStorage = new BasedLoggerSampleResultStorage();
  }
  
  public RequestTraceBuilder withSampleTaskActivator(SampleTaskActivator sampleTaskActivator) {
    this.activator = sampleTaskActivator;
    return this;
  }
  
  public RequestTraceBuilder withSampleRateLimiter(SampleRateLimiter sampleRateLimiter) {
    this.rateLimiter = sampleRateLimiter;
    return this;
  }
  
  public StackTraceSampler build() {
    return new DefaultStackTrackSimpler(taskPool, activator, rateLimiter, sampleResultStorage, taskSampleExecutor, stackTraceTruncateHandler);
  }
}
