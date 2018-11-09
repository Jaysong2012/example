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

package com.pubutech.example.patterns.proxy.staticproxy;

import com.pubutech.example.patterns.proxy.common.ISpeech;
import com.pubutech.example.patterns.proxy.common.Speecher;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/2
 * @since 1.0
 */
//此时代理模式的开闭原则（对扩展开放，对修改关闭）便体现出了它的用处
public class SpeecherProxy implements ISpeech {

    private Speecher speecher;

    public void setSpeecher(Speecher speecher){
        this.speecher = speecher;
    }

    @Override
    public void speech() {
       //Before Speech
       System.out.println("Hello everyone,I am "+this.speecher.getName());
       try{
           this.speecher.speech();
       }catch (Exception e){
           //Around Speech
           System.out.println("Exception happened, proxy handle ");
       }

       //After Speech
        System.out.println("Thanks ,I am "+this.speecher.getName()+" goodbye !!!");
    }

    @Override
    public void talk() {
        //Before Talk
        System.out.println("Hello everyone,I am "+this.speecher.getName());
        try{
            this.speecher.speech();
        }catch (Exception e){
            //Around Talk
            System.out.println("Exception happened, proxy handle ");
        }

        //After Talk
        System.out.println("Thanks ,I am "+this.speecher.getName()+" goodbye !!!");
    }

    @Override
    public void speak() {
        System.out.println("说话");
    }

}
