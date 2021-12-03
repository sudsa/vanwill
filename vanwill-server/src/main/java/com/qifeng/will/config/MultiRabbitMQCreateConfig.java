package com.qifeng.will.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

//@Configuration
public class MultiRabbitMQCreateConfig {

    @Resource(name = "v2RabbitAdmin")
    private RabbitAdmin v2RabbitAdmin;

    @Resource(name = "v1RabbitAdmin")
    private RabbitAdmin v1RabbitAdmin;

    @PostConstruct
    public void RabbitInit() {
        v2RabbitAdmin.declareExchange(new TopicExchange("exchange.topic.example.new", true, false));
        v2RabbitAdmin.declareQueue(new Queue("queue.example.topic.new", true));
        v2RabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("queue.example.topic.new", true))        //直接创建队列
                        .to(new TopicExchange("exchange.topic.example.new", true, false))    //直接创建交换机 建立关联关系
                        .with("routing.key.example.new"));    //指定路由Key
    }

    @PostConstruct
    public void RabbitInit2() {
        v1RabbitAdmin.declareExchange(new TopicExchange("exchange.topic.example.new", true, false));
        v1RabbitAdmin.declareQueue(new Queue("queue.example.topic.new", true));
        v1RabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("queue.example.topic.new", true))        //直接创建队列
                        .to(new TopicExchange("exchange.topic.example.new", true, false))    //直接创建交换机 建立关联关系
                        .with("routing.key.example.new"));    //指定路由Key
    }
}
