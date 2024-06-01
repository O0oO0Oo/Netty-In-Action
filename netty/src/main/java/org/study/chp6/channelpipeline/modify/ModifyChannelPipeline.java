package org.study.chp6.channelpipeline.modify;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.study.chp6.channelhandler.ServerSimpleChannelInboundHandler;
import org.study.chp6.resourceleak.ResourceLeakedServerChannelHandler;

public class ModifyChannelPipeline {
    public static void main(String[] args) {


        try (NioEventLoopGroup boss = new NioEventLoopGroup(1);
             NioEventLoopGroup worker = new NioEventLoopGroup()) {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    ResourceLeakedServerChannelHandler firstHandler = new ResourceLeakedServerChannelHandler();
                                    ServerSimpleChannelInboundHandler secondHandler = new ServerSimpleChannelInboundHandler();

                                    // pipeline 추가
                                    pipeline.addLast(secondHandler);
                                    pipeline.addFirst(firstHandler);
                                }
                            }
                    );

            Channel channel = bootstrap.bind(5001).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
