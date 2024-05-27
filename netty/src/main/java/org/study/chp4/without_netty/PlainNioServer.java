package org.study.chp4.without_netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {
    public void serve(int port) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false); // non blocking
            ServerSocket socket = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            socket.bind(address); // 지정된 포트에 서버를 바인

            Selector selector = Selector.open(); // 채널을 처리하기 위해 셀렉터를 엽니다.
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 셀렉터와 서버 소켓을 연결하여 연결을 수락합니다.

            final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
            for (; ; ) {
                try {
                    selector.select(); // 새 이벤트를 처리하기 위해 대기합니다. 다음 수신 이벤트까지 블록합니다.
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // 예외 처리
                    break;
                }

                Set<SelectionKey> readyKeys = selector.selectedKeys(); // 이벤트를 수신한 모든 SelectionKey 인스턴스를 가져옵니다.
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    try {
                        if (key.isAcceptable()) { // 이벤트가 새 연결 준비 상태인지 확인합니다.
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);

                            // 클라이언트를 수락하고 셀렉터와 등록합니다.
                            client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                            System.out.println("Accepted connection from " + client);
                        }
                        if (key.isWritable()) { // 소켓이 데이터를 쓰기 위한 준비 상태인지 확인합니다.
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            while (buffer.hasRemaining()) {
                                if (client.write(buffer) == 0) { // 연결된 클라이언트에 데이터를 씁니다.
                                    break;
                                }
                            }
                            client.close(); // 연결을 닫습니다.
                        }
                    } catch (IOException ex) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException cex) {
                            // 닫기 시 무시
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
