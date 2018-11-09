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

package com.pubutech.example.patterns.proxy.dynamicproxy;

import com.pubutech.example.patterns.proxy.common.Speecher;
import com.pubutech.example.patterns.proxy.dynamicproxy.annotation.Enhance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class DynamicProxyHandler implements InvocationHandler {

    private Speecher speecher;

    public DynamicProxyHandler(final Speecher speecher) {
        this.speecher = speecher;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        if("speak".equals(method.getName())){
//            return method.invoke(speecher, args);
//        }
        // 看看接口中方法是否标注了需要 Enhance
        boolean b = method.isAnnotationPresent(Enhance.class);
        if(!b){
            // 没有标注的话，按原方法执行
            return method.invoke(speecher, args);
        }
        Object result = null;
        //Before Talk
        System.out.println("Hello everyone,I am "+this.speecher.getName());
        try{
            result = method.invoke(speecher, args);
        }catch (Exception e){
            //Around Talk
            System.out.println("Exception happened, proxy handle ");
        }

        //After Talk
        System.out.println("Thanks ,I am "+this.speecher.getName()+" goodbye !!!");

        return result;
    }
}
