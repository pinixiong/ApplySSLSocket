package com.rj.applysslsocket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ClientThread implements Runnable {
    String request = "GET /wisp_aas/adapter?open&_method=getAllAppInfo HTTP/1.1\r\nHOST: 127.0.0.1:8011\r\n\r\n";
    private Context context;
    public char[] password = "123456".toCharArray();
    private int port = 8889;
    private String ip = "192.168.9.122";
    private SSLSocket sslsocket = null;
    private SSLContext mSSLContext = null;
    private Handler handler;
    OutputStream os = null;
    DataInputStream dis;
    public ClientThread(Handler handler, Context context, String ip, int port) {
        this.handler = handler;
        this.context = context;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            initSSLSocket();

            os = sslsocket.getOutputStream();
            Log.i("aa", "接收到了UI，准备上传ing：");
            os.write(request.getBytes("utf-8"));
            Log.i("aa", "UI数据上传成功:");

            // 启动一条子线程来读取服务器响应的数据
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.i("aa", "等待读取服务器数据");
                        InputStream in = sslsocket.getInputStream();
                        Log.i("aa", "读取服务器数据成功");
                        dis = new DataInputStream(in);
                        String line_head,line_body;
                        final List headlist = new ArrayList<>();
                        while (!TextUtils.isEmpty(line_head = dis.readLine())){
                            Log.i("aa",line_head);
                            headlist.add(line_head);
                        }
                        //分割得到编码
                        String type = headlist.get(3).toString();
                        String[] ary = type.split("\\=");
                        String s1 = ary[0];
                        String s2 = ary[1];
                        System.out.println(s2);
                        String length = headlist.get(4).toString();
                        String[] ary1 = length.split("\\ ");
                        String s3 = ary1[0];
                        String s4 = ary1[1];
                        System.out.println(s4);
                        int size = Integer.parseInt(s4);
                        String line, content = "";
                        byte[] bytes = new byte[size > 1024 ? 1024 : size];
                        while ((dis.read(bytes, 0, bytes.length)) > 0) {
                            line = new String(bytes, s2);
                            content += line;
                        }
                        System.out.println(content);
                        Message msg = new Message();
                        msg.what = 0x111;
                        msg.obj = content;
         /*               String test = "[{\"name\":\"电子政务\"},{\"name\":\"电子\"}]";
                        msg.obj = test;*/
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSSLSocket() {
        try {
            mSSLContext = SSLContext.getInstance("SSL");
            mSSLContext.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = mSSLContext.getSocketFactory();
            sslsocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);
/*
            Socket pyoxy = new Socket(ip,port);
            sslsocket = (SSLSocket) sslSocketFactory.createSocket(pyoxy,ip, port,true);*/

        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}

