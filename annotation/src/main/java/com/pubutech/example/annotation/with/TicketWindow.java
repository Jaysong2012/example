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

package com.pubutech.example.annotation.with;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/3
 * @since 1.0
 */
public class TicketWindow {

    public void sell(Human human){

        String type = human.getClass().getAnnotation(Certificate.class).type();
        switch (type){
            case "normal":
                System.out.println("成人票");
                return;
            case "child":
                System.out.println("儿童票");
                return;
            case "student":
                System.out.println("学生票");
                return;
            default:
                System.out.println("成人票");
                return;
        }
    }
}
