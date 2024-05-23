package org.study.chp1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingIO {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9990);
        Socket clientSocket = serverSocket.accept();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
        );

        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream())
        );

        String req, res;
        while ((req = in.readLine()) != null) {
            if ("Done".equals(req)) {
                break;
            }

            out.println(req);
        }

    }

    private static String processRequest(String req) {
        return "Processed: " + req;
    }
}
