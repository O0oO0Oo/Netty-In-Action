package org.study.chp6.resourceleak;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ResourceLeak {
    public static void main(String[] args) {

        try (EventLoopGroup boss = new NioEventLoopGroup(1);
             NioEventLoopGroup worker = new NioEventLoopGroup()) {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ResourceLeakedServerChannelHandler());
                        }
                    });

            Channel channel = bootstrap.bind(5001).sync().channel();

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
