package org.study.chp6.exception;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ThreeExceptionHandlerServer {
    public static void main(String[] args) {
        try (NioEventLoopGroup boss = new NioEventLoopGroup(1);
             NioEventLoopGroup worker = new NioEventLoopGroup()) {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.
                    group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    ChannelPipeline pipeline = ch.pipeline();

                                    // in -> channel -> first -> second -> third 순으로 들어옴
                                    pipeline.addLast(new FirstExceptionChannelHandler());
                                    pipeline.addLast(new SecondExceptionChannelHandler());
                                    pipeline.addLast(new ThirdExceptionChannelHandler());
                                }
                            }
                    );

            Channel channel = bootstrap.bind(5001).channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
