package com.clover.common.config.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dingpengfei
 * @date 2019-03-21 15:06
 */
@Configuration
@EnableAsync
public class TaskExecutePool {
  @Bean
  public Executor taskAsyncPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    TaskThreadPoolConfig config = new TaskThreadPoolConfig(20, 100, 100, 120);
    // 初始线程大小
    executor.setCorePoolSize(config.getCorePoolSize());
    // 最大线程大小
    executor.setMaxPoolSize(config.getMaxPoolSize());
    // 设置队列长度
    executor.setQueueCapacity(config.getQueueCapacity());
    // 设置多长时间，线程回收
    executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }

  private class TaskThreadPoolConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveSeconds;
    private int queueCapacity;

    public TaskThreadPoolConfig(
        int corePoolSize, int maxPoolSize, int keepAliveSeconds, int queueCapacity) {
      this.corePoolSize = corePoolSize;
      this.maxPoolSize = maxPoolSize;
      this.keepAliveSeconds = keepAliveSeconds;
      this.queueCapacity = queueCapacity;
    }

    public int getCorePoolSize() {
      return corePoolSize;
    }

    public int getMaxPoolSize() {
      return maxPoolSize;
    }

    public int getKeepAliveSeconds() {
      return keepAliveSeconds;
    }

    public int getQueueCapacity() {
      return queueCapacity;
    }
  }
}
