package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;

import butterknife.BindView;
import butterknife.OnClick;


public class TelMeasure extends BaseActivity {
    private static final String TAG = "TelMeasure";
    private static final int START=1,PAUSE=2,STOP=0;
    private int status_flag;

    @BindView(R.id.item_status_iv)
    ImageView status_iv;

    @BindView(R.id.item_status_tv)
    TextView status_tv;

    @BindView(R.id.LL)
    TextView ll;
    @SuppressLint("HandlerLeak")
    private Handler set_status_tv=new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (status_flag){
                case START:
                    ll.setText(""+msg.obj);
                default:
                    break;
            }
        }
    };

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;

    private android.hardware.SensorManager SensorManager;
    private android.hardware.Sensor Sensor;
    private MyListener Listener;

    @Override
    protected int getLayout() {
        return R.layout.activity_tel_measure;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("手机测量").setBack(true).setLeftTitle("返回").setRightViewId(R.layout.part_title_right_analyze);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        setStatus(0);
        right_menu.setVisibility(View.GONE);
        SensorManager = (android.hardware.SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor = SensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
        Listener = new MyListener();
        SensorManager.registerListener(Listener, Sensor, android.hardware.SensorManager.SENSOR_DELAY_GAME);
    }

    public class MyListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Message msg = new Message();
            msg.obj=sensorEvent.values[2];
            set_status_tv.sendMessage(msg);
        }

        @Override
        public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
        }
    }

    @Override
    protected void laodData() {


    }

    //点击索力分析
    @OnClick(R.id.right_view)
    void back(View view) {
        startAcitvity(SelectCableActivity.class);
    }

    //开始
    @OnClick(R.id.controler_start)
    void start(View view) {
        setStatus(1);
    }

    //暂停
    @OnClick(R.id.controler_pause)
    void pause(View view) {
        setStatus(2);
    }

    //停止
    @OnClick(R.id.controler_stop)
    void stop(View view) {
        setStatus(0);
    }

    //更多按钮
    @OnClick(R.id.control_more)
    void more() {
        right_menu.setVisibility(View.VISIBLE);
    }

    //隐藏更多界面
    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
    }

    //桥索参数设置
    @OnClick(R.id.tel_cable_setting)
    void setCable(View view) {
        startAcitvity(CableSettingActivity.class);
        right_menu.setVisibility(View.GONE);
    }

    //测量参数设置
    @OnClick(R.id.tel_measure_setting)
    void setConfig(View view) {
        startAcitvity(MeasureConfigSetting.class);
        right_menu.setVisibility(View.GONE);
    }

    //数据共享
    @OnClick(R.id.tel_data_share)
    void share(View view) {
        startAcitvity(ShareActivtiy.class);
        right_menu.setVisibility(View.GONE);
    }

    private void setStatus(int status) {
        switch (status) {
            /**
             * 测量状态 0: 已停止 1.测量中 2.已暂停
             */
            case STOP:
                status_flag=STOP;
                status_iv.setImageResource(R.drawable.tz_two);
                status_tv.setText("已停止");
                status_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case START:
                status_flag=START;
                status_iv.setImageResource(R.drawable.ks);
                status_tv.setText("测量中");
                status_tv.setTextColor(Color.rgb(255, 0, 0));
                break;
            case PAUSE:
                status_flag=PAUSE;
                status_iv.setImageResource(R.drawable.zt_two);
                status_tv.setText("已暂停");
                status_tv.setTextColor(Color.rgb(0, 89, 255));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SensorManager.unregisterListener(Listener);
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

}
