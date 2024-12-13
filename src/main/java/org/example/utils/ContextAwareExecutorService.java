package org.example.utils;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContextAwareExecutorService implements Executor {

    private final Executor executor;

    public ContextAwareExecutorService(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public static ContextAwareExecutorService newInstance(int numThreads) {
        return new ContextAwareExecutorService(numThreads);
    }

    @Override
    public void execute(Runnable command) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        SecurityContext securityContext = SecurityContextHolder.getContext();

        executor.execute(() -> {
            try {
                if(contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                if(requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                }
                if(securityContext != null) {
                    SecurityContextHolder.setContext(securityContext);
                }
                command.run();
            } finally {
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
                SecurityContextHolder.clearContext();
            }
        });

    }
}
