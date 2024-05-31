package org.study.chp5.bytebuf_reference_counting;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ByteBufReferenceCounting {
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
            
            // 참조 2 증가
            ByteBuf directBuffer = alloc.directBuffer();
            ByteBuf directBufferRetain = directBuffer.retain();
            ByteBuf heapBuffer = alloc.heapBuffer();
            assert directBuffer.refCnt() == 2;
            assert directBufferRetain.refCnt() == 2;
            assert heapBuffer.refCnt() == 1;

            alloc.heapBuffer(64)
                    .writeBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            
            // 참조 1 감소
            directBuffer.release();
            heapBuffer.release();
            assert directBuffer.refCnt() == 1;
            assert directBufferRetain.refCnt() == 1;

            // 참조 해제된 버퍼 사용시 예외 발생
            try{
                heapBuffer.writeByte(1);
            } catch (Exception e) {
                System.out.println("heapBuffer 카운트 0, IllegalReferenceCountException 발생");
            }

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
