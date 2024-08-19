package com.mx.sampler;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author FizzPu
 * @since 2024/5/22 下午8:06
 */
public class DefaultSampleTaskPool implements SampleTaskPool {
  
  private final Map<String, TaskNode> allTaskNodes = new ConcurrentHashMap<>(16);
  
  /**
   * 采样队列头节点；加入该队列的采样任务才会执行采样操作；
   */
  private final TaskNode taskQueueHead = new TaskNode(null, null);
  
  /**
   * 注册任务，将任务加入任务池；
   * 注册到任务池中的任务并不会立即执行采样操作，直到将任务加入采样队列中
   *
   * @param task 采样任务
   */
  @Override
  public void registerSampleTask(SampleTask task) {
    this.allTaskNodes.put(task.getTaskId(), new TaskNode(task));
  }
  
  /**
   * 根据任务ID获取采样任务
   *
   * @param taskId 任务ID
   * @return 采样任务
   */
  @Override
  public SampleTask getTaskByTaskId(String taskId) {
    TaskNode taskNode = this.allTaskNodes.get(taskId);
    if (taskNode != null) {
      return taskNode.sampleTask;
    }
    return null;
  }
  
  /**
   * 根据任务ID移除采样任务
   *
   * @param taskId 任务ID
   * @return 被移除的采样任务
   */
  @Override
  public SampleTask removeTask(String taskId) {
    TaskNode taskNode = allTaskNodes.remove(taskId);
    SampleTask task = null;
    if (taskNode != null) {
      taskNode.remove();
      task = taskNode.sampleTask;
    }
    return task;
  }
  
  /**
   * 将任务加入采样任务队列中，加入采样队列之后才会开始执行采样操作；
   *
   * 注意：该方法不支持并发调用，一般仅在采样线程中调用；
   */
  @Override
  public void addToSampleTaskQueue(String taskId) {
    TaskNode taskNode = allTaskNodes.get(taskId);
    Preconditions.checkArgument(taskNode != null);
    Preconditions.checkArgument(taskNode.next == null);
    taskNode.next = taskQueueHead.next;
    taskQueueHead.next = taskNode;
  }
  
  /**
   * 获取采样任务队列中所有的任务
   *
   * 注意：该方法不支持并发调用，一般仅在采样线程中调用；
   *
   * @return 所有待采样的任务
   */
  @Override
  public List<SampleTask> getRunningTaskList() {
    List<SampleTask> sampleTasks = new ArrayList<>();
    TaskNode prev = taskQueueHead;
    TaskNode curr = taskQueueHead.next;
    while (curr != null) {
      if (curr.removed) {
        prev.next = curr.next;
      } else {
        prev = curr;
        sampleTasks.add(curr.sampleTask);
      }
      curr = curr.next;
    }
    return sampleTasks;
  }
 
  @Override
  public int size() {
    return allTaskNodes.size();
  }
  
  @Override
  public Iterator<SampleTask> iterator() {
    return new TaskIterator();
  }
  
  final class TaskIterator implements Iterator<SampleTask> {
    private final Iterator<TaskNode> iterator;
    TaskIterator() {
      this.iterator = allTaskNodes.values().iterator();
    }
    
    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }
    
    @Override
    public SampleTask next() {
      return iterator.next().sampleTask;
    }
  }
  
  private static class TaskNode {
    private final SampleTask sampleTask;
    private TaskNode next;
    private volatile boolean removed;
    
    public TaskNode(SampleTask sampleTask) {
      this(sampleTask, null);
    }
    
    public TaskNode(SampleTask sampleTask, TaskNode next) {
      this.sampleTask = sampleTask;
      this.next = next;
      this.removed = false;
    }
    
    public void remove() {
      this.removed = true;
    }
  }
}
