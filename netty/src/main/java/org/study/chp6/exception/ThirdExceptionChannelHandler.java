package org.study.chp6.exception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.nio.charset.StandardCharsets;

public class ThirdExceptionChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.writeBytes(" third handler ".getBytes());

        System.out.print("Server Read : ");
        while (buf.isReadable()) {
            System.out.print((char) buf.readByte());
        }
        System.out.println(" Done");

        ctx.write(Unpooled.copiedBuffer("Hello third,", StandardCharsets.UTF_8));
        buf.release();
    }

    // inbound exception handler
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
