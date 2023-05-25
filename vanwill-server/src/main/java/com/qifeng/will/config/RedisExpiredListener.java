package com.qifeng.will.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

//redis消息过期监听器
@Component
@Slf4j
public class RedisExpiredListener extends KeyExpirationEventMessageListener {
    public RedisExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    //过期触发方法
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("cation ---- key {} expired", expiredKey);
    }
}
