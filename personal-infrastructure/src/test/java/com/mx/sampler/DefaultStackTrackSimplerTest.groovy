package com.mx.sampler

import com.mx.sampler.context.DefaultSampleContext
import spock.lang.Specification

import java.util.concurrent.locks.ReentrantLock

/**
 *
 * @author FizzPu
 * @since 2024/8/19 下午5:30
 */
class DefaultStackTrackSimplerTest extends Specification {
  def stackTraceSample = new DefaultStackTrackSimpler()

  void setup() {
    stackTraceSample.startUp()
  }

  void cleanup() {
    stackTraceSample.closeUp();
  }

  static class HttpSampleContext extends DefaultSampleContext {
    private String url = "/mock/test"
  }
  def "test slow code"() {
    given: "build sample context"
    def httpSampleContext = new HttpSampleContext()

    and: "start a sample task"
    def sampleId = stackTraceSample.startSample(Thread.currentThread(), httpSampleContext)

    when: "execute business codes"
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    func1()
    fun2()

    then:
    stackTraceSample.stopSample(sampleId)
    System.in.read()
  }

  private void func1() {
    ReentrantLock reentrantLock = new ReentrantLock();
    for (int i = 1; i <= 50000; i++) {
      reentrantLock.lock();
      int a = i * 2 * 32 * 123123131;
      reentrantLock.unlock();
    }

  }

  private void fun2() {
    for (int i = 1; i <= 50000; i++) {
      int a = i * 2 * 32 * 13131;
    }
  }
}
