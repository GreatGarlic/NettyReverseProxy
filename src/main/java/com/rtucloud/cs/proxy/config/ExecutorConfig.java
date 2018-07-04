package com.rtucloud.cs.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@EnableAsync(proxyTargetClass = true)
@Configuration
public class ExecutorConfig {

    @Bean(name = "frontendWorkTaskExecutor")
    public Executor newSingleThreadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setKeepAliveSeconds(0);
        executor.setQueueCapacity(0);
        executor.setDaemon(true);
        executor.setThreadNamePrefix("frontendWorkTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
