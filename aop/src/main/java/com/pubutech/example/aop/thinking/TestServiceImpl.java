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

package com.pubutech.example.aop.thinking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class TestServiceImpl {

    @Autowired
    private TestMapper testMapper;

    public void setTestMapper(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    public void service1(){
        System.out.println("....Service层处理第一个请求逻辑");
        System.out.println("               |            ");
        System.out.println("               V            ");
        testMapper.db1();
        System.out.println("               |            ");
        System.out.println("               V            ");
        System.out.println("....Service层返回第一个请求逻辑数据");
        donothing();
    }

    public void service2(){
        System.out.println("....Service层处理第二个请求逻辑");
        System.out.println("               |            ");
        System.out.println("               V            ");
        testMapper.db2();
        System.out.println("               |            ");
        System.out.println("               V            ");
        System.out.println("....Service层返回第一个请求逻辑数据");
    }

    private void donothing(){
        System.out.println("....Service层donothing");
    }

}
