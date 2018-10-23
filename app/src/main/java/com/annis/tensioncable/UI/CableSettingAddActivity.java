package com.annis.tensioncable.UI;

import android.text.TextUtils;
import android.widget.EditText;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.model.TensionCable;

import butterknife.BindView;
import butterknife.OnClick;

public class CableSettingAddActivity extends BaseActivity {
    /*** 编号 ***/
    @BindView(R.id.cable_number)
    EditText number;
    /*** 长度 ***/
    @BindView(R.id.cable_length)
    EditText length;
    /*** 面积 ***/
    @BindView(R.id.cable_area)
    EditText area;
    /*** 编号 ***/
    @BindView(R.id.cable_density)
    EditText density;


    @Override
    protected int getLayout() {
        return R.layout.activity_cable_setting_detail;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("桥索参数配置").setBack(true);
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

    @OnClick(R.id.part_cancel)
    void cancael() {
        onBackPressed();
    }

    @OnClick(R.id.part_save)
    void save() {
        /**编号**/
        String numberStr = number.getText().toString();
        /**长度**/
        String lengthStr = length.getText().toString();
        /**截面积**/
        String areaStr = area.getText().toString();
        /**密度**/
        String densityStr = density.getText().toString();
        if (TextUtils.isEmpty(numberStr)) {
            showToast("请填写编号");
            return;
        }
        if (TextUtils.isEmpty(lengthStr)) {
            showToast("请填写长度");
            return;
        }
        if (TextUtils.isEmpty(areaStr)) {
            showToast("请填写截面积");
            return;
        }
        if (TextUtils.isEmpty(densityStr)) {
            showToast("请填写密度");
            return;
        }
        //TODO writing your code
        for (TensionCable item : Constants.Object.getTensionCables(this)) {
            if (numberStr.equals(item.getName())) {
                showToast("编号重复");
                return;
            }
        }
        Constants.Object.addTensionCables(this, new TensionCable(numberStr, Double.valueOf(lengthStr), Double.valueOf(areaStr), Double.valueOf(densityStr)));
        onBackPressed();
    }
}
