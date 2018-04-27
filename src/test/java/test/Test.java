package test;

import java.io.*;

public class Test {


    public static void main(String[] args){
        File file = new File("D:\\httpdocs"+"/index.html");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while (br.readLine()!=null){
                System.out.println(br.readLine());
            }

        }catch (Exception e){
         e.printStackTrace();
        }

    }
}
