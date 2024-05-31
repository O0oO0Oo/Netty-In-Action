package org.study.chp5.bytebuf_allocation;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ByteBufAllocation {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    pipeline.addLast(new ChannelHandlerAdapter(){
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            System.out.println("Start");

                                            // ChannelHandlerContext 통한 alloc
                                            ByteBufAllocator alloc = ctx.alloc();
                                            alloc.directBuffer(64)
                                                    .writeBytes(new byte[]{1, 2, 3, 4, 5});
                                            super.channelActive(ctx);
                                        }
                                    });
                                }
                            }
                    );

            Channel channel = bootstrap.bind(5001).sync().channel();

            // Channel 를 통한 alloc
            ByteBufAllocator alloc = channel.alloc();
            alloc.heapBuffer(64)
                    .writeBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
