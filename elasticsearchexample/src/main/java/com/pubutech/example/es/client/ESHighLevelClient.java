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

package com.pubutech.example.es.client;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/1
 * @since 1.0
 */
@Data
public class ESHighLevelClient {

    private RestHighLevelClient client;

    public ESHighLevelClient(String host,int port,String scheme){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, port, scheme)));
    }

    public void close(){
        //关闭连接
        if(client !=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
