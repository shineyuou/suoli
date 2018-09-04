package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.My.IpScanner;
import com.annis.tensioncable.My.TcpService;
import com.annis.tensioncable.R;
import com.annis.tensioncable.adapter.EquipmentAdapter;
import com.annis.tensioncable.model.Equipment;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


public class HighMeasure extends BaseActivity {
    @BindView(R.id.high_measure_lv)
    RecyclerView listView;

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;

    List<Equipment> datas = new ArrayList<>();
    EquipmentAdapter adapter;

    ArrayList<String> mmac=new ArrayList<>();
    ArrayList<String> mip=new ArrayList<>();
    private IpScanner mIpScanner;

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
        setFlag(false);
        startService(new Intent(HighMeasure.this, TcpService.class));
        right_menu.setVisibility(View.GONE);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        listView.addItemDecoration(divider);
        adapter = new EquipmentAdapter(this);
        listView.setAdapter(adapter);
        adapter.setOnRvItemClickListener((itemView, position) -> {
            Intent intent = new Intent(this, EquipmentDataActivity.class);
            intent.putExtra("object",mmac.get(position));
            //将需要显示波形的ip存入sp中
            intent.putExtra("ip",mip.get(position));
            SharedPreferences.Editor edit = getSharedPreferences("data", MODE_PRIVATE).edit();
            edit.putString("ip",mip.get(position));
            edit.apply();

            startActivity(intent);
            //TODO 向TCP服务传入需要显示波形的IP地址

        });
//        adapter.setOnRvItemClickListener((itemView, position) ->
//                startAcitvity(EquipmentDataActivity.class, adapter.getItem(position)));

        mIpScanner = new IpScanner();
        mIpScanner.startScan();
        mIpScanner.setOnScanListener((resultMap, mac, ip) -> {
            mmac=mac;
            mip=ip;
        });
    }

    /**
     * 加载数据
     */
    @Override
    protected void laodData() {
        //TODO 添加自己的动态数据
        //datas = Constants.Object.getEquipments();
        datas.clear();
        for (int i = 0; i < mmac.size(); i++) {
            datas.add(new Equipment(mmac.get(i),0));
        }
        adapter.setDatas(datas);
    }

    /**
     * 生命周期刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        //laodData();
    }

    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
    }

    @OnClick(R.id.right_view)
    void back(View view) {
        right_menu.setVisibility(View.VISIBLE);
    }

    /**
     * 参数设置
     *
     * @param view
     */
    @OnClick(R.id.tel_cable_setting)
    void setCable(View view) {
        startAcitvity(CableSettingActivity.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量参数
     *
     * @param view
     */
    @OnClick(R.id.tel_measure_setting)
    void setConfig(View view) {
        startAcitvity(MeasureConfigSetting.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 分享
     *
     * @param view
     */
    @OnClick(R.id.tel_data_share)
    void share(View view) {
        startAcitvity(ShareActivtiy.class);
        right_menu.setVisibility(View.GONE);
    }

    /**
     * 测量 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_start)
    void start(View view) {
        showToast("start");
        //存入开始数据的标志
        //TODO
        setFlag(true);

        //使全部节点变成测量状态
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(1);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 暂停 点击
     * @param view
     */
    @OnClick(R.id.controler_pause)
    void pause(View view) {
        showToast("pause");
        setFlag(false);
        stopService(new Intent(HighMeasure.this, TcpService.class));
        //使全部节点变成测量状态
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(2);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 停止 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_stop)
    void stop(View view) {
        showToast("stop");
        setFlag(false);
        stopService(new Intent(HighMeasure.this, TcpService.class));
        //使全部节点变成测量状态
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setStatus(0);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 刷新 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_reset)
    void reset(View view) {
        showToast("reset");
        mIpScanner.startScan();
        laodData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setFlag(false);
        stopService(new Intent(HighMeasure.this, TcpService.class));
    }

    /**
     * 用来通知是否开始存储tcp服务器的数据
     * @param b true 存储 false 不存储
     */
    private void setFlag(boolean b){
        SharedPreferences.Editor flag = getSharedPreferences("flag", MODE_PRIVATE).edit();
        flag.putBoolean("flag",b);
        flag.apply();
    }
}
