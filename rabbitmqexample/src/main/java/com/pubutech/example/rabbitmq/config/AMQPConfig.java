/*
 * SC License
 *
 * Copyright (c)  SongChao 2018 .
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copyof this software and associated documentation files (the "Software").
 *
 * This software is only used for learning and communication.
 * Without permission,please do not use the software illegally.
 */

package com.pubutech.example.rabbitmq.config;

import com.pubutech.example.rabbitmq.amqp.FirstDelayConsumer;
import com.pubutech.example.rabbitmq.amqp.RabbitMQChannel;
import com.pubutech.example.rabbitmq.properties.RabbitMQProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/13
 * @since 1.0
 */
@Slf4j
@Configuration
public class AMQPConfig {

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Bean
    public ConnectionFactory connectionFactory(){
        //第一步配置连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(rabbitMQProperties.getUsername());
        factory.setPassword(rabbitMQProperties.getPassword());
        factory.setVirtualHost(rabbitMQProperties.getVhost());
        factory.setHost(rabbitMQProperties.getHost());
        factory.setPort(rabbitMQProperties.getPort());

        return factory;
    }

    @Bean(destroyMethod = "close")
    public RabbitMQChannel rabbitMQChannel(ConnectionFactory connectionFactory){
        return new RabbitMQChannel(connectionFactory);
    }

    @Bean
    public AMQP.Exchange.DeclareOk declareFirstDelayExchange(RabbitMQChannel rabbitMQChannel){
        log.info(" rabbitMQChannel {}   channel {} ",rabbitMQChannel,rabbitMQChannel.getChannel());

        try {
            return rabbitMQChannel.getChannel().exchangeDeclare("first-delay-exchange", BuiltinExchangeType.DIRECT, true, false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AMQP.Exchange.DeclareOk declareFirstDeadExchange(RabbitMQChannel rabbitMQChannel){
        try {
            return rabbitMQChannel.getChannel().exchangeDeclare("first-delay-dead-exchange", BuiltinExchangeType.DIRECT, true, false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AMQP.Queue.DeclareOk declareFirstDelayQueen(RabbitMQChannel rabbitMQChannel){
        Map<String, Object> map = new HashMap<>();
        //标志队列中的消息存活时间，也就是说队列中的消息超过了指定时间会被删除(数字类型，标志时间，以豪秒为单位)
        map.put("x-message-ttl", rabbitMQProperties.getFirstdelaytimeout());


        /*
        消息因为超时或超过限制在队列里消失，这样我们就丢失了一些消息，也许里面就有一些是我们做需要获知的。而rabbitmq的死信功能则为我们带来了解决方案。
        设置了dead letter exchange与dead letter routingkey（要么都设定，要么都不设定）那些因为超时或超出限制而被删除的消息会被推动到我们设置的exchange中，
        再根据routingkey推到queue中.
        */
        map.put("x-dead-letter-exchange","first-delay-dead-exchange");
        map.put("x-dead-letter-routing-key","first-delay-dead");


        try {
            return rabbitMQChannel.getChannel().queueDeclare("first-delay-queen", true, false, false, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AMQP.Queue.DeclareOk declareFirstDelayDeadQueen(RabbitMQChannel rabbitMQChannel){

        try {
            return rabbitMQChannel.getChannel().queueDeclare("first-delay-dead-queen", true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AMQP.Queue.BindOk bindFirstDelayQueen(RabbitMQChannel rabbitMQChannel){
        try {
             return rabbitMQChannel.getChannel().queueBind("first-delay-queen", "first-delay-exchange", "first-delay", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public AMQP.Queue.BindOk bindFirstDelayDeadQueen(RabbitMQChannel rabbitMQChannel){
        try {
            return rabbitMQChannel.getChannel().queueBind("first-delay-dead-queen", "first-delay-dead-exchange", "first-delay-dead", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Bean
//    public FirstDelayConsumer firstDelayConsumer(RabbitMQChannel rabbitMQChannel){
//        return new FirstDelayConsumer(rabbitMQChannel);
//    }

}
