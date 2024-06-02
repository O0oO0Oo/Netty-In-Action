package org.study.chp7.scheduled_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledClient {
    private final String host;
    private final int port;

    public ScheduledClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public static void main(String[] args) throws Exception {
        new ScheduledClient("localhost", 5001).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ScheduledClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync();
            Channel channel = f.channel();

            AtomicInteger cnt = new AtomicInteger(0);
            // 1초 마다 메시지 보냄

            ScheduledFuture<?>[] scheduledFuture = new ScheduledFuture<?>[1];
            scheduledFuture[0] = channel.eventLoop().scheduleWithFixedDelay(
                    () -> {
                        // 5초후 종료
                        if(cnt.incrementAndGet() > 5){
                            scheduledFuture[0].cancel(true);
                        }
                        else{
                            channel.writeAndFlush(Unpooled.copiedBuffer(("hello at " + System.currentTimeMillis()).getBytes()));
                        }
                    },
                    1, 1, TimeUnit.SECONDS
            );

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
