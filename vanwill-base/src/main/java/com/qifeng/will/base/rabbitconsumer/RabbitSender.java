package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class RabbitSender {

    @Resource(name="firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;

    public void send1() {
        String context = "quene1:hello1 " + new Date();
        System.out.println("Sender : " + context);
        this.firstRabbitTemplate.convertAndSend("first.queue.hello1", context);
    }

    @Resource(name="secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    public void send2() {
        String context = "quene2:hello1 " + new Date();
        System.out.println("Sender : " + context);
        this.secondRabbitTemplate.convertAndSend("first.queue.hello1", context);
    }

}
