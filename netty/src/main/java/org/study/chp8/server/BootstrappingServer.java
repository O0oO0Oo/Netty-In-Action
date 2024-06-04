package org.study.chp8.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class BootstrappingServer {
    public static void main(String[] args) throws Exception {

        try (
                NioEventLoopGroup boss = new NioEventLoopGroup(1);
                NioEventLoopGroup worker = new NioEventLoopGroup();
        ) {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            System.out.println("Recevied data.");
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress("localhost", 5001));
            channelFuture.addListener(
                    (ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            System.out.println("Connection succeed!");
                        } else {
                            System.out.println("Connection failed.");
                            future.cause().printStackTrace();
                        }
                    }
            );
        }

    }
}
