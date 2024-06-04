package org.study.chp8.shutdown;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.PromiseNotifier;

import java.util.concurrent.ExecutionException;

public class ShutdownServer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .handler(
                            new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                    System.out.println("hello.");
                                }
                            }
                    );

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Future<?> future = group.shutdownGracefully();
            // asynchronous
            future.addListener(
                    listener -> System.out.println("Shutdown.")
            );

            // block
            // future.get();
        }
    }
}
