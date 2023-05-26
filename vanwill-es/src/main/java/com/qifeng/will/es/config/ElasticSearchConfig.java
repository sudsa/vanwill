package com.qifeng.will.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ElasticSearchConfig
 * Description:
 *
 * @author FUIOU
 * @date 2023/5/24 16:25
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${hostName}")
    private String host;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        //es添加了安全访问规则，访问es需要添加一个安全头，就可以通过requestOptions设置
        //官方建议把requestOptions创建成单实例
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        //这中间可能还有一些设置项，就是添加安全头之类的
        COMMON_OPTIONS = builder.build();
    }

    @Bean("esSafeClient")
    public RestHighLevelClient esRestClient() {

        RestClientBuilder builder = null;
        // 可以指定多个es
        builder = RestClient.builder(new HttpHost(host, 9200, "http"),new HttpHost(host, 9300, "http"),new HttpHost(host, 9400, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
