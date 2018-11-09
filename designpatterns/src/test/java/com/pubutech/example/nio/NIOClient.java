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

package com.pubutech.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/11/2
 * @since 1.0
 */
public class NIOClient implements Runnable {

    private String tag = "client";
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    private boolean isConnected = false;
    private int bufferSize = 1024;

    public NIOClient(String tag){
        this.tag = tag;
        readBuffer = ByteBuffer.allocate(bufferSize);
        writeBuffer = ByteBuffer.allocate(bufferSize);
    }

    @Override
    public void run() {

        try {

            Selector selector = Selector.open();

            SocketChannel socketChannel  = SocketChannel.open();

            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 5500));
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ|SelectionKey.OP_WRITE).attach(tag);

            System.out.println(tag+" connect .... port:5500");

            while(true) {
                int readyChannels = selector.select();
                if(readyChannels == 0) continue;
                Set selectedKeys = selector.selectedKeys();
                Iterator keyIterator = selectedKeys.iterator();
                while(keyIterator.hasNext()) {

                    SelectionKey key = (SelectionKey)keyIterator.next();
                    // 判断是哪个事件
                    if(key.isAcceptable()) {// 客户请求连接
                        System.out.println(key.attachment()+ "is accepted by server");

                    } else if (key.isConnectable()) {
                        System.out.println(key.attachment()+ " connect ");
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            try {
                                /*
                                 * 人为将程序中止，等待连接创建完成,否则会报如下错误
                                 * java.nio.channels.NotYetConnectedException at sun.nio.ch.SocketChannelImpl.ensureWriteOpen(SocketChannelImpl.java:274)
                                 */
                                if (channel.finishConnect()) {
                                    System.out.println(key.attachment()+" connect server succ");
                                    isConnected = true;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (key.isReadable()) {
                        System.out.println(key.attachment()+ " read ");

                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        try {
                            int len = clientChannel.read(readBuffer);
                            if (len == -1) {
                                System.out.println(key.attachment()+" read : len=-1");
                                // 说明连接已经断开
                                selector.close();
                                socketChannel.close();
                            } else {
                                readBuffer.flip();
                                byte[] buffer = new byte[len];
                                readBuffer.get(buffer);
                                readBuffer.clear();
                                System.out.println(key.attachment()+" read : len=" + len + ", str=" + new String(buffer));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (key.isWritable()) {
                        System.out.println(key.attachment()+ " write ");
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        String str = "helloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshellos" +
                                "helloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshellos" +
                                "helloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshellos" +
                                "helloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshellos" +
                                "helloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshelloshellos" +
                                "helloshelloshelloshelloshelloshelloshelloshellos";
                        byte[] buffer = str.getBytes();
                        writeBuffer.put(buffer);
                        writeBuffer.flip();
                        try {
                            System.out.println(key.attachment()+ " write : len=" + buffer.length + ", str=" + str);
                            clientChannel.write(writeBuffer);

                            try {
                                Thread.sleep(100);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        writeBuffer.clear();
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
