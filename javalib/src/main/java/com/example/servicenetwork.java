package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by djj on 2016/11/9.
 */

public class servicenetwork {
    static final int PORT = 12797, MAXSOCKET = 100;
    static boolean close = false;
    //static ServerSocket server;
    //static DBHelper dbHelper;
    //static Socket socket;

    //static LinkedList<SocketThread> socketpool;

    /*public static void close() {
        try {
           *//* for (SocketThread i : socketpool)
                i.finish();

            //socket.close();
            if (socket == null) {
                socket = new Socket("127.0.0.1", PORT);
            }
            socket.close();
            dbHelper.close();
            server.close();*//*


        } catch (Exception e) {
            e.printStackTrace();
        }
        mainthread.interrupt();
        System.exit(1);
    }*/

   /* private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }*/

    public static void main(String[] args) throws IOException {
        /*System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        String t = "测试字符...";
        System.out.println(t);
        String utf8 = new String(t.getBytes("UTF-8"));
        System.out.println(utf8);
        String unicode = new String(utf8.getBytes(), "UTF-8");
        System.out.println(unicode);
        String gbk = new String(unicode.getBytes("GBK"));

        System.out.println(gbk);*/
        //Thread mainthread;
        //mainthread = Thread.currentThread();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAXSOCKET);
        //socketpool = new LinkedList<SocketThread>();
        ServerSocket server = new ServerSocket(PORT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                boolean stop = false;
                while (!stop) {
                    String com = sc.nextLine();
                    if (com.equalsIgnoreCase("exit")) {
                        fixedThreadPool.shutdown();
                        close = true;
                        try {
                            Socket socket = new Socket("127.0.0.1", PORT);
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        sc.close();
                        stop = true;
                        //close();
                    }
                }
            }
        }).start();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Iterator iterator = socketpool.iterator();
                    while (iterator.hasNext()) {
                        SocketThread socketThread = (SocketThread) iterator.next();
                        if (socketThread.getfinished()) iterator.remove();
                    }
                    try {
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/


        //SocketThread tmpthread;

        //ResultSet rs;
        while (!close) {
            try {
                fixedThreadPool.execute(new SocketThread(server.accept()));
            } catch (RejectedExecutionException e) {
            }
        }


        //is=new DataInputStream(s.getInputStream());
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//璁剧疆鏃ユ湡鏍煎紡
            dbHelper.testblob();*/




       /* while (true)
        {
            text = is.readUTF();
            System.out.println(df.format(new Date()) + "-" + s.getInetAddress().toString().substring(1) + ":" + text);
            sql = "insert tmp(text) values(?)";
            dbHelper.dosql(sql, text);
            sqlread = "select * from tmp";
            rs = dbHelper.readsql(sqlread);
            try
            {
                while (rs.next()) {
                    System.out.println(rs.getString("text"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}
