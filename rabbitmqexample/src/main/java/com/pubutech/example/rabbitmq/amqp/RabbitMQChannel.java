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

package com.pubutech.example.rabbitmq.amqp;

import com.pubutech.example.rabbitmq.properties.RabbitMQProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/13
 * @since 1.0
 */
@Data
@Slf4j
public class RabbitMQChannel {

    private ConnectionFactory connectionFactory = null;

    private Connection conn = null;

    private Channel channel = null;

    public RabbitMQChannel(){}

    public RabbitMQChannel(ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;

        //创建连接
        try {
            conn = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        if(conn != null){
            try {
                channel = conn.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log.info("Success :"+channel.isOpen());

        try {
            //我们可以通过prefetchCount 限制每个消费者在收到下一个确认回执前一次可以最大接受多少条消息。即如果设置prefetchCount =1，RabbitMQ向这个消费者发送一个消息后，再这个消息的消费者对这个消息进行ack之前，RabbitMQ不会向这个消费者发送新的消息
            channel.basicQos(0,1,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        init();
    }

    public void init(){

    }

    public void close(){
        //最后关闭channel
        if(channel != null){
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //最后关闭conn
        if(conn != null){
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
