package com.mx.sampler.limit;

import com.mx.sampler.context.SampleTaskContext;

import java.util.concurrent.TimeUnit;

/**
 * 采样频率限制器
 *
 * @author FizzPu
 * @since 2024/2/28 10:50
 */
public interface SampleRateLimiter {
  /**
   * 按照单个采样结果50K，每个日志文件最大100M（每天），因此每天最多存储2000个采样结果；细分到每个小时，大约为100个
   */
  int MAX_SAMPLE_COUNT_PER_HOUR = 100;
  SampleRateLimiter DEFAULT_RATE_LIMITER = new HourSlotBasedRateLimiter(MAX_SAMPLE_COUNT_PER_HOUR);

  /**
   * 是否允许采样
   *
   * @param taskContext 采样任务上下文
   * @return true: 允许采样，false: 不允许采样
   */
  boolean allowSample(SampleTaskContext taskContext);

  class HourSlotBasedRateLimiter implements SampleRateLimiter {
    private static final long MILLS_PER_HOUR = TimeUnit.HOURS.toMillis(1);
    private final int maxSampleCountPerHour;
    private int sampleCount;
    private long sampleTimeHour;

    public HourSlotBasedRateLimiter(int maxSampleCountPerHour) {
      this.maxSampleCountPerHour = maxSampleCountPerHour;
    }

    boolean allowSample(long currentTimeMills) {
      long currentTimeHour = currentTimeMills / MILLS_PER_HOUR;
      if (currentTimeHour != sampleTimeHour) {
        sampleTimeHour = currentTimeHour;
        sampleCount = 0;
      }

      return sampleCount++ < maxSampleCountPerHour;
    }

    @Override
    public boolean allowSample(SampleTaskContext taskContext) {
      return allowSample(System.currentTimeMillis());
    }
  }
}
