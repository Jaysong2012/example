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

package com.pubutech.example.rabbitmq.runner;

import com.pubutech.example.rabbitmq.amqp.RabbitMQChannel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/13
 * @since 1.0
 */
@Slf4j
@Component
public class QueenRunner implements ApplicationRunner {

    @Autowired
    private RabbitMQChannel rabbitMQChannel;

    SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Channel channel = rabbitMQChannel.getChannel();
        try {
            channel.confirmSelect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channel.addConfirmListener(new ConfirmListener() {

            //处理消息成功回执
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.info("Ack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }

            //处理失败回执
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }

        });

        // 查看下一个要发送的消息的序号
        long nextSeqNo = channel.getNextPublishSeqNo();
        try {
            channel.basicPublish("first-delay-exchange", "first-delay", MessageProperties.PERSISTENT_TEXT_PLAIN, "test runeer info".getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("send first message nextSeqNo :{}",nextSeqNo);
        confirmSet.add(nextSeqNo);

    }


}
