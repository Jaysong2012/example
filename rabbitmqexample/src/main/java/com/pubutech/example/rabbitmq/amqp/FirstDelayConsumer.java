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

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/13
 * @since 1.0
 */
@Slf4j
@Component
public class FirstDelayConsumer extends DefaultConsumer {

    @Autowired
    private RabbitMQChannel rabbitMQChannel;

    public FirstDelayConsumer(RabbitMQChannel rabbitMQChannel) {
        super(rabbitMQChannel.getChannel());
        this.rabbitMQChannel = rabbitMQChannel;
        try {
            rabbitMQChannel.getChannel().basicConsume("first-delay-dead-queen",false, "first-delay-dead-consumer-tag",this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {

        long deliveryTag = envelope.getDeliveryTag();
        // positively acknowledge all deliveries up to
        // this delivery tag
        rabbitMQChannel.getChannel().basicAck(deliveryTag, true);

        log.info("consumerTag : {}  deliveryTag : {} message {} ",consumerTag,deliveryTag,new String(body,"utf-8"));
    }
}
