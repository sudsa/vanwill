package com.qifeng.will.base.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

/**
 * 功能描述: <br>
 * @Date: 2019/7/7
 */
//@Component
public class TopicManReceiver {

    @RabbitListener(queues = "topic.man")
    public void process(Map testMessage) {
        System.out.println("TOPIC MAN 消费者收到消息  : " +testMessage.toString());
    }

    @RabbitListener(queues = "topic.woman")
    public void process2(Map testMessage) {
        System.out.println("TOPIC all消费者收到消息  : " +testMessage.toString());
    }

}
