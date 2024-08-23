package com.mx.sampler;

import com.mx.sampler.context.DefaultSampleContext;
import com.mx.sampler.context.SampleTaskContext;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FizzPu
 * @since 2024/8/23 下午4:51
 */
public class Main {
  public static void main(String[] args) throws IOException {
    StackTraceSampler stackTraceSample = new DefaultStackTrackSimpler();
    
    // 进程启动，开启采样器
    stackTraceSample.startUp();
    
    
    // 构建采样上下文
    class HttpSampleContext extends DefaultSampleContext {
      private String url = "/mock/test";
    }
    SampleTaskContext httpSampleContext = new HttpSampleContext();
    
    // 开始一个采样任务
    String sampleTaskId = stackTraceSample.startSample(Thread.currentThread(), httpSampleContext);
    
    // 执行业务代码
    try {
      executeSlowRequest();
    } catch (InterruptedException e) {
      // ignore
    } finally {
      // 关闭当前采样任务
      stackTraceSample.stopSample(sampleTaskId);
    }
    
    // 关闭采样器
    stackTraceSample.closeUp();
    System.in.read();
  }

  private static void executeSlowRequest() throws InterruptedException {
    func1();
    fun2();
  }
  
  private static void func1() throws InterruptedException {
    ReentrantLock reentrantLock = new ReentrantLock();
    for (int i = 1; i <= 50000; i++) {
      reentrantLock.lock();
      int a = i * 2 * 32 * 123123131;
      reentrantLock.unlock();
    }
    Thread.sleep(2000);
    
  }
  
  private static void fun2() throws InterruptedException {
    for (int i = 1; i <= 50000; i++) {
      int a = i * 2 * 32 * 13131;
    }
    Thread.sleep(400);
  }
}
