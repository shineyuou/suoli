package com.annis.tensioncable.My;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.annis.tensioncable.R;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
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
    private int count = 0;

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
        setNoti(count);
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
        new Thread(this::doProcSync).start();
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(port);
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isEnable) {
                RemotePeer = socket.accept();
                threadPool.submit(() -> {
                    count++;
                    setNoti(count);
                    List<IpUtils> util = LitePal.where("ip = ?",
                            RemotePeer.getInetAddress().toString().replace("/", "")).find(IpUtils.class);
                    String filename = util.get(0).getMac() +"*"+util.get(0).getTimestamp()+ ".csv";
                    File file = new File(Environment.getExternalStorageDirectory() + "/1/节点",
                            filename);
                    onAcceptRemotePeer(RemotePeer, file);
                });
            }
            if (!isEnable)
                Log.i(TAG, "失去设备连接");
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
            byte buffer[] = new byte[6];
            FileOutputStream fos = new FileOutputStream(file);
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            String mip = remotePeer.getInetAddress().getHostAddress().replace("/", "");

            // 从InputStream当中读取客户端所发送的数据
            while (inputStream.read(buffer) != -1) {
                double j = (buffer[4] & 0xff);
                double i = (buffer[3] << 8) & 0xff00;
                String ip = sp.getString("ip", null);
                List<IpUtils> utils = LitePal.where("ip = ?",
                        RemotePeer.getInetAddress().toString().replace("/", ""))
                        .find(IpUtils.class);
                boolean flag = utils.get(0).isFlag();
                if (flag){
                    if (System.currentTimeMillis()<=utils.get(0).getEnd_time()){
                        String jj = System.currentTimeMillis() + "," + String.valueOf(j + i) + "\n";
                        fos.write(jj.getBytes());
                        if (ip != null && mip.equals(ip)) {
                            Intent intent = new Intent();
                            intent.putExtra("count", j + i);
                            intent.setAction("com.EquipmentDataActivity");
                            sendBroadcast(intent);
                        }
                    }else {
                        //存储完毕，通知绘图界面
                        Intent intent1 = new Intent();
                        intent1.putExtra("count", (double)-1);
                        intent1.setAction("com.EquipmentDataActivity");
                        sendBroadcast(intent1);
                        //存储完毕，通知节点界面
                        Intent intent = new Intent();
                        intent.putExtra("ip", mip);
                        intent.putExtra("flag",false);
                        intent.setAction("com.HighMeasure");
                        sendBroadcast(intent);
                    }
                }
            }
            fos.close();
            count--;
            setNoti(count);
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
            count--;
            setNoti(count);
        }
    }

    /**
     * 更改前台通知
     *
     * @param count 设置当前已接入设备
     */
    private void setNoti(int count) {
        Notification noti = new NotificationCompat.Builder(TcpService.this, "1")
                .setContentTitle("已连接节点")
                .setContentText("" + count)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .build();

        startForeground(1, noti);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
        if (!isEnable) {
            return;
        }
        isEnable = false;
        if (RemotePeer != null) {
            try {
                socket.close();
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
    public String byteArrToHexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte aB : b) {
            result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
