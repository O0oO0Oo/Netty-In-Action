package org.study.chp8.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class BootstrappingClient {
    public static void main(String[] args) throws Exception {
        try (
                NioEventLoopGroup group = new NioEventLoopGroup();
        ) {
            /**
             * A Bootstrap that makes it easy to bootstrap a Channel to use for clients.
             * The bind() methods are useful in combination with connectionless transports such as datagram (UDP).
             * For regular TCP connections, please use the provided connect() methods
             */
            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            System.out.println("Received message.");
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(
                    new InetSocketAddress("localhost", 5001)
            );

            channelFuture.addListener(
                    (ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            System.out.println("Connection established.");
                        } else {
                            System.out.println("Connection failed.");
                            future.cause().printStackTrace();
                        }
                    }
            );
        }
    }
}
