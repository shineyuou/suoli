package com.annis.tensioncable.UI;

import android.view.View;
import android.widget.CheckBox;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.adapter.CableSettingSelectorAdapter;
import com.annis.tensioncable.model.Equipment;
import com.annis.tensioncable.model.TensionCable;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class CableSettingActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    List<TensionCable> cableList = new ArrayList<>();
    CableSettingSelectorAdapter adapter;
    Equipment item;

    @Override
    protected int getLayout() {
        return R.layout.activity_select_cable_to_setting;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("桥索参数设置").setBack(true).setLeftTitle("返回").setRightViewId(R.layout.part_right_add);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        if (getIntentObj() != null) {
            if (getIntentObj() instanceof Equipment) {
                //TODO 如果代码走到这里来了,说明是通过设备页面来的
                item = (Equipment) getIntentObj();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
        adapter = new CableSettingSelectorAdapter(this);
        adapter.setDatas(cableList);
        recyclerView.setAdapter(adapter);


        adapter.setOnRvItemClickListener((itemView, position) -> {
            cableList.get(position).setClicked(!cableList.get(position).isClicked());
            adapter.setDatas(cableList);
        });

    }

    @Override
    protected void laodData() {
        //TODO 更换成动态数据
        cableList = Constants.Object.getTensionCables(this);
        adapter.setDatas(cableList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        laodData();
    }

    /**
     * 添加按钮点击
     */
    @OnClick(R.id.right_view)
    void rightView() {
        ArrayList<TensionCable> selected = new ArrayList<>();
        for (TensionCable item : cableList) {
            if (item.isClicked()) {
                selected.add(item);
            }
        }
        startAcitvity(CableSettingAddActivity.class);
    }

    /**
     * 删除 按钮点击
     *
     * @param view
     */
    @OnClick(R.id.cable_select_delete)
    void delete(View view) {
        //TODO write your code
        List<TensionCable> ListTemp = new ArrayList<>();
        for (TensionCable item : cableList) {
            if (item.isClicked()) {
                continue;
            }
            ListTemp.add(item);
        }
        Constants.Object.saveTensionCables(this, ListTemp);
        laodData();
    }

    /**
     * 全选
     *
     * @param view
     */
    @OnClick(R.id.cb_all)
    void all(View view) {
        //TODO 全选逻辑
        boolean isClicked = ((CheckBox) view).isChecked();
        showToast(isClicked ? "全选" : "全不选");
        for (TensionCable item : cableList) {
            item.setClicked(isClicked);
        }
        adapter.setDatas(cableList);
    }
}
