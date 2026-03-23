package com.um.springbootprojstructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final PromptLogger promptLogger;

    public RequestLoggingInterceptor(PromptLogger promptLogger) {
        this.promptLogger = promptLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        Map<String, String> headers = Collections.list((Enumeration<String>) request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader, (a, b) -> b));

        Map<String, String[]> params = request.getParameterMap();

        promptLogger.log("[HTTP_REQUEST] method=" + method + " uri=" + uri
                + " params=" + params
                + " headers=" + headers);

        return true;
    }
}
