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

import java.net.InetAddress;
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
public class NIOServer implements Runnable{

    private int clientNum = 0;
    private int bufferSize = 128;

    @Override
    public void run() {

        try {
            // 创建通道和选择器
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 5500);
            serverSocketChannel.socket().bind(inetSocketAddress);
            // 设置通道非阻塞 绑定选择器
            serverSocketChannel.configureBlocking(false);
            /*
             * ServerSocketChannel只有accept
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT).attach("server");
            System.out.println("Server started .... port:5500");

            while(true) {
                int readyChannels = selector.select();
                if(readyChannels == 0) continue;
                Set selectedKeys = selector.selectedKeys();
                Iterator keyIterator = selectedKeys.iterator();
                while(keyIterator.hasNext()) {

                    SelectionKey key = (SelectionKey)keyIterator.next();
                    // 判断是哪个事件
                    if(key.isAcceptable()) {// 客户请求连接
                        System.out.println(key.attachment()+ " - 接受请求事件");
                        ++clientNum;
                        // 获取通道 接受连接,
                        // 设置非阻塞模式（必须），同时需要注册 读写数据的事件，这样有消息触发时才能捕获
                        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();

                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ).attach("accept"+clientNum);

                        System.out.println(key.attachment() + " - 已连接");
                    } else if (key.isConnectable()) {
                        System.out.println(key.attachment()+ " - 连接事件");
                    } else if (key.isReadable()) {
                        System.out.println(key.attachment()+ " - 读数据事件");
                        SocketChannel clientChannel=(SocketChannel)key.channel();
                        ByteBuffer receiveBuf = ByteBuffer.allocate(bufferSize);
                        int length = clientChannel.read(receiveBuf);
                        System.out.println(key.attachment()+ "length = "+length+" - 读取数据：" + getString(receiveBuf));
                    } else if (key.isWritable()) {
                        System.out.println(key.attachment()+ " - 写数据事件");
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer sendBuf = ByteBuffer.allocate(bufferSize);
                        String sendText = "helloc";
                        sendBuf.put(sendText.getBytes());
                        sendBuf.flip();        //写完数据后调用此方法
                        clientChannel.write(sendBuf);
                    }
                    keyIterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getString(ByteBuffer buffer){
        StringBuffer builder = new StringBuffer("");
        try{
            for(int i = 0; i<buffer.position();i++){
                builder.append((char)buffer.get(i));
            }
            return builder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            return builder.toString();
        }
    }
}
