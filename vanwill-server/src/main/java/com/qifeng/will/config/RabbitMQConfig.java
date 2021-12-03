package com.qifeng.will.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
public class RabbitMQConfig {

    @Bean(name = "firstConnectionFactory")
    @Primary
    public CachingConnectionFactory firstConnectionFactory(
            @Value("${spring.rabbitmq.first.host}") String host,
            @Value("${spring.rabbitmq.first.port}") int port,
            @Value("${spring.rabbitmq.first.username}") String username,
            @Value("${spring.rabbitmq.first.password}") String password,
            @Value("${spring.rabbitmq.first.virtualHost}") String virtualHost
    ) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean(name = "secondConnectionFactory")
    public CachingConnectionFactory secondConnectionFactory(
            @Value("${spring.rabbitmq.second.host}") String host,
            @Value("${spring.rabbitmq.second.port}") int port,
            @Value("${spring.rabbitmq.second.username}") String username,
            @Value("${spring.rabbitmq.second.password}") String password,
            @Value("${spring.rabbitmq.second.virtualHost}") String virtualHost
    ) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean(name = "firstRabbitTemplate")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型 
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate firstRabbitTemplate = new RabbitTemplate(connectionFactory);
        return firstRabbitTemplate;
    }

    @Bean(name = "secondRabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型 
    public RabbitTemplate secondRabbitTemplate(
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate secondRabbitTemplate = new RabbitTemplate(connectionFactory);
        return secondRabbitTemplate;
    }

    // 配置监听1
    @Bean(name = "firstFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory firstFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    // 配置监听2
    @Bean(name = "secondFactory")
    public SimpleRabbitListenerContainerFactory secondFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /* 创建队列hello1，
       因为这里没有指定connectFactory，所以使用的是默认的connectionFactory，
       即用@Primary标记的firstConnectionFactory创建的，
       所以下面这种写法只会再第一个默认的mq种创建队列
       注意：这一段并不会在第二个mq中创建队列hello1
     */
    @Bean
    public Queue firstQueue() {
        System.out.println("configuration firstQueue ........................");
        return new Queue("first.queue.hello1");
    }



    /** 如果要在第二个mq中也创建队列hello1，那么需要指定数据源secondConnectionFactory **/
    @Bean
    public String chargeQueue(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare("first.queue.hello1", true, false, false, null);
        }catch (IOException e){
            e.printStackTrace();
        }
        return "first.queue.hello1";
    }

    /*@Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean(name = "queueIotHub")
    public Queue queuePocket() {
        return new Queue("iothub.queue.device", true);
    }

    @Bean(name = "bindingIotHub")
    public Binding binding(@Qualifier("queueIotHub") Queue queuePocket, TopicExchange topicExchange) {
        //	return BindingBuilder.bind(queuePocket).to(topicExchange).with("device_realtime_data.device.signal_data");
        return BindingBuilder.bind(queuePocket).to(topicExchange).with(upRoutingKey);
    }*/
}
