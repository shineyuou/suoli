package com.annis.tensioncable.UI;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.adapter.EquipmentAdapter;
import com.annis.tensioncable.adapter.TensionCableAdapter;
import com.annis.tensioncable.model.Equipment;
import com.annis.tensioncable.model.TensionCable;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TensionCableListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    TensionCableAdapter adapter;
    List<TensionCable> datas = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_analizy_list;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
        adapter = new TensionCableAdapter(this);
        recyclerView.setAdapter(adapter);
        //添加Android自带的分割线
        adapter.setOnRvItemClickListener((itemView, position) -> {
            startAcitvity(AnalyzeActivity.class, adapter.getItem(position));
        });
    }

    @Override
    protected void laodData() {
        datas = Constants.Object.getTensionCables(this);
        adapter.setDatas(datas);
    }

}
