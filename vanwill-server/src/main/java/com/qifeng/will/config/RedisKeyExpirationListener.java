package com.qifeng.will.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component("redisKeyExpirationListener")
@RequiredArgsConstructor
@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {

        //  获取失效的key
        String expiredKey = message.toString();
        log.info("监听到Redis中：{}过期", expiredKey);
        //  指定key 的前缀=device:key:
        if (expiredKey.startsWith("test")) {
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("fangchong:"+expiredKey  , LocalDateTime.now().toString());
            if(aBoolean) {
                String username = expiredKey.split(":")[2];
                log.info("user:" + username + " 在redis中失效过期");

            }
        }


    }
}
