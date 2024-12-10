package org.mx.yahaha.request.trace.core.task;

import org.mx.yahaha.request.trace.core.context.SampleTaskContext;
import org.mx.yahaha.request.trace.core.storage.DumpResult;

/**
 * @author FizzPu
 * @since 2024/2/26 17:13
 */
public interface SampleTask {
  boolean start();
  
  String getTaskId();
  
  TaskState getTaskState();
  
  SampleTaskContext getTaskContext();
  
  void doSample();
  
  long elapsedTimeMillis();
  
  DumpResult dumpSampleResult();
  
  void stop();
}

