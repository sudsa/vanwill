package com.qifeng.will.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

//实现ClientHttpRequestInterceptor接口
@Component
@Slf4j
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        httpRequest.getHeaders().set("traceId", MDC.get("traceID"));
        return clientHttpRequestExecution.execute(httpRequest,bytes);
    }
}
