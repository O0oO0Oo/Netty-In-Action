package org.study.chp6.exception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.nio.charset.StandardCharsets;

public class FirstExceptionChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Active.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.writeBytes(" first handler ".getBytes());
        ctx.fireChannelRead(buf);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.writeBytes(" first.".getBytes());
        ctx.writeAndFlush(
                msg
        );
    }
}