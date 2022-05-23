package com;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerDemo {
    public static List<Socket> AllOnlineSocket = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        System.out.println("==================启动服务端成功=================");
        //1、注册端口
        ServerSocket serverSocket = new ServerSocket(8888);

        //2、循环接受管道请求
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getLocalSocketAddress() + "上线了！");
            AllOnlineSocket.add(socket);
            //3、创建独立线程单独处理请求
            new ServerReaderThead(socket).start();
        }
    }
}

class ServerReaderThead extends Thread{
    private Socket socket;
    public ServerReaderThead(Socket socket){
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
                System.out.println(socket.getRemoteSocketAddress() + "发来了：" + line);
                //服务端群发消息
                SendMsgToAll(line);
            }
        } catch (Exception e) {
            System.out.println(socket.getRemoteSocketAddress() + "下线了！！！");
            ServerDemo.AllOnlineSocket.remove(socket);
        }
    }

    private void SendMsgToAll(String msg) throws Exception {
        for (Socket socket : ServerDemo.AllOnlineSocket) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println(msg);
            ps.flush();
        }
    }
}