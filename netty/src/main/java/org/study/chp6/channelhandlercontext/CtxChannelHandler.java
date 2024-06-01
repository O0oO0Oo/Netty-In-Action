package org.study.chp6.channelhandlercontext;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

import java.nio.charset.StandardCharsets;

public class CtxChannelHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        channel.write(
                msg
        );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channel.write(
                Unpooled.copiedBuffer("Netty in Action - channel", StandardCharsets.UTF_8)
        );

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.write(
                Unpooled.copiedBuffer("Netty in Action - pipeline", StandardCharsets.UTF_8)
        );
    }
}
