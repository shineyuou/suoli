package com.annis.tensioncable.My;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;

import com.annis.tensioncable.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.app.NotificationCompat;

public class TcpService extends Service {
    private static final String TAG = "TcpService";
    //MySocketServer的相关参数
    private int port;//端口
    private boolean isEnable;
    private ExecutorService threadPool;//线程池
    private ServerSocket socket;
    private Socket RemotePeer;
    private Notification mNoti;
    private int count=0;

    public TcpService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        添加前台通知
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        mNoti = new NotificationCompat.Builder(this)
                .setContentTitle("已连接节点")
                .setContentText(""+count)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .build();
        startForeground(1,mNoti);
        MySocketServer(1235);//TCP服务开启端口
        startServerAsync();
    }

    public void MySocketServer(int abort) {
        port = abort;
        threadPool = Executors.newCachedThreadPool();
    }

    /**
     * 开启server
     */
    public void startServerAsync() {
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(port);
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isEnable) {
                RemotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        mNoti = new NotificationCompat.Builder(TcpService.this)
                                .setContentTitle("已连接节点")
                                .setContentText(""+count)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.mipmap.ic_launcher))
                                .build();
                        startForeground(1,mNoti);
                        String filename=RemotePeer.getInetAddress().toString().replace("/","")
                                +".csv";
                        File file = new File(Environment.getExternalStorageDirectory() + "/1/节点",
                                filename);
                        onAcceptRemotePeer(RemotePeer,file);
                    }
                });
            }
            if (!isEnable)
                System.out.println("失去连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void onAcceptRemotePeer(Socket remotePeer, File file) {
        try {
            //remotePeer.getOutputStream().write("connected successful".getBytes());//告诉客户端连接成功
            // 从Socket当中得到InputStream对象
            InputStream inputStream = remotePeer.getInputStream();
            byte buffer[] = new byte[5];
            FileOutputStream fos = new FileOutputStream(file);
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences flag = getSharedPreferences("flag", MODE_PRIVATE);

            // 从InputStream当中读取客户端所发送的数据
            while (inputStream.read(buffer) != -1) {
                double j = (buffer[4] & 0xff);
                double i =(buffer[3] << 8) & 0xff00;
                String jj= String.valueOf(j+i)+"\n";
                String ip=sp.getString("ip",null);
                if (flag.getBoolean("flag",false))
                    fos.write(jj.getBytes());
                if (ip!=null){
                    Intent intent = new Intent();
                    intent.putExtra("count", j+i);
                    intent.setAction("com.project.moli.demobroad.MyService");
                    sendBroadcast(intent);
                }
            }
            fos.close();
            count--;
            mNoti = new NotificationCompat.Builder(TcpService.this)
                    .setContentTitle("已连接节点")
                    .setContentText(""+count)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher))
                    .build();
            startForeground(1,mNoti);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count=0;
        if (!isEnable) {
            return;
        }
        isEnable = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (RemotePeer!=null) {
            try {
                RemotePeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = null;
    }
    /**
     * byte型十六进制数字装换成字符串
     *
     * @param b byte数组
     * @return 字符串
     */
    public static String byteArrToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte aB : b) {
            result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
