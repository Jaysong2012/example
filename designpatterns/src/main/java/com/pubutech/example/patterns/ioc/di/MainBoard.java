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

package com.pubutech.example.patterns.ioc.di;

import com.pubutech.example.patterns.ioc.bean.CPU;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/14
 * @since 1.0
 */
public class MainBoard {
    private CPU cpu;

    public MainBoard(CPU cpu){
        this.cpu = cpu;
    }

}
