package com.annis.tensioncable.UI;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.model.Equipment;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipmentDataActivity extends BaseActivity {
    @BindView(R.id.item_status_iv)
    ImageView status_iv;
    @BindView(R.id.item_status_tv)
    TextView status_tv;
    @BindView(R.id.part_right_menu)
    LinearLayout right_menu;
    Equipment item;
    String title = "振动数据";

    @Override
    protected int getLayout() {
        return R.layout.activity_tel_measure;
    }

    @Override
    protected TitleBean getMyTitle() {
        item = (Equipment) getIntentObj();
        if (item != null) {
            title = item.getName() + "振动数据";
        }
        TextView textView = new TextView(this);
        textView.setTextColor(Color.WHITE);
        textView.setText("索力分析");
        textView.setTextSize(16);
        textView.setOnClickListener(v -> startAcitvity(SelectCableActivity.class, item));
        return new TitleBean(title).setBack(true).setRightView(textView);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        item = (Equipment) getIntentObj();
        setStatus(item.getStatus());
    }

    @Override
    protected void laodData() {

    }

    /**
     * 菜单显示
     *
     * @param view
     */
    @OnClick(R.id.control_more)
    void showMoreOption(View view) {
        right_menu.setVisibility(View.VISIBLE);
    }

    /**
     * 菜单隐藏
     *
     * @param view
     */
    @OnClick(R.id.part_right_menu_back)
    void hide(View view) {
        right_menu.setVisibility(View.GONE);
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
     */
    @OnClick(R.id.controler_start)
    void start() {
        setStatus(1);
    }

    /**
     * 暂停 点击
     */
    @OnClick(R.id.controler_pause)
    void pause() {
        setStatus(2);
    }

    /**
     * 停止 点击
     */
    @OnClick(R.id.controler_stop)
    void stop() {
        setStatus(0);
    }

    /**
     * 设置状态图表
     *
     * @param status
     */
    private void setStatus(int status) {
        switch (status) {
            /**
             * 测量状态 0: 已停止 1.测量中 2.已暂停
             */
            case 0:
                status_tv.setText("已停止");
                status_iv.setImageResource(R.drawable.tz_two);
                status_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                status_tv.setText("测量中");
                status_iv.setImageResource(R.drawable.ks);
                status_tv.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 2:
                status_tv.setText("已暂停");
                status_iv.setImageResource(R.drawable.zt_two);
                status_tv.setTextColor(Color.rgb(0, 89, 255));
                break;
        }
    }
}
