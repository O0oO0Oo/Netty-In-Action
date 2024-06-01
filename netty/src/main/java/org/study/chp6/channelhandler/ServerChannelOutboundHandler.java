package org.study.chp6.channelhandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;

@ChannelHandler.Sharable
public class ServerChannelOutboundHandler extends ChannelHandlerAdapter {

    @Override
    public boolean isSharable() {
        return super.isSharable();
    }
}
