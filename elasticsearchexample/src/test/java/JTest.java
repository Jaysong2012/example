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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/10/24
 * @since 1.0
 */
public class JTest {

    //@Test
    public void test(){
        //创建连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.199.18", 9200, "http"),
                        new HttpHost("192.168.199.118", 9200, "http")));


        //创建请求
        IndexRequest request = new IndexRequest(
                "posts", // Index
                "doc",  // Type
                "1");   // Document id

        //构建请求体
        JavaBean bean = new JavaBean();

        ObjectMapper objectMapper = new ObjectMapper();//create once reuse
        // generate json
        String json = null;
        try {
            json = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(json);

        request.source(json, XContentType.JSON);

        //设置操作类型为DocWriteRequest.OpType
        request.opType(DocWriteRequest.OpType.CREATE);


        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            //调用成功时回调，返回信息作为参数传入
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("异步回调成功");
                String index = indexResponse.getIndex();
                String type = indexResponse.getType();
                String id = indexResponse.getId();
                long version = indexResponse.getVersion();
                //文档创建成功操作
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println(index+ type+ id+ version);
                    //文档更新成功操作
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

                }
            }

            //调用失败时回调，错误信息作为参数传入
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };

        System.out.println("开始");
        //异步操作
        //client.indexAsync(request/*需要执行的IndexRequest*/, listener/*执行完成之后的回调*/);

        try {
            client.index(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭连接
        if(client !=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //@Test
    public void esTest(){
        //创建连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("es.test.maimob.net", 80, "http")));

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
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                String sourceAsString = hit.getSourceAsString();
                System.out.println(sourceAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭连接
        if(client !=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //@Test
    public void finalTest(){
        //创建连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("es.test.maimob.net", 80, "http")));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("cell_location");
        searchRequest.types("cell_location");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("body.deviceInfo.android_id.keyword","f91dac31c2e20f7a"))
                .filter(QueryBuilders.existsQuery("body.deviceInfo.imei.keyword"))
                .filter(QueryBuilders.existsQuery("body.deviceInfo.imsi.keyword"))
                .filter(QueryBuilders.existsQuery("body.cell_location.cells"))
                .filter(QueryBuilders.existsQuery("body.cell_location.loc"))
                .filter(QueryBuilders.termQuery("body.cell_location.loc.type.keyword","gsm"))
        );

        searchRequest.source(searchSourceBuilder);

        String sourceAsString1;
        try {
            SearchResponse searchResponse = client.search(searchRequest);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                sourceAsString1 = hit.getSourceAsString();
                System.out.println(sourceAsString1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭连接
        if(client !=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //@Test
    public void finalTest2(){
        //创建连接
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("47.97.20.178", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("catch_deviceinfo");
        searchRequest.types("catch_deviceinfo");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(
                QueryBuilders.termQuery("body.android_id.keyword","f91dac31c2e20f7a")));

        searchRequest.source(searchSourceBuilder);

        String sourceAsString1;
        try {
            SearchResponse searchResponse = client.search(searchRequest);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                sourceAsString1 = hit.getSourceAsString();
                System.out.println(sourceAsString1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭连接
        if(client !=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2(){
        System.out.println(4 >>1);

        System.out.println(Arrays.asList(1,2,3).stream().filter(item -> item % 2 == 0).findFirst().get());
    }
}


@Data
class JavaBean{
    private String name;
    private int age;
    private long timestamp;
    private ArrayList<Object> list;

    public JavaBean(){
        this.name = "hello";
        this.age = 18;
        this.timestamp = System.currentTimeMillis();
    }
}
