package org.mx.yahaha.request.trace.core.task;

import java.util.concurrent.ThreadFactory;

/**
 * @author FizzPu
 * @since 2024/5/30 下午8:50
 */
public class DemonThreadFactory implements ThreadFactory {
  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    thread.setDaemon(true);
    thread.setPriority(Thread.NORM_PRIORITY);
    return thread;
  }
}
