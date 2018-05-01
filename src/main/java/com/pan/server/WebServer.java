package com.pan.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebServer {
    private static  BlockingQueue<Socket> queue = new LinkedBlockingQueue<Socket>(20);
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void serverStart(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                queue.put(socket);
                //new Processor(socket).start();
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
        //线程1：创建web服务器，监听请求
//        final int finalPort = port;
//        Thread t1 = new Thread(new Runnable() {
//            public void run() {
//                WebServer server = new WebServer();
//                server.serverStart(finalPort);
//            }
//        });

        //线程2：监听queue，若有socket元素，则处理
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                while (true){
                    if(queue.isEmpty()){
                        try {
                            Thread.sleep(100);
                           // System.out.println("当前没有待处理的请求.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("处理请求.");
                        Runnable r1 = new Runnable() {
                            public void run() {
                                System.out.println(queue);
                                Socket socket = null;
                                try {
                                    socket = queue.take();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(socket == null){
                                    System.out.println("连接已断开");
                                    return;
                                }
                                System.out.println("当前执行socket线程："+Thread.currentThread().getName()+"socket:"+socket.isConnected());
//                                System.out.println("当前执行socket线程："+Thread.currentThread().getName()+"+++++"+socket.getInetAddress());
//                                System.out.println("当前执行socket线程："+Thread.currentThread().getName()+"+++++"+socket.getLocalPort());
//                                System.out.println("当前执行socket线程："+Thread.currentThread().getName()+"+++++"+socket.getLocalSocketAddress());
//                                System.out.println("当前执行socket线程："+Thread.currentThread().getName()+"+++++"+socket.getRemoteSocketAddress());

                                new Processor(socket).start();
                            }
                        };
                        executorService.execute(r1);
                    }
                }

            }
        });
        t2.start();
        WebServer server = new WebServer();
        server.serverStart(port);
        //t1.start();

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
