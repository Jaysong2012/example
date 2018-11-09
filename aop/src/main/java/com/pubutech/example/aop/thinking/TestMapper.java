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
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class TestMapper {

    public void db1(){
        System.out.println("........Mapper执行第一请求数据库操作");
        System.out.println("........Mapper返回第一请求数据库操作");
    }

    public void db2(){
        System.out.println("........Mapper执行第一请求数据库操作");
        System.out.println("........Mapper返回第一请求数据库操作");
    }
}
