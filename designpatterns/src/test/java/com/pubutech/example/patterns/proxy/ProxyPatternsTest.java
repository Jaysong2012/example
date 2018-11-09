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

package com.pubutech.example.patterns.proxy;

import com.alibaba.fastjson.JSONObject;
import com.pubutech.example.patterns.factory.ComputerParams;
import com.pubutech.example.patterns.ioc.bean.CPU;
import com.pubutech.example.patterns.ioc.bean.Computer;
import com.pubutech.example.patterns.ioc.di.Chassis;
//import com.pubutech.example.patterns.ioc.di.Computer;
import com.pubutech.example.patterns.ioc.di.MainBoard;
import com.pubutech.example.patterns.proxy.cglibproxy.CglibProxy;
import com.pubutech.example.patterns.proxy.common.ISpeech;
import com.pubutech.example.patterns.proxy.common.Speecher;
import com.pubutech.example.patterns.proxy.dynamicproxy.DynamicProxyHandler;
import com.pubutech.example.patterns.proxy.staticproxy.SpeecherProxy;
import org.junit.Test;
import sun.applet.Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class ProxyPatternsTest {


    //@Test
    public void test() throws Exception{

        /*int cpuCore = 8;
        CPU cpu = new CPU(8);
        MainBoard mainBoard = new MainBoard(cpu);
        Chassis chassis = new Chassis(mainBoard);
        Computer computer = new Computer(chassis);*/
        //Computer computer = new Computer();

//        Speecher speecher = new Speecher();
//        SpeecherProxy staticProxy = new SpeecherProxy();
//        staticProxy.setSpeecher(speecher);
//        staticProxy.speech();


//        //获取字节码对象
//        Class<Speecher> clazz =  (Class<Speecher>) Class.forName("com.pubutech.example.patterns.proxy.common.Speecher");
//        //获取一个对象
//        Constructor con =  clazz.getConstructor();
//        Speecher speecher = (Speecher) con.newInstance();
//
//        Class<ISpeech>  interfaces = (Class<ISpeech>) Class.forName("com.pubutech.example.patterns.proxy.common.ISpeech");
//        Method[] methods = interfaces.getMethods();
//        for (Method i :methods) {
//            //Before Speech
//            System.out.println("Hello everyone,I am "+speecher.getName());
//            try{
//                //获取Method对象
//                //调用invoke方法来调用
//                i.invoke(speecher);
//            }catch (Exception e){
//                //Around Speech
//                System.out.println("Exception happened, proxy handle ");
//            }
//
//            //After Speech
//            System.out.println("Thanks ,I am "+speecher.getName()+" goodbye !!!");
//
//        }

//        Speecher speecher = new Speecher();
//        ISpeech speech = (ISpeech) Proxy.newProxyInstance(ISpeech.class.getClassLoader(), new
//               Class[]{ISpeech.class}, new DynamicProxyHandler(speecher));
//        speech.speech();
//
//        speech.speak();

//        Speecher speecher = new Speecher();
//        CglibProxy cglibProxy = new CglibProxy();
//        Speecher speecherCglibProxy = (Speecher) cglibProxy.getInstance(speecher);
//        speecherCglibProxy.dance();

        /*
        //先让客户填写组装清单
        ComputerParams params = new ComputerParams.ComputerParamsBuilder()
                //填写内存大小
                .setMemorySize(16)
                //填写核心数
                .setCpuCore(8)
                //填写硬盘大小
                .setHardDiskSize(2000)
                //选择主板
                .setMainBoardBrand("dell")
                //选择机箱
                .setChassisBrand("dell")
                .build();

        //电脑生产先依据客户的清单配置我们的电脑
        Computer computer = new Computer.Builder(params)
                .buildMemory()
                .buildCpu()
                .buildHardDisk()
                .buildMainBoard()
                .buildChassis()
                .build();
         */

    }

}
