package org.study.chp6.exception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SecondExceptionChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.writeBytes(" second handler ".getBytes());
        ctx.fireChannelRead(buf);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(
                new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(!future.isSuccess()) {
                            System.out.println("Second Exception");
                            future.cause().printStackTrace();
                            ctx.writeAndFlush(Unpooled.copiedBuffer("Outbound Exception Occur!", StandardCharsets.UTF_8));
                        }
                        else{
                            ctx.write(msg);
                        }
                    }
                }
        );
        throw new IOException("Outbound Exception!");
    }
}