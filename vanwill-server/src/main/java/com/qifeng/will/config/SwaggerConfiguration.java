package com.qifeng.will.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableSwagger2
@EnableKnife4j
public class SwaggerConfiguration {

    @Bean(value = "userApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.will.vanoil.warrior.controler"))
                .paths(PathSelectors.any())
                .build().securityContexts(getSecurityContexts()).securitySchemes(getSecuritySchemes());
    }


    /**
     * @Date: 2020/12/25 10:24
     * @Description: API详情配置
     */
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("基于springcloud的接口文档")
                .description("<div style='font-size:14px;color:red;'>knife4j swagger RESTful APIs</div>")
                .termsOfServiceUrl("http://10.39.80.39:8010")
                .version("1.0")
                .build();
    }

    /**
     * @Date: 2020/12/25 10:33
     * @Description: 安全上下文。即存储认证授权的相关信息，实际上就是存储"当前用户"账号信息和相关权限
     */
    private List<SecurityContext> getSecurityContexts() {
        return Lists.newArrayList(securityContext(), securityContext1());
    }

    /**
     * @Date: 2020/12/25 10:33
     * @Description: 安全上下文。即存储认证授权的相关信息，实际上就是存储"当前用户"账号信息和相关权限
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    /**
     * @Author: 马家立
     * @Date: 2020/12/25 10:33
     * @Description: 安全上下文1。即存储认证授权的相关信息，实际上就是存储"当前用户"账号信息和相关权限
     */
    private SecurityContext securityContext1() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth1())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    /**
     * @Date: 2020/12/25 10:34
     * @Description: 安全引用
     */
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }

    /**
     * @Date: 2020/12/25 10:34
     * @Description: 安全引用1
     */
    List<SecurityReference> defaultAuth1() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken1", authorizationScopes));
    }

    /**
     * @Date: 2020/12/25 10:37
     * @Description: 描述API如何保护（基本认证，OAuth2，...）。
     */
    private ArrayList<SecurityScheme> getSecuritySchemes() {
        ApiKey apiKey = new ApiKey("BearerToken", "Authorization", "header");
        ApiKey apiKey1 = new ApiKey("BearerToken1", "Authorization-x", "header");
        return Lists.<SecurityScheme>newArrayList(apiKey, apiKey1);
    }

}
