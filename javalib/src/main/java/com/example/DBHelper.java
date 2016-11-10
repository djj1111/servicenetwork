package com.example;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by djj on 2016/10/31.
 */

public class DBHelper {
    private static final String url = "jdbc:mysql://127.0.0.1/test";
    private static final String name = "com.mysql.jdbc.Driver";
    private static final String user = "user";
    private static final String password = "1111";
    public PreparedStatement stmt = null;
    public ResultSet rs = null;
    public Connection conn = null;

    public DBHelper() {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//建立数据库连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public  void testblob(){
        int j;
        try {byte[] buffer1=new byte[10];
            for (int i=0;i<10;i++){
                buffer1[i]=(byte) (i+40);
            }
            byte[] buffer2=new byte[20];
            for (int i=0;i<20;i++){
                buffer2[i]=(byte) (i+50);
            }

            DataInputStream in=new DataInputStream(new ByteArrayInputStream(buffer1));
            stmt = conn.prepareStatement("insert tmp(photo) values(?)");
            stmt.setBinaryStream(1,in,in.available());
            in=new DataInputStream(new ByteArrayInputStream(buffer2));
            //System.out.print(buffer.length);
            stmt.setBinaryStream(1,in,in.available());
            j=stmt.executeUpdate();
            System.out.print(j);
            //ByteArrayOutputStream i=new ByteArrayOutputStream();


            //rs = stmt.executeQuery(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public int writedatabase(String ip, String text, byte[] in) {
        DataInputStream photo = new DataInputStream(new ByteArrayInputStream(in));
        try {
            stmt = conn.prepareStatement("insert tmp(ip,text,photo) values(?,?,?)");
            stmt.setString(1, ip);
            stmt.setString(2, text);
            stmt.setBinaryStream(3, photo, photo.available());
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    public void readdatabase() {
        try {
            stmt = conn.prepareStatement("select photo from tmp where id=8");
            //stmt.setInt(1,3);
            rs = stmt.executeQuery();
            rs.first();
            Blob bb = rs.getBlob(1);
            DataInputStream inputStream = new DataInputStream(bb.getBinaryStream());
            File file = new File("d:\\tttttt5.jpg");
            FileOutputStream fout = new FileOutputStream(file);
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b, 0, b.length)) > 0) {
                System.out.println("收到照片，长度为" + length);
                fout.write(b, 0, length);
                fout.flush();
            }
            fout.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return rs;
    }

    public void close() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
