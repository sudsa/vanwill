package com.qifeng.will.config;

import com.qifeng.will.interceptor.RestTemplateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
/*
 *  MDC的底层是用了ThreadLocal来保存数据的。
    我们可以用它传递参数，进行全链路跟踪。
    例如现在有这样一种场景：我们使用RestTemplate调用远程接口时，有时需要在header中传递信息，
    * 比如：traceId，source等，便于在查询日志时能够串联一次完整的请求链路，快速定位问题。
 * @author zouhw
 * @date 2022/6/1 10:14
 * @param
 * @return
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor()));
        return restTemplate;
    }

    @Bean
    public RestTemplateInterceptor restTemplateInterceptor() {
        return new RestTemplateInterceptor();
    }

}
