package org.study.chp8.datagram;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public class BootstrapDatagramServer {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received Data.");
                    }
                });

        // connectionless protocol use "bind()" method
        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(5001));
    }
}
