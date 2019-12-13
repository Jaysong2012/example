package com.example.nioexample.nio;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author songchao
 * @version 1.0
 * @created 2019-12-06
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100));
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new TimeClientHandle("127.0.0.1",port));
        }
        System.out.println("ssss");

        try{
            while(true){
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("lllll");
    }

}
