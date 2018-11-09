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

package com.pubutech.example.aop.interfacetest;

import com.pubutech.example.aop.args.Message;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/4
 * @since 1.0
 */
public class BusinessImpl implements IBussiness {

    @Override
    public String doSomething() {
        System.out.println("执行业务操作");
        return "success";
    }

    public void doException(Message msg){
        char a = msg.getMessage().charAt(10);
        System.out.println("让他出现异常，这里走不到");
    }

}
