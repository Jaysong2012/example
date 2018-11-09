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

package com.pubutech.example.aop.runner;

import com.alibaba.fastjson.JSONObject;
import com.pubutech.example.aop.args.Message;
import com.pubutech.example.aop.interfacetest.BusinessImpl;
import com.pubutech.example.aop.interfacetest.Dancer;
import com.pubutech.example.aop.interfacetest.ISkill;
import com.pubutech.example.aop.thinking.TestServiceImpl;
import com.pubutech.example.patterns.factory.Computer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/4
 * @since 1.0
 */
@Component
public class AOPApplicationRunner implements ApplicationRunner {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @Autowired
    private BusinessImpl businessImpl;

    @Autowired
    private Dancer dancer;

    @Autowired
    private Computer.Builder computerBuilder;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        //testServiceImpl.service1();
        //businessImpl.doSomething();

//        Message msg = new Message();
//        msg.setMessage("xxxx");
//        try {
//            businessImpl.doException(msg);
//        }catch (Exception e){
//            //throw e;
//        }

//        dancer.dance();
//        ISkill skill = (ISkill)dancer;
//        ((ISkill) dancer).coding();


        try {

            Thread.sleep(1000);
            computerBuilder.buildMemory()
                    .buildCpu()
                    .buildHardDisk()
                    .buildMainBoard()
                    .buildChassis()
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
