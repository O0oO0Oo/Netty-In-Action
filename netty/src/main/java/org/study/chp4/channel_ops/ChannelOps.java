package org.study.chp4.channel_ops;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ChannelOps {

    public void writeToChannel() {
         Channel channel= new NioSocketChannel();
        ByteBuf buf = Unpooled.copiedBuffer("Your data", StandardCharsets.UTF_8);
        // 데이터를 쓰고 플러시
        ChannelFuture cf = channel.writeAndFlush(buf);

        // 완료후 알림을 받기위해 리스너 등록
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("write successful");
                }
                else{
                    System.err.println("write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }

    public void usingChannelFromManyThreads() {
        Channel channel = new NioSocketChannel();
        ByteBuf buf = Unpooled.copiedBuffer("your data.", StandardCharsets.UTF_8);
        Runnable runnable = () -> channel.write(buf.duplicate());

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        IntStream.range(0, 10)
                .forEach(i -> threadPool.execute(runnable));
    }
}
