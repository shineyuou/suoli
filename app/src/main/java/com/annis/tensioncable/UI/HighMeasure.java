package com.annis.tensioncable.UI;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import android.view.View;
import android.widget.LinearLayout;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.adapter.EquipmentAdapter;
import com.annis.tensioncable.model.Equipment;

import java.util.ArrayList;
import java.util.List;


public class HighMeasure extends BaseActivity {
    @BindView(R.id.high_measure_lv)
    RecyclerView listView;

    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;

    List<Equipment> datas = new ArrayList<>();
    EquipmentAdapter adapter;

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

    @Override
    protected void initViewAndEvent() {
        right_menu.setVisibility(View.GONE);
        listView.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        listView.addItemDecoration(divider);
        adapter = new EquipmentAdapter(this);
        listView.setAdapter(adapter);
        adapter.setOnRvItemClickListener((itemView, position) -> startAcitvity(EquipmentDataActivity.class, adapter.getItem(position)));

        laodData();
    }

    /**
     * 加载数据
     */
    @Override
    protected void laodData() {
        //TODO 添加自己的动态数据
        datas = Constants.Object.getEquipments();
        adapter.setDatas(datas);
    }

    /**
     * 生命周期刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        laodData();
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
    }

    /**
     * 暂停 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_pause)
    void pause(View view) {
        showToast("pause");
    }

    /**
     * 停止 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_stop)
    void stop(View view) {
        showToast("stop");
    }

    /**
     * 刷新 点击
     *
     * @param view
     */
    @OnClick(R.id.controler_reset)
    void reset(View view) {
        showToast("reset");
    }
}
