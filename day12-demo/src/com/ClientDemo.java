package com;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("==================启动客服端成功=================");
        //1、与服务端的socket连接
        Socket socket = new Socket("127.0.0.1",8888);

        //创建独立线程接收消息
        new ClientReaderThread(socket).start();
        //2、从Socket管道中得到一个字节输出流
        OutputStream os = socket.getOutputStream();
        //3、把低级输出流包装成高级打印流
        PrintStream ps = new PrintStream(os);
        //4、发送数据
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请说：");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();//刷新
        }
    }
}

class ClientReaderThread extends Thread{
    private Socket socket;
    public ClientReaderThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            //字节输入流
            InputStream is = socket.getInputStream();
            //把低级字节输入流包装成高级的缓冲字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //按行读取
            String line;
            while ((line = br.readLine()) !=null ){
                System.out.println("收到消息：" + line);
            }
        } catch (Exception e) {
            System.out.println("你已被提出群聊！！！");
        }
    }
}
