package com.mx.sampler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FizzPu
 * @since 2024/2/26 15:27
 * */
public class Main {
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        
        StackTraceSampler stackTraceSimpler = new DefaultStackTrackSimpler();
        stackTraceSimpler.startUp();
        
        String taskId = stackTraceSimpler.startSample(Thread.currentThread(), new DefaultSampleContext());
        for (int i = 0; i < 500; i++) {
            main.func1();
            main.fun2();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stackTraceSimpler.stopSample(taskId);
        
        
        taskId = stackTraceSimpler.startSample(Thread.currentThread(), new DefaultSampleContext());
        main.fun2();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stackTraceSimpler.stopSample(taskId);
        
        stackTraceSimpler.closeUp();
        System.in.read();
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