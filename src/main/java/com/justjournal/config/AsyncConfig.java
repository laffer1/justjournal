package com.justjournal.config;

import java.util.Arrays;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Thread pool used by {@code @Async} methods. Async is enabled on {@code Application}.
 *
 * <p>Async methods run on their own thread with their own transaction (if annotated
 * {@code @Transactional}); they do not participate in the caller's transaction. If work must run
 * only after the caller commits, publish an event and handle it with
 * {@code @TransactionalEventListener} instead.
 */
@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

  @Bean(name = "taskExecutor")
  public ThreadPoolTaskExecutor taskExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("AsyncThread-");
    executor.initialize();
    return executor;
  }

  @Override
  public Executor getAsyncExecutor() {
    return taskExecutor();
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (ex, method, params) ->
        log.error(
            "Uncaught exception in async method {} with params {}",
            method.getName(),
            Arrays.toString(params),
            ex);
  }
}
