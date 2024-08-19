package com.mx.sampler;

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

