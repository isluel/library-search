package com.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean("lsExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 기본 core 사이즈
        int cpuCoreCount = Runtime.getRuntime().availableProcessors();
//        executor.setCorePoolSize(cpuCoreCount);
        executor.setCorePoolSize(2);
        // 최대 풀 사이즈
        // 빠른 응답성을 필요로 하는 경우에는 Queuecapacity를 0, MaxPoolSize는 Integer.MAX 로..
//        executor.setMaxPoolSize(cpuCoreCount * 2);
        executor.setMaxPoolSize(2);
        // 작업 Queue의 사이즈
//        executor.setQueueCapacity(10);
        executor.setQueueCapacity(2);
        // 추가 Thread 가 대기할 시간
        executor.setKeepAliveSeconds(60);
        // Application 종료시 남은 Thread 작업 완료 대기 시간
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix("LS-");

        // reject 시 발생하는 Exception Handler
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();

        return executor;
    }

    // Async 작업 예외 처리 핸들러
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // Event Handler에서 에러 발생할 경우 처리.
        // 보통은 Error만 찍고 나감
        return new CustomAsyncExceptionHandler();
    }

    private static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("Fail to execute {}", ex.getMessage());
            Arrays.asList(params).forEach(p -> log.error("parameter Value = {}", p));
        }
    }
}
