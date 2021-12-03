package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "first.queue.hello1", containerFactory="firstFactory")
public class RabbitListener1 {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("queue1-Receiver  : " + hello);
    }

}
