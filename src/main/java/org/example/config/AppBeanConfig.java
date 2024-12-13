package org.example.config;

import org.example.utils.ContextAwareExecutorService;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

public class AppBeanConfig {

    public static final int EXECUTOR_NUM_THREADS = 30;

    @Bean
    public Executor contextAwareExecutor() {
        return ContextAwareExecutorService.newInstance(EXECUTOR_NUM_THREADS);
    }

}
