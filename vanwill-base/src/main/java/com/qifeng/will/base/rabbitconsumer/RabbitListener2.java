package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "first.queue.hello1", containerFactory="secondFactory")
public class RabbitListener2 {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("queue12-Receiver  : " + hello);
    }

}
