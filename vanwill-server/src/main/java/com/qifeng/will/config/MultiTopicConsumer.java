package com.qifeng.will.config;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
//@RabbitListener(queues = "queue.example.topic.new", containerFactory = "v1ContainerFactory")
public class MultiTopicConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.println(message);
    }
}
