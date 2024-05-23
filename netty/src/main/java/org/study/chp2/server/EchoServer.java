package org.study.chp2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(5001).start();
    }

    public void start() throws Exception{
        final ChannelHandlerAdapter serverHandler = new EchoServerHandler();
        // Nio 이벤트 그룹 생성
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 서버부트 스트랩 생성
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // Nio 트랜스포트 채널로 설정
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 채널 초기화 설정
                    // ChannelInitializer 는 새 연결이 수락될 때마다 새 자식 채널이 생성되고,
                    // 이 채널의 ChannelPipeline 에 EchoServerHandler 인스턴스를 추가
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // ChannelPipeline에 EchoServerHandler를 추가
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // 서버를 포트에 바인딩하고, 바인딩이 완료될 때까지 대기
            ChannelFuture f = b.bind().sync();
            // 서버 채널이 닫힐 때까지 애플리케이션이 대기
            f.channel().closeFuture().sync();
        } finally {
            // 이벤트 루프 그룹을 종료하고 모든 리소스를 해제
            group.shutdownGracefully().sync();
        }
    }
}
