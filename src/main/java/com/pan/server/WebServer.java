package com.pan.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebServer {

    public void serverStart(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                new Processor(socket).start();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void  main(String[] args){
        int port = 80;
        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }


        WebServer server = new WebServer();
        server.serverStart(port);

//        try {
//            ServerSocket serverSocket = new ServerSocket(9000);
//            Socket socket = serverSocket.accept();
//            InputStream is = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            OutputStream os = socket.getOutputStream();
//            PrintStream ps = new PrintStream(os);
//            byte[] bytes = new byte[1024];
//            String i = "";
//            while ((i = br.readLine()) != null && !"".equals(i = br.readLine().trim())) {
//                System.out.println(i);
//                ps.println("ok");
//                ps.println("------");
//            }
//            System.out.println("读完了");
//            ps.flush();
//            ps.close();
//            br.close();
//            socket.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
