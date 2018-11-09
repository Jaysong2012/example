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

import com.pubutech.example.nio.NIOClient;
import com.pubutech.example.nio.NIOServer;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/2
 * @since 1.0
 */
public class IOTest {

    //@Test
    public void test(){
        byte[] buffer=new byte[1024];   //一次取出的字节数大小,缓冲区大小
        int numberRead=0;
        FileInputStream input=null;
        FileOutputStream out =null;
        try {
            input=new FileInputStream("/data/file/temp/1.jpg");
            out=new FileOutputStream("/data/file/temp/2.jpg"); //如果文件不存在会自动创建

            while ((numberRead=input.read(buffer))!=-1) {  //numberRead的目的在于防止最后一次读取的字节小于buffer长度，
                out.write(buffer, 0, numberRead);       //否则会自动被填充0
            }
        } catch (IOException e) {
            // TODO自动生成的 catch 块
            e.printStackTrace();
        }finally{
            try {
                input.close();
                out.close();
            } catch (IOException e) {
                // TODO自动生成的 catch 块
                e.printStackTrace();
            }

        }
    }

    public void test2(){

        //读取的字符数
        char chars[] = new char[1024];

        FileReader freader = null;
        FileWriter fwriter = null;

        try {

            freader = new FileReader("/data/file/temp/1.text");
            File f1 = new File("/data/file/temp/2.txt");
            if(!f1.exists()){
                f1.createNewFile();
            }
            fwriter = new FileWriter(f1);

            while((freader.read(chars))!= -1)
            {
                fwriter.write(chars);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                freader.close();
                fwriter.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //@Test
    public void test3(){
//        InputStream input = null;
//        try {
//            input = new FileInputStream("/data/file/temp/test.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//        try {
//            String nameLine   = reader.readLine();
//            String ageLine    = reader.readLine();
//            String emailLine  = reader.readLine();
//            String phoneLine  = reader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("/data/file/temp/test2.txt", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChannel inChannel = aFile.getChannel();

        //建造一块20480字节的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(20480);

        //写入文件信息到缓冲区
        int bytesRead = 0;
        try {
            bytesRead = inChannel.read(buf);

            System.out.println(bytesRead);
            while (bytesRead != -1) {
                //转为读取模式
                buf.flip();
                while(buf.hasRemaining()){
                    // 一次读取一个字节
                    System.out.print((char) buf.getChar());
                }
                //清空缓冲区
                buf.clear();
                //继续写入文件信息到缓冲区
                bytesRead = inChannel.read(buf);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    @Test
    public void server(){
        Thread server = new Thread(new NIOServer());
        server.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread client1 = new Thread(new NIOClient("client1"));
        client1.start();

        Thread client2 = new Thread(new NIOClient("client2"));
        client2.start();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        client1.stop();
        client2.stop();

        server.stop();


        while (true){
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //@Test
    public void client1(){

        int i =0;
        Socket socket= null;//BIO 阻塞
        try {
            socket = new Socket("127.0.0.1",5500);
            System.out.println("连接成功");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.write("hi");
            printWriter.flush();
            printWriter.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Test
    public void client2(){


        Socket socket= null;//BIO 阻塞
        try {
            socket = new Socket("127.0.0.1",5500);
            System.out.println("连接成功");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.write("hi");
            printWriter.flush();

            printWriter.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
