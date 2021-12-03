package com.qifeng.will.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

//@Configuration
@Slf4j
public class MultipleRabbitMQConfig {

    @Bean(name = "v2ConnectionFactory")
    public CachingConnectionFactory hospSyncConnectionFactory(
            @Value("${v2.spring.rabbitmq.host}") String host,
            @Value("${v2.spring.rabbitmq.port}") int port,
            @Value("${v2.spring.rabbitmq.username}") String username,
            @Value("${v2.spring.rabbitmq.password}") String password,
            @Value("${v2.spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${v2.spring.rabbitmq.publisher-confirms}") Boolean publisherConfirms,
            @Value("${v2.spring.rabbitmq.publisher-returns}") Boolean publisherReturns) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        return connectionFactory;
    }

    @Bean(name = "v2RabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型 
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v2.spring.rabbitmq.template.mandatory}") Boolean mandatory) {
        RabbitTemplate v2RabbitTemplate = new RabbitTemplate(connectionFactory);
        v2RabbitTemplate.setMandatory(mandatory);
        v2RabbitTemplate.setConfirmCallback((correlationData, ack, s) -> {
            if (!ack) {
                log.info("{} 发送RabbitMQ消息 ack确认 失败: [{}]", JSON.toJSONString(s));
            } else {
                log.info("{} 发送RabbitMQ消息 ack确认 成功: [{}]", JSON.toJSONString(s));
            }
        });
        v2RabbitTemplate.setReturnCallback((message, code, s, exchange, routingKey) -> {
            log.error("{} 发送RabbitMQ消息returnedMessage，出现异常，Exchange不存在或发送至Exchange却没有发送到Queue中，message：[{}], code[{}], s[{}], exchange[{}], routingKey[{}]", new Object[]{JSON.toJSONString(message), JSON.toJSONString(code), JSON.toJSONString(s), JSON.toJSONString(exchange), JSON.toJSONString(routingKey)});
        });
        return v2RabbitTemplate;
    }

    @Bean(name = "v2ContainerFactory")
    public SimpleRabbitListenerContainerFactory hospSyncFactory(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v2.spring.rabbitmq.listener.simple.acknowledge-mode}") String acknowledge,
            @Value("${v2.spring.rabbitmq.listener.simple.prefetch}") Integer prefetch
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.valueOf(acknowledge.toUpperCase()));
        factory.setPrefetchCount(prefetch);
        return factory;
    }

    @Bean(name = "v2RabbitAdmin")
    public RabbitAdmin iqianzhanRabbitAdmin(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    // mq主连接
    @Bean(name = "v1ConnectionFactory")
    @Primary
    public CachingConnectionFactory publicConnectionFactory(
            @Value("${v1.spring.rabbitmq.host}") String host,
            @Value("${v1.spring.rabbitmq.port}") int port,
            @Value("${v1.spring.rabbitmq.username}") String username,
            @Value("${v1.spring.rabbitmq.password}") String password,
            @Value("${v1.spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${v1.spring.rabbitmq.publisher-confirms}") Boolean publisherConfirms,
            @Value("${v1.spring.rabbitmq.publisher-returns}") Boolean publisherReturns) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        return connectionFactory;
    }

    @Bean(name = "v1RabbitTemplate")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型 
    public RabbitTemplate publicRabbitTemplate(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v1.spring.rabbitmq.template.mandatory}") Boolean mandatory) {
        RabbitTemplate v1RabbitTemplate = new RabbitTemplate(connectionFactory);
        v1RabbitTemplate.setMandatory(mandatory);
        v1RabbitTemplate.setConfirmCallback((correlationData, ack, s) -> {
            if (!ack) {
                log.info("{} 发送RabbitMQ消息 ack确认 失败: [{}]", JSON.toJSONString(s));
            } else {
                log.info("{} 发送RabbitMQ消息 ack确认 成功: [{}]", JSON.toJSONString(s));
            }
        });
        v1RabbitTemplate.setReturnCallback((message, code, s, exchange, routingKey) -> {
            log.error("{} 发送RabbitMQ消息returnedMessage，出现异常，Exchange不存在或发送至Exchange却没有发送到Queue中，message：[{}], code[{}], s[{}], exchange[{}], routingKey[{}]", new Object[]{JSON.toJSONString(message), JSON.toJSONString(code), JSON.toJSONString(s), JSON.toJSONString(exchange), JSON.toJSONString(routingKey)});
        });
        return v1RabbitTemplate;
    }

    @Bean(name = "v1ContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory insMessageListenerContainer(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v1.spring.rabbitmq.listener.simple.acknowledge-mode}") String acknowledge,
            @Value("${v1.spring.rabbitmq.listener.simple.prefetch}") Integer prefetch) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.valueOf(acknowledge.toUpperCase()));
        factory.setPrefetchCount(prefetch);
        return factory;
    }

    @Bean(name = "v1RabbitAdmin")
    @Primary
    public RabbitAdmin publicRabbitAdmin(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("exchange.topic.example.new");
    }

    @Bean(name = "queueExample")
    public Queue queuePocket() {
        return new Queue("queue.example.topic.new", true);
    }

    @Bean(name = "bindingExample")
    public Binding binding(@Qualifier("queueExample") Queue queuePocket, TopicExchange topicExchange) {
        //	return BindingBuilder.bind(queuePocket).to(topicExchange).with("device_realtime_data.device.signal_data");
        return BindingBuilder.bind(queuePocket).to(topicExchange).with("routing.key.example.new");
    }

}
