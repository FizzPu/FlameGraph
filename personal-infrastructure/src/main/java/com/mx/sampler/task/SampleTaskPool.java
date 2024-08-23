package com.mx.sampler.task;

import java.util.List;

/**
 * @author FizzPu
 * @since 2024/8/12 下午5:39
 */
public interface SampleTaskPool extends Iterable<SampleTask> {
  int size();
  
  void addToSampleTaskQueue(String taskId);
  
  List<SampleTask> getRunningTaskList();
  
  SampleTask getTaskByTaskId(String taskId);
  
  void registerSampleTask(SampleTask task);
  
  SampleTask removeTask(String taskId);
}

