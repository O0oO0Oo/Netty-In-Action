package org.study.chp8.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class BootstrapServerAndClient {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler( // 수락된 채널에 대해 I/O 및 데이터를 처리한 핸들러 설정
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture channelFuture;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 클라이언트
                                Bootstrap bootstrap = new Bootstrap();
                                bootstrap
                                        .channel(NioSocketChannel.class)
                                        .handler(
                                                new SimpleChannelInboundHandler<ByteBuf>() {
                                                    @Override
                                                    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                                        System.out.println("Received data.");
                                                    }
                                                }
                                        );

                                // 같은 이벤트 루프를 공유
                                bootstrap.group(ctx.channel().eventLoop());
                                bootstrap.connect(new InetSocketAddress("localhost", 5001));
                            }

                            @Override
                            protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                if (channelFuture.isDone()) {
                                    // do something with the data
                                }
                            }
                        }
                );

        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(5050));
        channelFuture.addListener(
                (ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println("Server  bound.");
                    } else {
                        System.out.println("Bind failed.");
                        future.cause().printStackTrace();
                    }
                }
        );
    }
}
