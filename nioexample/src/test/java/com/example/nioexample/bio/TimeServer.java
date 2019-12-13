package com.example.nioexample.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author songchao
 * @version 1.0
 * @created 2019-12-12
 */
public class TimeServer {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),50,120L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println(System.currentTimeMillis()+"Server Start at 8080");
            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                System.out.println(System.currentTimeMillis()+"接收连接时间");
                threadPoolExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }

    }
}
