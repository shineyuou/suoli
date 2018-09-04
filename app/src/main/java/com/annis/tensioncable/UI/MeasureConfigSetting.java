package com.annis.tensioncable.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.Utils.ConstantsSP;

import butterknife.BindView;
import butterknife.OnClick;

public class MeasureConfigSetting extends BaseActivity {
    @BindView(R.id.measure_time_et)
    TextView et_time;
    @BindView(R.id.tv_hz)
    TextView hz;
    @BindView(R.id.sb)
    SeekBar seekBar;
    @BindView(R.id.measure_config_time_container)
    RelativeLayout time_container;

    @Override
    protected int getLayout() {
        return R.layout.activity_measure_config_setting;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("测量参数配置").setBack(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    int MeasureTime, MeasureFrequency;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initViewAndEvent() {
        Intent intent = getIntent();
        String flag = intent.getStringExtra("filepath");

        time_container.findViewById(R.id.part_add).setOnClickListener(v -> setTime(5));
        time_container.findViewById(R.id.part_minus).setOnClickListener(v -> setTime(-5));
        MeasureTime = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureTime, 0);
        MeasureFrequency = ConstantsSP.getInstance(this).getValue(Constants.SP.MeasureFrequency, 0);
        et_time.setText(MeasureTime + "S");
        hz.setText("" + MeasureFrequency);
        if (flag!=null){
            seekBar.setProgress(200);
            hz.setText(200 + "");
            seekBar.setEnabled(false);
        }else{
            seekBar.setProgress(MeasureFrequency);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setFrequency(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void laodData() {

    }

    /**
     * 设置时间
     *
     * @param item_step 进度位置
     */
    @SuppressLint("SetTextI18n")
    private void setTime(int item_step) {
        if ((MeasureTime + item_step) < 0) {
            return;
        }
        MeasureTime += item_step;
        et_time.setText("" + MeasureTime + "S");
    }

    /**
     * 设置频率
     *
     * @param frequency_step
     */
    @SuppressLint("SetTextI18n")
    private void setFrequency(int frequency_step) {
        hz.setText(frequency_step + "");
        MeasureFrequency = frequency_step;
    }

    /**
     * 确定
     */
    @OnClick(R.id.part_save)
    void save() {
        ConstantsSP.getInstance(this).setValue(Constants.SP.MeasureFrequency, MeasureFrequency);
        ConstantsSP.getInstance(this).setValue(Constants.SP.MeasureTime, MeasureTime);
        showToast("保存成功");
        finish();
    }

    /**
     * 取消
     */
    @OnClick(R.id.part_cancel)
    void cancel() {
        finish();
    }
}
