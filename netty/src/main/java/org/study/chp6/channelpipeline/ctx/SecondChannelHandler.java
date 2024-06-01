package org.study.chp6.channelpipeline.ctx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SecondChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        buf.writeBytes(" second handler ".getBytes());
        ctx.fireChannelRead(buf);
    }
}
