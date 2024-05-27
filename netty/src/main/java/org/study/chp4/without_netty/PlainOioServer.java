package org.study.chp4.without_netty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port); // 지정된 포트에 서버를 바인딩
        try {
            for (;;) {
                final Socket clientSocket = socket.accept(); // 연결 수락
                System.out.println("Accepted connection from " + clientSocket);
                // 연결을 처리할 새 스레드를 생성
                new Thread(() -> {
                    try(OutputStream out = clientSocket.getOutputStream()) {
                        out.write("Hi!\r\n".getBytes(
                                Charset.forName("UTF-8"))); // 연결된 클라이언트에게 메시지를 작성
                        out.flush();
                        clientSocket.close(); // 연결 닫기
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start(); // 스레드 시작
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}