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

package com.pubutech.example.rabbitmq.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/13
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
@EqualsAndHashCode(callSuper = false)
@Order(1)
public class RabbitMQProperties {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String vhost;

    private Integer firstdelaytimeout;

    private Integer seconddelaytimeout;

}
