package com.example.nioexample.nio;

import org.springframework.util.StringUtils;

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
 * @author songchao
 * @version 1.0
 * @created 2019-12-06
 */
public class MultiplexerTimerServer implements Runnable {

    private Selector acceptSelector;

    private Selector clientSelector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public MultiplexerTimerServer(int port){
        try {
            acceptSelector = Selector.open();
            clientSelector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind( new InetSocketAddress(port),1024);
            serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
            System.out.println("Server starts in port :"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean getStop() {
        return stop;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                acceptSelector.select(1000);
                Set<SelectionKey> selectionKeySet = acceptSelector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                SelectionKey selectionKey = null;
                while (it.hasNext()){
                    selectionKey = it.next();
                    try {
                        handleSelectionKey(selectionKey);
                    }catch (Exception e1){
                        if(null != selectionKey){
                            selectionKey.cancel();
                            if(null != selectionKey.channel()){
                                selectionKey.channel().close();
                            }
                        }
                    }
                    it.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                clientSelector.select(1000);
                Set<SelectionKey> selectionKeySet = clientSelector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                SelectionKey selectionKey = null;
                while (it.hasNext()){
                    selectionKey = it.next();
                    try {
                        handleSelectionKey(selectionKey);
                    }catch (Exception e1){
                        if(null != selectionKey){
                            selectionKey.cancel();
                            if(null != selectionKey.channel()){
                                selectionKey.channel().close();
                            }
                        }
                    }
                    it.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(null != acceptSelector){
            try {
                acceptSelector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(null != clientSelector){
            try {
                clientSelector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleSelectionKey(SelectionKey key) throws IOException{
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel clientSocketChannel = serverSocketChannel.accept();
                clientSocketChannel.configureBlocking(false);
                clientSocketChannel.register(clientSelector,SelectionKey.OP_READ);
                return;
            }

            if(key.isReadable()){
                SocketChannel clientSocketChannel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = clientSocketChannel.read(readBuffer);
                if(readBytes >0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("Server receive content "+body);
                    String curentTime = System.currentTimeMillis() +"";
                    doRespone(clientSocketChannel,curentTime);
                    return ;
                }

                if(readBytes < 0){
                    // 对端链路关闭
                    key.cancel();
                    clientSocketChannel.close();
                    return;
                }

                // 啥内容都没有收到，无视他
            }
        }
    }

    private void doRespone(SocketChannel channel, String content) throws IOException {
        if(!StringUtils.isEmpty(content)){
            byte[] bytes = content.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            System.out.println("Server write 2 Client "+content);
        }
    }

}
