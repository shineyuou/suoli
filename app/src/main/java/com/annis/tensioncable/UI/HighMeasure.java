package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.IpScanner;
import com.annis.tensioncable.My.IpUtils;
import com.annis.tensioncable.My.TcpService;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;
import com.annis.tensioncable.adapter.EquipmentAdapter;
import com.annis.tensioncable.model.Equipment;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


public class HighMeasure extends BaseActivity {
    private static final String TAG = "HighMeasure";
    @BindView(R.id.high_measure_lv)
    RecyclerView listView;

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;

    List<Equipment> datas = new ArrayList<>();
    EquipmentAdapter adapter;

    ArrayList<String> mmac = new ArrayList<>();
    ArrayList<String> mip = new ArrayList<>();
    private IpScanner mIpScanner;
    private MyReceiver mMyReceiver;
    private SimpleDateFormat nowTime; // 获取当前时间

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ip = Objects.requireNonNull(intent.getExtras()).getString("ip");
            boolean flag = intent.getExtras().getBoolean("flag", false);
            for (int i = 0; i < mip.size(); i++) {
                if (mip.get(i).equals(ip)) {
                    if (flag) {
                        datas.get(i).setStatus(1);
                    } else {
                        datas.get(i).setStatus(0);
                        setFlag(false);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 用来通知是否开始存储tcp服务器的数据
     *
     * @param b true 存储 false 不存储
     */
    private void setFlag(boolean b) {
        long start_time = System.currentTimeMillis();
        long end_time = start_time + ConstantsSP.getInstance(this)
                .getValue(Constants.SP.MeasureTime, 10) * 1000;
        ContentValues values = new ContentValues();
        values.put("end_time", end_time);
        values.put("flag", b);
        LitePal.updateAll(IpUtils.class, values);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_high_measure;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("高精度测量").setBack(true).setLeftTitle("返回").setRightViewId(R.layout.part_title_right_more);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void initViewAndEvent() {
        nowTime = new SimpleDateFormat("hh-mm-ss");
        setFlag(false);

        mMyReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.HighMeasure");
        this.registerReceiver(mMyReceiver, filter);


        right_menu.setVisibility(View.GONE);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        listView.addItemDecoration(divider);
        adapter = new EquipmentAdapter(this);
        listView.setAdapter(adapter);
        //索力测量
        adapter.setOnRvItemClickListener((itemView, position) -> {
            Intent intent = new Intent(this, EquipmentDataActivity.class);
            intent.putExtra("object", mmac.get(position));
            //将需要显示波形的ip存入sp中
            SharedPreferences.Editor edit = getSharedPreferences("data", MODE_PRIVATE).edit();
            edit.putString("ip", mip.get(position));
            edit.apply();
            startActivity(intent);
        });

        mIpScanner = new IpScanner();
        mIpScanner.startScan();
        mIpScanner.setOnScanListener((resultMap, mac, ip) -> {
            mmac = mac;
            mip = ip;
        });
    }

    /**
     * 加载数据
     */
    @Override
    protected void laodData() {

        datas.clear();
        long start_time = 0;
        long end_time = 0;
        for (int i = 0; i < mmac.size(); i++) {
            datas.add(new Equipment(mmac.get(i), 0));
        }
        for (int i = 0; i < mip.size(); i++) {
            if (LitePal.where("mac = ?", mmac.get(i)).find(IpUtils.class).isEmpty()) {
                new IpUtils(mip.get(i), mmac.get(i), start_time,
                        end_time, false,nowTime.format(new java.util.Date())).save();
            }
        }
        adapter.setDatas(datas);
    }

    /**
     * 桥索参数设置
     */
    @OnClick(R.id.tel_cable_setting)
    void setCable(View view) {
        startAcitvity(CableSettingActivity.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量参数设置
     */
    @OnClick(R.id.tel_measure_setting)
    void setConfig(View view) {
        startAcitvity(MeasureConfigSetting.class);
        right_menu.setVisibility(View.GONE);
    }


    /**
     * 分享
     */
    @OnClick(R.id.tel_data_share)
    void share(View view) {
        Intent intent = new Intent(this, ShareActivtiy.class);
        intent.putExtra("source", "高精度测量");
        startActivity(intent);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量 点击
     */
    @OnClick(R.id.controler_start)
    void start(View view) {
        showToast("start");
        //存入开始数据的标志
        setFlag(true);
        //使全部节点变成测量状态
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(1);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 暂停 点击
     */
    @OnClick(R.id.controler_pause)
    void pause(View view) {
        setFlag(false);
        //使全部节点变成测量状态
        File[] files = new File(Environment.getExternalStorageDirectory() + "/1/节点").listFiles();
        for (File file : files) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write("".getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(2);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 停止 点击
     */
    @OnClick(R.id.controler_stop)
    void stop(View view) {
        setFlag(false);
        File[] files = new File(Environment.getExternalStorageDirectory() + "/1/节点").listFiles();
        for (File file : files) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write("".getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //使全部节点变成测量状态
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(0);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新 点击
     */
    @OnClick(R.id.controler_reset)
    void reset(View view) {
        startService(new Intent(HighMeasure.this, TcpService.class));
        showToast("reset");
        mIpScanner.startScan();
        laodData();
    }

    /**
     * 生命周期刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(HighMeasure.this, TcpService.class));
        this.unregisterReceiver(mMyReceiver);
        LitePal.deleteDatabase("iputils");
        LitePal.getDatabase();
    }

    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
    }

    @OnClick(R.id.right_view)
    void back(View view) {
        right_menu.setVisibility(View.VISIBLE);
    }

}
