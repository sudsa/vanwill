package com.qifeng.will.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/*
 *
 * @DESC 注册一个redis的消息监听器给spring容器管理
 * @DESC
 * @author zouhw
 * @date 2022/5/30 14:11
 * @param
 * @return
 */
@Configuration
public class RedisConfigListenerContainer {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListener redisKeyExpirationListener){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisKeyExpirationListener, new PatternTopic("__keyevent@1__:expired"));
        return container;
    }


}
