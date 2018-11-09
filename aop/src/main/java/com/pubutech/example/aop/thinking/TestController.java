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

/**
 * 模拟SpringMVC Controller 层
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class TestController {

    private TestServiceImpl serviceImpl;

    public void setServiceImpl(TestServiceImpl serviceImpl){
        this.serviceImpl = serviceImpl;
    }

    public void request1(){
        System.out.println("Controller接收第一个接口的请求数据");
        System.out.println("             |                 ");
        System.out.println("             V                 ");
        serviceImpl.service1();
        System.out.println("             |                 ");
        System.out.println("             V                 ");
        System.out.println("Controller返回第一个接口的请求数据");
    }

    public void request2(){
        System.out.println("Controller接收第二个接口的请求数据");
        System.out.println("             |                 ");
        System.out.println("             V                 ");
        serviceImpl.service2();
        System.out.println("             |                 ");
        System.out.println("             V                 ");
        System.out.println("Controller返回第二个接口的请求数据");
    }



}
