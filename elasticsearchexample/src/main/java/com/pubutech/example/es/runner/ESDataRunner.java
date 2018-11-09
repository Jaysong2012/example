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

package com.pubutech.example.es.runner;

import com.pubutech.example.es.client.ESHighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/1
 * @since 1.0
 */
@Component
public class ESDataRunner implements ApplicationRunner {

    @Autowired
    private ESHighLevelClient deviceInfoClient;

    @Autowired
    private ESHighLevelClient cellLocationClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Start");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("cell_location");
        searchRequest.types("cell_location");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.existsQuery("body.deviceInfo.android_id.keyword"))
                .filter(QueryBuilders.existsQuery("body.deviceInfo.imei.keyword"))
                .filter(QueryBuilders.existsQuery("body.deviceInfo.imsi.keyword"))
                .filter(QueryBuilders.existsQuery("body.cell_location.cells"))
                .filter(QueryBuilders.existsQuery("body.cell_location.loc"))
                .filter(QueryBuilders.termQuery("body.cell_location.loc.type.keyword","gsm"))
        );

        searchSourceBuilder.size(500);
        searchRequest.source(searchSourceBuilder);


        String celllocation;
        String android_id;
        String deviceinfo;
        String result ;

        SearchHit hit;
        try {
            SearchResponse searchResponse = cellLocationClient.getClient().search(searchRequest);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (int i=0;i< searchHits.length;i++) {

                hit = searchHits[i];
                celllocation = hit.getSourceAsString();
                //Map<String, Object> map = hit.getSourceAsMap();
                System.out.println(celllocation);
                android_id = (String)(((Map<String, Object>)((Map<String, Object>)(hit.getSourceAsMap().get("body"))).get("deviceInfo"))).get("android_id");
                System.out.println(android_id);
                if(android_id ==null || "".equals(android_id))continue;
                deviceinfo = getDeviceInfo(android_id);
                if(deviceinfo ==null || "".equals(deviceinfo))continue;
                result = "{"+"\"device_info\":"+deviceinfo+","+"\"cell_location\":"+celllocation+"}";

                System.out.println(result);
                writeFile(result,"/data/file/json/"+"info"+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        deviceInfoClient.close();
        cellLocationClient.close();

    }

    public String getDeviceInfo(String android_id){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("catch_deviceinfo");
        searchRequest.types("catch_deviceinfo");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("body.android_id.keyword",android_id))
        );
        searchSourceBuilder.size(1);

        searchRequest.source(searchSourceBuilder);

        String deviceinfo = "";
        try {
            SearchResponse searchResponse = deviceInfoClient.getClient().search(searchRequest);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                deviceinfo = hit.getSourceAsString();
                System.out.println(deviceinfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deviceinfo;
    }

    private void writeFile(String str,String path){
        File f=new File(path);//新建一个文件对象
        FileWriter fw;
        try {
            fw=new FileWriter(f);//新建一个FileWriter
            fw.write(str);//将字符串写入到指定的路径下的文件中
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
