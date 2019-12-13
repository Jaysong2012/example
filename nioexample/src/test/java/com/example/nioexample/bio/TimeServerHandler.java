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
public class TimeServerHandler implements Runnable{

    private Socket socket;

    public TimeServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);

            String body = null;
            while (true){
                body = in.readLine();
                if(null == body){
                    break;
                }

                System.out.println(System.currentTimeMillis()+" 线程 "+Thread.currentThread().getName()+"Server receive msg :"+body);
                out.println("Request Success");

            }

        }catch (Exception e){
            e.printStackTrace();
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

            if(this.socket != null){
                try {
                    this.socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.socket = null;
            }
        }
    }


}
