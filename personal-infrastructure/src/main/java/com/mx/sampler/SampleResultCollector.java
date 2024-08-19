package com.mx.sampler;

/**
 * @author FizzPu
 * @since 2024/2/28 10:50
 */
public interface SampleResultCollector {
    void collect(OnceSampleResult onceSampleResult);

    String dump();
}
