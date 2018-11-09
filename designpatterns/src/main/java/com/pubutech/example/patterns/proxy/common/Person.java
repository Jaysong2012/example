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

package com.pubutech.example.patterns.proxy.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/2
 * @since 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
//定义一个人类
public class Person implements Serializable {

    private String name;

    private String gender;

    private int age;

    public void dance(){
        System.out.println("Let us dance");
    }

}
