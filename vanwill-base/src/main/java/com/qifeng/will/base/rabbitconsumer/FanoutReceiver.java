package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

//@Component
public class FanoutReceiver{

    @RabbitListener(queues = "fanout.A")
    public void processA(Map testMessage) {
        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }

    @RabbitListener(queues = "fanout.B")
    public void processB(Map testMessage) {

        System.out.println("FanoutReceiverB消费者收到消息  : " +testMessage.toString());
    }

    @RabbitListener(queues = "fanout.C")
    public void process(Map testMessage) {

        System.out.println("FanoutReceiverC消费者收到消息  : " +testMessage.toString());
    }

}
