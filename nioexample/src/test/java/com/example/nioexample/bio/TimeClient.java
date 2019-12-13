package com.example.nioexample.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author songchao
 * @version 1.0
 * @created 2019-12-12
 */
public class TimeClient {

    public static void main(String[] args) {

        Socket socket = null;
        BufferedReader in  =null;
        PrintWriter out = null;
        try{
            System.out.println(System.currentTimeMillis()+"客户端准备开始链接");
            socket = new Socket("127.0.0.1",8080);
            Thread.sleep(1000);
            System.out.println(System.currentTimeMillis()+"客户端睡一秒钟链接看看连接状态");

            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println("第一条指令");
            System.out.println("客户端发送第一条指令");
            String resp  = in.readLine();
            System.out.println("客户端第一次接收响应"+resp);


            out.println("第二条指令");
            System.out.println("客户端发送第二条指令");
            String resp1  = in.readLine();
            System.out.println("客户端第二次接收响应"+resp1);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                in = null;
            }

            if(out != null){
                out.close();
                out = null;
            }

            if(socket != null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
