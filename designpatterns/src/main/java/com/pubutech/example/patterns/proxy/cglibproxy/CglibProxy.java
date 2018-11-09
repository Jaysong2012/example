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

package com.pubutech.example.patterns.proxy.cglibproxy;

import com.pubutech.example.patterns.proxy.common.Speecher;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class CglibProxy implements MethodInterceptor {

    private Object object;

    public Object getInstance(final Object object) {
        this.object = object;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.object.getClass());
        enhancer.setCallback(this);
       return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        //Before Talk
        System.out.println("Hello everyone,I am "+((Speecher)this.object).getName());
        try{
            result = methodProxy.invoke(object,objects);
        }catch (Exception e){
            //Around Talk
            System.out.println("Exception happened, proxy handle ");
        }

        //After Talk
        System.out.println("Thanks ,I am "+((Speecher)this.object).getName()+" goodbye !!!");

        return result;
    }
}
