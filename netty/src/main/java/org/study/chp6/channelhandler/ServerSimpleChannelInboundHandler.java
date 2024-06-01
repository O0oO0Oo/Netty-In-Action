package org.study.chp6.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerSimpleChannelInboundHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
