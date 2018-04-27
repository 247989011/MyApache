package com.pan.server;

import com.sun.deploy.util.ArrayUtil;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class Processor extends  Thread{

    private  Socket socket;
    private  InputStream is;
    private PrintStream out ;
    public final static String WEB_ROOT="D:\\httpdocs";

    public Processor(Socket socket){
        this.socket = socket;
        try {
            this.is = socket.getInputStream();
            this.out = new PrintStream(socket.getOutputStream());
        }catch (Exception e){

        }

    }

    public void run(){
       String filename =  parse(is);

        sendFile(filename);

    }



    //根据输出流获取资源文件名
    public String parse(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String i ="";
//            while (( i =br.readLine())!=null){
//                System.out.println(i);
//            }
            String httpmsg = br.readLine();
            String[] content  = httpmsg.split(" ");
            if(content.length != 3){
                sendErrorMsg(400,"Client query error");
                return null;
            }
            System.out.println("code:"+content[0]+",filename:"+content[1] +",http version:" + content[2]);
            return content[1];
        }catch (Exception e ){
            e.printStackTrace();
        }


        return null;
    }
    //发送错误信息
    public void sendErrorMsg(int errorCode,String msg){
        try {


            out.println("HTTP/1.1 " + errorCode +" " + msg );
            out.println("content-type: text/html");
            out.println();
            out.println("<html><head>");
            out.println("<title>Erro Message");
            out.println("</title></head>");
            out.println("<body>");
            out.println("<h1>ErroCode:"+errorCode+",Message:"+msg +"</h1>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
    //发送文件
    public void sendFile(String filename){
        File file = new File(WEB_ROOT+filename);
        if(!file.exists()){
            sendErrorMsg(404,"file not found!");
            return ;
        }
        try {

            InputStream in = new FileInputStream(file);
            byte content[] = new byte[(int) file.length()];
            in.read(content);
            out.println("HTTP/1.1 200 queryfile");
            out.println("content-length:"+file.length());
            out.println("\r\t\n");
            out.write(content);
            out.flush();
            out.close();
            in.close();
            return;

        }catch (Exception e ){
            e.printStackTrace();
        }



    }

}
