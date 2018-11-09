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

package com.pubutech.example.es.config;

import com.pubutech.example.es.client.ESHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/1
 * @since 1.0
 */
@Configuration
public class ClientConfig {

    @Bean
    public ESHighLevelClient deviceInfoClient(){
        return new ESHighLevelClient("47.97.20.178", 9200, "http");
    }

    @Bean
    public ESHighLevelClient cellLocationClient(){
        return new ESHighLevelClient("es.test.maimob.net", 80, "http");
    }

}
