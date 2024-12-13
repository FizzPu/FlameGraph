## FlameGraph

### 背景

对于Java线上服务来说，可能会出现偶发的慢请求。当面临此类性能问题时，由于无法稳定复现，缺少必要的追查线索，所以个人觉得排查起来还是有一点难度的。本项目通过对线上请求的监控，采集被监控代码的Java堆栈，并提供Web服务来解析Java堆栈生成火焰图，来辅助开发者定位可能存在的性能问题。

### Feature
- [x] 数据采集侧支持springboot starter
- [x] 数据解析侧提供在线web服务，生成火焰图
- [x] docker构建镜像，自动安装perl、java等依赖

### 使用与接入

#### 数据采集侧
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
      
      public String getUrl() {
        return url;
      }
      
      public void setUrl(String url) {
        this.url = url;
      }
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


#### 数据解析侧

### 演示
下面是对Java代码进行采样的一次结果，整个采样结果主要有三个方面的内容taskInfo，taskContext，stackTrace。 taskInfo表示采样任务的基本详情，记录了任务的持续时间、采样的时间间隔、采样开始时间等信息。taskContext表示采样上下文，允许开发者定制化自己的信息，stackTrace表示采集到的堆栈信息。

```java
17:03:12.219 [Thread-0] INFO BasedLoggerSampleResultStorage - <$start
taskInfo: {"duration":2425,"sampleInterval":0,"datetime":"2024-08-23 17:03:09.751","id":"7147c2a5-fac4-401d-a508-12a292380195","sampleStartTime":"2024-08-23 17:03:09.783","threadName":"main"}
taskContext: {"url":"/mock/test"}
stackTrace:
com.mx.sampler.Main(main:32);com.mx.sampler.Main(executeSlowRequest:47);com.mx.sampler.Main(fun2:65);java.lang.Thread(sleep:-2); 16
com.mx.sampler.Main(main:32);com.mx.sampler.Main(executeSlowRequest:46);com.mx.sampler.Main(func1:57);java.lang.Thread(sleep:-2); 83
end$>
```



通过访问http://106.14.180.9:8080/flamegraph/logs，账号：admin， 密码：panzer，生成可视化的结果。

![image-20240824125717630](/sample-result.png)

![image-20240823174702139](/flame-graph-demo.png)



从火焰图可以看出来，com.mx.sampler.func1比com.mx.sampler.func2的执行时间要长，占了83个采样，平均每个采样20毫秒，那么com.mx.sampler.func1约耗时83 * 20ms = 1660毫秒。在具体的实践过程中，可能就要重点优化此段逻辑。



### 基本原理
启动一个采样线程，每隔一段时间对Java堆栈进行采集，然后序列化成火焰图UI的形式。

![sample-diagram](/sample-diagram.png)

参考资料:
- https://www.ruanyifeng.com/blog/2017/09/flame-graph.html
- https://github.com/brendangregg/FlameGraph

### 后续

- 支持spring-boot-starter, 开箱即用
- 优化UI展示
