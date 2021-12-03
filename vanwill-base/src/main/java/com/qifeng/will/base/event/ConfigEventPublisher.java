package com.qifeng.will.base.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     *  事件发布方法
     */
    public void pushListener(String msg) {
        applicationEventPublisher.publishEvent(new ConfigEvent(this, msg));
    }


}
