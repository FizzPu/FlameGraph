package com.mx.sampler;

import com.mx.sampler.context.SampleTaskContext;

/**
 * @author FizzPu
 * @since 2024/2/26 15:36
 */
public interface StackTraceSampler {
    /**
     * <p>开启采样器</p>
     */
    void startUp();
    
    /**
     * <p>开始一次采样</p>
     */
    String startSample(Thread targetThread, SampleTaskContext sampleTaskContext);
    
    /**
     * <p>关闭一次采样</p>
     */
    void stopSample(String taskId);
    
    /**
     * <p>销毁采样器</p>
     */
    void closeUp();
}
