package com.sparta.ssaktium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "emailTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본 스레드 수
        executor.setMaxPoolSize(20); // 최대 스레드 수
        executor.setQueueCapacity(100); // 대기 큐의 크기
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();

        // 스레드 풀의 상태를 로그로 출력
        logThreadPoolStatus(executor);

        return executor;
    }

    private void logThreadPoolStatus(ThreadPoolTaskExecutor executor) {
        System.out.println("Active Threads: " + executor.getActiveCount());
        System.out.println("Total Tasks: " + executor.getThreadPoolExecutor().getTaskCount());
        System.out.println("Completed Tasks: " + executor.getThreadPoolExecutor().getCompletedTaskCount());
    }
}
