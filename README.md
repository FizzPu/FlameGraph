#### 火焰图技术设计

### 背景

对于Java线上服务来说，可能会出现偶发的慢请求。当面临此类性能问题时，由于无法稳定复现，缺少必要的线索，所以个人觉得排查起来还是有一点难度的。本项目通过对线上请求的监控，并生成火焰图，来辅助开发者定位可能存在的性能问题。



### 使用与接入

下面的代码是一个接入示例，通过采集一段Java代码的堆栈，请求完成后，会输出采集结果。

~~~Java
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
~~~



### 演示



```java
17:03:12.219 [Thread-0] INFO BasedLoggerSampleResultStorage - <$start
taskInfo: {"duration":2425,"sampleInterval":0,"datetime":"2024-08-23 17:03:09.751","id":"7147c2a5-fac4-401d-a508-12a292380195","sampleStartTime":"2024-08-23 17:03:09.783","threadName":"main"}
taskContext: null
stackTrace:
com.mx.sampler.Main(main:32);com.mx.sampler.Main(executeSlowRequest:47);com.mx.sampler.Main(fun2:65);java.lang.Thread(sleep:-2); 16
com.mx.sampler.Main(main:32);com.mx.sampler.Main(executeSlowRequest:46);com.mx.sampler.Main(func1:57);java.lang.Thread(sleep:-2); 83
end$>
```



http://106.14.180.9:8080/flamegraph/logs

账号：admin， 密码：panzer

![image-20240823174702139](/Users/fizz/WorkSpace/Codes/CompanyCodes/sensorsdata/Personal-Web-Service/flame-graph-demo.png)



从火焰图可以看出来，com.mx.sampler.func1比com.mx.sampler.func2的执行时间要长，占了83个采样，平均每个采样20毫秒，那么com.mx.sampler.func1约耗时83 * 20ms = 1660毫秒。在具体的实践过程中，可能就要重点优化此段逻辑。



### 基本原理



### 后续

