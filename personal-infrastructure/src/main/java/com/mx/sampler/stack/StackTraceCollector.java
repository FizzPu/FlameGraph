package com.mx.sampler.stack;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author FizzPu
 * @since 2024/2/28 11:09
 */
public class StackTraceCollector implements SampleResultCollector {
    
    private static final Logger log = LoggerFactory.getLogger(StackTraceCollector.class);
    
    private final Cache<StackTrace, Integer> stackTraceToCount = CacheBuilder.newBuilder().softValues().build();
    
    @Override
    public void collect(OnceSampleResult onceSampleResult) {
        Preconditions.checkNotNull(onceSampleResult);
        if (!(onceSampleResult instanceof StackTrace)) {
            return;
        }
        StackTrace stackTrace = (StackTrace) onceSampleResult;
        
        Integer counter = stackTraceToCount.getIfPresent(stackTrace);
        if (counter == null) {
            counter = 0;
        } else {
            counter++;
        }
        
        stackTraceToCount.put(stackTrace, counter);
        if (log.isDebugEnabled()) {
            log.debug("collect result. [stackTrace = {}]", stackTrace);
        }
    }
    
    @Override
    public String dump() {
        StringBuilder output = new StringBuilder();
        output.append("stackTrace:\n");
        Map<StackTrace, Integer> stackTraceToCountMap = stackTraceToCount.asMap();
        for (Map.Entry<StackTrace, Integer> entry: stackTraceToCountMap.entrySet()) {
            output.append(entry.getKey().dump());
            output.append(" ");
            output.append(entry.getValue());
            output.append("\n");
        }
        output.setLength(output.length() - 1);
        return output.toString();
    }
}
