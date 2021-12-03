package com.qifeng.will.base.event;


import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ConfigEventListener {

    @Async
    @Order
    @EventListener(ConfigEvent.class)
    public void getErr(ConfigEvent event) {
        System.out.println("当前操作Excel：" + event.getMsg() );
    }

}
