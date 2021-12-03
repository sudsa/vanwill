package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

//@Component
public class DirectMsgReceiver {

    @RabbitListener(queues = "testDirectQueue")
    public void process(Map testMessage) {
        System.out.println("第一个1 DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

    @RabbitListener(queues = "testDirectQueue")
    public void process2(Map testMessage) {
        System.out.println("第一个2 DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

    @RabbitListener(queues = "testDirectQueue")
    public void process3(Map testMessage) {
        System.out.println("第一个3 DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
