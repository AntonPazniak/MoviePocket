package com.moviePocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10); // Количество потоков в пуле задач
        taskScheduler.setThreadNamePrefix("scheduled-task-"); // Префикс имен потоков
        taskScheduler.setRemoveOnCancelPolicy(true); // Удалять задачи из пула, если они были отменены
        return taskScheduler;
    }
}
