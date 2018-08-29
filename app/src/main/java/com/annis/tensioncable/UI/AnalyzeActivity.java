package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;
import com.annis.tensioncable.model.TensionCable;

import butterknife.BindView;

public class AnalyzeActivity extends BaseActivity {
    /**
     * 上一页面被选中的拉索
     */
    TensionCable item;
    /**
     * 基频 内容
     */
    @BindView(R.id.n_et)
    TextView et_N;
    /**
     * 索力 内容
     */
    @BindView(R.id.hz_et)
    TextView et_Hz;

    @BindView(R.id.suoli)
    TextView ss;

    @Override
    protected int getLayout() {
        return R.layout.activity_analyze;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("索力分析").setBack(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        item = (TensionCable) getIntentObj();
        double csa = item.getCSA();
        double density = item.getDensity();
        double length = item.getLength();
        String name = item.getName();
        Integer frequency = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureFrequency, 200);
        Integer time = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureTime, 30);
        //TODO 设置数据
        setContent("1000", "10000");
        setttt( csa, density, length, name, frequency, time);
    }

    @SuppressLint("SetTextI18n")
    private void setttt(double csa, double density, double length, String name, Integer frequency, Integer time){
        ss.setText(""+csa+"\n"+density+"\n"+length+"\n"+name+"\n"+frequency+"\n"+time);
    }
    @Override
    protected void laodData() {

    }

    /**
     * 设置数据图下面的内容
     *
     * @param Hz
     * @param N
     */
    private void setContent(String Hz, String N) {
        et_Hz.setText(Hz);
        et_N.setText(N);
    }
}
