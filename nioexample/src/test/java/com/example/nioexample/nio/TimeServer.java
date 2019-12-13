package com.example.nioexample.nio;

/**
 * @author songchao
 * @version 1.0
 * @created 2019-12-06
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        MultiplexerTimerServer multiplexerTimerServer = new MultiplexerTimerServer(port);
        new Thread(multiplexerTimerServer,"NIO-MultiplexerTimerServer-001").start();

        System.out.println("sssss");

        try{
            while(true){
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
