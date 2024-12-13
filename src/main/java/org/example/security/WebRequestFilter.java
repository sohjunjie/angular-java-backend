package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static org.example.utils.Constants.*;

@Slf4j
@Component
public class WebRequestFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String correlationId = getOrDefaultCorrelationIdHeader(request);
            String httpMethod = request.getMethod();
            String apiEndpoint = request.getServletPath();

            MDC.put(API_MDC_CORRELATION_ID_KEY, correlationId);
            MDC.put(API_MDC_HTTPMETHOD_KEY, httpMethod);
            MDC.put(API_MDC_ENDPOINT_KEY, apiEndpoint);

            long timeStart = System.currentTimeMillis();

            filterChain.doFilter(request, response);

            long timeEnd = System.currentTimeMillis();

            int httpStatusCode = response.getStatus();
            boolean success = (httpStatusCode >= 200 && httpStatusCode < 300);

            log.info("authSubj={}, timeTakenMs={}, success={}", getAuthUserName(), timeEnd - timeStart, success);

        } finally {
            MDC.clear();
        }

    }

    private String getOrDefaultCorrelationIdHeader(HttpServletRequest request) {
        if(StringUtils.isBlank((request.getHeader(API_MDC_CORRELATION_ID_KEY)))) {
            String correlationId = UUID.randomUUID().toString();
            request.setAttribute(API_MDC_CORRELATION_ID_KEY, correlationId);
            return correlationId;
        }
        return request.getHeader(API_MDC_CORRELATION_ID_KEY);
    }

    private String getAuthUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
