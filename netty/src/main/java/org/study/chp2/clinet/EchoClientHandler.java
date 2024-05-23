package org.study.chp2.clinet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 연결이 설정되었을 때 호출
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", StandardCharsets.UTF_8));
    }

    // 데이터가 수신될 때마다 호출
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Client received: " + in.toString(StandardCharsets.UTF_8));
    }

    // 특정 타입의 메시지를 처리, channelRead0 이름이 변경됨
    /*
     * Since 5.0, {@code channelRead0(ChannelHandlerContext, I)} has been renamed to
     * {@link #messageReceived(ChannelHandlerContext, I)}.
    */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received: " + msg.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
