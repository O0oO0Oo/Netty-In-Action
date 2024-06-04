package org.study.chp8.options;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class NettyOptions {
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Receoved Data");
                    }
                });

        bootstrap
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        // new AttributeKey<>(String name) 는 private 임, newInstance, valueOf 를 사용해야 함 또한 Singleton 패턴임
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        bootstrap.attr(id, 12345);

        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(5001));
        channelFuture.syncUninterruptibly();
    }
}
