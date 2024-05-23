package org.study.chp2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.nio.charset.StandardCharsets;


// ChannelInboundHandlerAdapter - deprecated - Use ChannelHandlerAdapter instead.
// 채널 핸들러가 여러 채널에서 안전하게 공유될 수 있음을 나타냄
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    // 채널에서 데이터를 읽을 때 호출된다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(StandardCharsets.UTF_8));
        ctx.write(in); // 수신된 메시지를 원격 피어에 다시 쓴다.
    }

    // 마지막 메시지가 읽혔을 떄 호출된다.
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
        // 비어있는 ByteBuf를 쓰고 플러시한 후, 채널을 닫는 리스너를 추가
    }

    // 예외 발생시 호출
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); // 채널 닫음
    }
}