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

package com.pubutech.example.aop.test;

import com.pubutech.example.aop.thinking.TestController;
import com.pubutech.example.aop.thinking.TestMapper;
import com.pubutech.example.aop.thinking.TestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AopTest.class)
public class AopTest {

    @Test
    public void test(){
        TestController testController = new TestController();
        TestServiceImpl serviceImpl = new TestServiceImpl();
        testController.setServiceImpl(serviceImpl);
        TestMapper mapper = new TestMapper();
        serviceImpl.setTestMapper(mapper);

        testController.request1();
        //testController.request2();
    }


}
