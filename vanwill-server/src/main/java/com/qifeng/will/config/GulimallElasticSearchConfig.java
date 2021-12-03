package com.qifeng.will.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class GulimallElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String hostname;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.user}")
    private String user;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.timeout}")
    private long socketTimeout;
    @Value("${elasticsearch.scheme}")
    private String scheme;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    /**
     * 无账号密码连接方式
     **/
    @Bean(name = "esUnSafeClient")
    public RestHighLevelClient esUnSafeRestClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        //集群配置法
                        new HttpHost("localhost", 9201, "http")));
    }

    /**
     *  使用账号密码连接
     **/
    @Bean(name = "esSafeClient")
    public RestHighLevelClient esSafeRestClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostname, port, scheme));
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
        builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
        return new RestHighLevelClient(builder);
    }
}