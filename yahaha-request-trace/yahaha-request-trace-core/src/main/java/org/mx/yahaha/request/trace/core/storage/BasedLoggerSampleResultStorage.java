package org.mx.yahaha.request.trace.core.storage;

import org.mx.yahaha.request.trace.core.convert.JsonConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author FizzPu
 * @since 2024/5/30 下午9:13
 */
public class BasedLoggerSampleResultStorage implements SampleResultStorage {
  public static final String DEFAULT_LOGGER_NAME = "BasedLoggerSampleResultStorage";
  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private final Logger saveLogger;
  private static final Logger logger = LoggerFactory.getLogger(BasedLoggerSampleResultStorage.class);
  
  public BasedLoggerSampleResultStorage(String loggerName) {
    this.saveLogger = LoggerFactory.getLogger(loggerName);
  }
  
  public BasedLoggerSampleResultStorage() {
    this(DEFAULT_LOGGER_NAME);
  }
  
  private String toDateTime(SimpleDateFormat format, long timeMills) {
    if (timeMills > 0) {
        return format.format(new Date(timeMills));
    }
    return "";
  }
  
  @Override
  public void store(DumpResult result) {
    if (!(result instanceof StackTraceDumpResult)) {
      return;
    }
    
    StackTraceDumpResult stackTraceDumpResult = (StackTraceDumpResult) result;
    SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
    Map<String, Object> taskInfo = new HashMap<>();
    taskInfo.put("id", stackTraceDumpResult.getTaskId());
    taskInfo.put("duration", stackTraceDumpResult.getTaskExecutionTime());
    taskInfo.put("threadName", stackTraceDumpResult.getThreadName());
    taskInfo.put("datetime", toDateTime(formatter, stackTraceDumpResult.getStartTimeMills()));
    taskInfo.put("sampleInterval", stackTraceDumpResult.getSampleIntervalInMills());
    taskInfo.put("sampleStartTime", toDateTime(formatter, stackTraceDumpResult.getSampleStartTimeMills()));
    
    try {
      String rawTaskInfo = JsonConvertor.me().toJson(taskInfo);
      String rawTaskContext = JsonConvertor.me().toJson(stackTraceDumpResult.getTaskContext());
      String stackTrace = stackTraceDumpResult.getStackTrace();
      saveLogger.info("<$start\ntaskInfo: {}\ntaskContext: {}\n{}\nend$>", rawTaskInfo, rawTaskContext, stackTrace);
    } catch (Throwable throwable) {
      logger.warn("unexpected error occur when save dump result.", throwable);
    }
  }
}
