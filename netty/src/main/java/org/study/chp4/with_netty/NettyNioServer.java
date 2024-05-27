package org.study.chp4.with_netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class NettyNioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n",
                StandardCharsets.UTF_8);
        EventLoopGroup group = new NioEventLoopGroup(); // 비블로킹 모드를 위한 NioEventLoopGroup을 사용합니다.

        try {
            ServerBootstrap b = new ServerBootstrap(); // ServerBootstrap을 생성합니다.
            b.group(group)
                    .channel(NioServerSocketChannel.class) // NioServerSocketChannel 클래스를 사용합니다.
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 각 수락된 연결에 대해 호출될 ChannelInitializer를 지정합니다.
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() { // 이벤트를 수신하고 처리하기 위해 ChannelInboundHandlerAdapter를 추가합니다.
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE); // 클라이언트에게 메시지를 작성하고 메시지가 작성된 후 연결을 닫는 ChannelFutureListener를 추가합니다.
                                        }
                                    });
                        }
                    });

            ChannelFuture f = b.bind().sync(); // 서버를 바인딩하여 연결을 수락합니다.
            f.channel().closeFuture().sync(); // 모든 리소스를 해제합니다.
        } finally {
            group.shutdownGracefully().sync(); // 모든 리소스를 해제합니다.
        }
    }
}
