package com.annis.tensioncable.UI;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;

import butterknife.OnClick;

public class SetCable extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_set_cable;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("配置拉索").setBack(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {

    }

    @Override
    protected void laodData() {

    }

    @OnClick(R.id.control_more)
    void more() {

    }

    @OnClick(R.id.controler_start)
    void start() {

    }

    @OnClick(R.id.controler_pause)
    void pause() {

    }

    @OnClick(R.id.controler_stop)
    void stop() {

    }
}