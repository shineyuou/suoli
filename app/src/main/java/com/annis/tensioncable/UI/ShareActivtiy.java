package com.annis.tensioncable.UI;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;

public class ShareActivtiy extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_share;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("数据共享").setBack(true);
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
}
