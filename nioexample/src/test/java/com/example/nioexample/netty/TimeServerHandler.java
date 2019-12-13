package com.example.nioexample.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author songchao
 * @version 1.0
 * @created 2019-12-13
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception{
        String body = (String)object;

        System.out.println("Server receive order:"+body);

        ByteBuf resp = Unpooled.copiedBuffer(("request Success" + System.getProperty("line.separator")).getBytes());

        channelHandlerContext.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext,Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }

}

