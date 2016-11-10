package com.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by djj on 2016/11/9.
 */

public class servicenetwork {
    static final int PORT = 12797, MAXSOCKET = 100;
    static ServerSocket server;
    static DBHelper dbHelper;
    static Socket socket;
    static Thread mainthread;
    static LinkedList<SocketThread> socketpool;

    public static void close() {
        try {
            for (SocketThread i : socketpool)
                i.finish();

            socket.close();
            if (socket == null) {
                socket = new Socket("127.0.0.1", PORT);
            }
            socket.close();
            dbHelper.close();
            server.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mainthread.interrupt();
        System.exit(1);
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        String t = "测试字符...";
        System.out.println(t);
        String utf8 = new String(t.getBytes("UTF-8"));
        System.out.println(utf8);
        String unicode = new String(utf8.getBytes(), "UTF-8");
        System.out.println(unicode);
        String gbk = new String(unicode.getBytes("GBK"));

        System.out.println(gbk);
        mainthread = Thread.currentThread();
        socketpool = new LinkedList<SocketThread>();
        server = new ServerSocket(PORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String com = sc.nextLine();
                    if (com.equals("exit")) {
                        sc.close();
                        close();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (SocketThread i : socketpool) {
                        if (i.getfinished()) socketpool.remove(i);
                    }
                    try {
                        Thread.sleep(2000);
                        ;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        dbHelper = new DBHelper();
        SocketThread tmpthread;

        //ResultSet rs;
        while (true) {
            socket = server.accept();
            if (socketpool.size() < MAXSOCKET) {
                tmpthread = new SocketThread(socket, dbHelper);
                tmpthread.start();
                socketpool.add(tmpthread);

            } else {
                socket.close();
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
