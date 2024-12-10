package org.mx.yahaha.request.trace.core;

import org.mx.yahaha.request.trace.core.context.SampleTaskContext;
import org.mx.yahaha.request.trace.core.limit.SampleRateLimiter;
import org.mx.yahaha.request.trace.core.limit.SampleTaskActivator;
import org.mx.yahaha.request.trace.core.stack.StackTraceCollector;
import org.mx.yahaha.request.trace.core.stack.StackTraceTruncateHandler;
import org.mx.yahaha.request.trace.core.storage.DumpResult;
import org.mx.yahaha.request.trace.core.storage.SampleResultStorage;
import org.mx.yahaha.request.trace.core.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author FizzPu
 * @since 2024/2/26 15:39
 */

public final class DefaultStackTrackSimpler implements StackTraceSampler {
  private static final Logger logger = LoggerFactory.getLogger(DefaultStackTrackSimpler.class);
  
  // 采样任务池最多同时支持的任务数
  private static final int MAX_SAMPLE_CONCURRENCY = 2000;
  private static final int SAMPLE_INTERVAL_IN_MILLIS = 20;
  private static final long SAMPLE_TASK_MAX_DURATION_IN_MILLIS = TimeUnit.SECONDS.toMillis(30);
  
  // 各个线程关联的采样任务
  private final Map<Long, SampleTask> threadTidToTask = new ConcurrentHashMap<>(16);
  
  private SampleTaskPool taskPool;
  private SampleTaskActivator activator;
  private SampleRateLimiter rateLimiter;
  private SampleResultStorage sampleResultStorage;
  private TaskSampleExecutor taskSampleExecutor;
  private StackTraceTruncateHandler stackTraceTruncateHandler;
  
  public DefaultStackTrackSimpler(
    SampleTaskPool taskPool,
    SampleTaskActivator activator,
    SampleRateLimiter rateLimiter,
    SampleResultStorage sampleResultStorage,
    TaskSampleExecutor taskSampleExecutor,
    StackTraceTruncateHandler stackTraceTruncateHandler) {
    this.taskPool = taskPool;
    this.activator = activator;
    this.rateLimiter = rateLimiter;
    this.sampleResultStorage = sampleResultStorage;
    this.taskSampleExecutor = taskSampleExecutor;
    this.stackTraceTruncateHandler = stackTraceTruncateHandler;
  }
  
  private class SimpleTaskCommand implements Runnable {
    @Override
    public void run() {
      boolean active = true;
      while (true) {
        try {
          long startTime = System.currentTimeMillis();
          runSampleTasks(startTime);
          long interval = System.currentTimeMillis() - startTime;
          long sleepTime = Math.min(SAMPLE_INTERVAL_IN_MILLIS - interval, SAMPLE_INTERVAL_IN_MILLIS);
          if (sleepTime > 0) {
            sleepAWhile(sleepTime);
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        } catch (Exception e) {
          logger.error("Got an unexpected exception during sampling thread stack trace.", e);
          // 如果出现报错，强制清理掉所有的采样任务
          cleanAllSampleTasks();
        }
      }
    }
    
    private void sleepAWhile(long sleepTimeMills) throws InterruptedException {
      Thread.sleep(sleepTimeMills);
    }
  }
  
  private void runSampleTasks(long currentTime) {
    // 优先处理采样任务，尽可能保证均匀的采样频率
    List<SampleTask> queuedTasks = taskPool.getRunningTaskList();
    for (SampleTask task : queuedTasks) {
      doSample(task, currentTime);
    }
    
    List<String> removedTaskIds = new ArrayList<>();
    for (SampleTask task : taskPool) {
      if (TaskState.PENDING.equals(task.getTaskState())) {
        // allow sample的判断逻辑必须放在最后，否则会导致采样频率控制不准确；
        if (activator.activate(task) && rateLimiter.allowSample(task.getTaskContext())) {
          // 激活满足条件的任务，同时将任务加入采样任务队列中；
          if (task.start()) {
            taskPool.addToSampleTaskQueue(task.getTaskId());
            doSample(task, currentTime);
          }
        }
      } else if (TaskState.STOPPED.equals(task.getTaskState())) {
        // 将采样结果存储下来，移除已经停止的任务
        DumpResult dumpSampleResult = task.dumpSampleResult();
        storeSampleResult(dumpSampleResult);
        removedTaskIds.add(task.getTaskId());
      } else if (TaskState.DISCARD.equals(task.getTaskState())) {
        // 采样任务已经被丢弃，移除即可
        removedTaskIds.add(task.getTaskId());
      }
    }
    
    for (String taskId : removedTaskIds) {
      taskPool.removeTask(taskId);
    }
  }
  
  private void doSample(SampleTask task, long currentTime) {
    if (!(task instanceof BasedThreadSimpleTask)) {
      return;
    }
    
    // 过滤掉已经停止的任务；
    if ((TaskState.STOPPED.equals(task.getTaskState()))) {
      return;
    }
    if (!(TaskState.STARTED.equals(task.getTaskState()))) {
      logger.warn("Cannot do sample operation, task is not started. [state={}]", task.getTaskState());
      return;
    }
   
    BasedThreadSimpleTask basedThreadSimpleTask = (BasedThreadSimpleTask)task;
    Thread thread = basedThreadSimpleTask.getTargetThread();
    
    boolean forceStopSampleTask = false;
    // 如果任务的执行时间周期超过最大时间周期，则强制停止采样任务；
    if (task.elapsedTimeMillis() >= SAMPLE_TASK_MAX_DURATION_IN_MILLIS) {
      forceStopSampleTask = true;
    } else {
      // 如果线程已经退出，则强制停止该采样任务；
      if (thread == null || !thread.isAlive()) {
        logger.warn(
          "Task thread has already exited, force stop sample task. [taskId={}, taskContext={}]",
          task.getTaskId(), task.getTaskContext());
        forceStopSampleTask = true;
      }
    }
    
    if (forceStopSampleTask) {
      stopSample(task.getTaskId());
    } else {
      // 执行采样，收集线程堆栈
      task.doSample();
    }
  }
  
  private void storeSampleResult(DumpResult dumpResult) {
    try {
      sampleResultStorage.store(dumpResult);
    } catch (Exception e) {
      logger.error("Failed to store sample result. [sampleResult={}]", dumpResult, e);
    }
  }
  
  private void cleanAllSampleTasks() {
    List<String> removedTaskIds = new ArrayList<>();
    for (SampleTask task : taskPool) {
      stopSample(task.getTaskId());
      removedTaskIds.add(task.getTaskId());
    }
    
    for (String taskId : removedTaskIds) {
      taskPool.removeTask(taskId);
    }
  }
  
  @Override
  public void startUp() {
    logger.info("start stack trace sampler...");
    runSampleExecutor();
  }
  
  private void runSampleExecutor() {
    taskSampleExecutor.execute(new SimpleTaskCommand());
  }
  
  @Override
  public String startSample(Thread targetThread, SampleTaskContext sampleTaskContext) {
    if (taskPool.size() > MAX_SAMPLE_CONCURRENCY) {
      logger.error("Too many sample tasks in the task pool. [taskPoolSize={}]", taskPool.size());
    }
    
    SampleTask task = new BasedThreadSimpleTask(sampleTaskContext, new StackTraceCollector(),
      System.currentTimeMillis(), targetThread, stackTraceTruncateHandler);
    taskPool.registerSampleTask(task);
    
    long threadId = targetThread.getId();
    SampleTask oldTask = threadTidToTask.putIfAbsent(threadId, task);
    if (oldTask != null) {
      logger.warn("Multiple sample task binding on the thread. [threadId={}, oldTask={}, newTask={}]", threadId, oldTask, task);
    }
    
    return task.getTaskId();
  }
  
  @Override
  public void stopSample(String taskId) {
    SampleTask task = taskPool.getTaskByTaskId(taskId);
    
    if (task != null) {
      task.stop();
    }
    
    Thread targetThread;
    if (task instanceof BasedThreadSimpleTask && (targetThread = ((BasedThreadSimpleTask) task).getTargetThread()) != null) {
      long threadId = targetThread.getId();
      threadTidToTask.remove((threadId));
    }
  }
  
  @Override
  public void closeUp() {
    logger.info("close stack trace sampler...");
    taskSampleExecutor.close();
  }
  
}
