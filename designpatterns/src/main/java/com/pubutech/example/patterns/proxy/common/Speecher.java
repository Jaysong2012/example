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

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/2
 * @since 1.0
 */
//演讲者实现了演讲的能力（接口）
public class Speecher extends Person implements ISpeech{

    @Override
    public void speech() {
        System.out.println("进行一场演讲");
    }

    @Override
    public void talk() {
        System.out.println("进行一场脱口秀");
    }

    @Override
    public void speak() {
        System.out.println("说话");
    }


}
