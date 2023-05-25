package com.qifeng.will.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        MDC.put("traceID",UUID.randomUUID().toString());
        log.info("记录请求日志,traceId = {}", MDC.get("traceID"));
        chain.doFilter(request, response);
        log.info("记录响应日志");

    }

    @Override
    public void destroy() {

    }
}
