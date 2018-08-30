package com.annis.tensioncable.UI;

import android.content.Intent;
import android.widget.Toast;

import com.annis.appbase.base.BaseActivity;
import com.annis.appbase.base.BasePresenter;
import com.annis.appbase.base.TitleBean;
import com.annis.tensioncable.R;
import com.annis.tensioncable.Utils.Constants;
import com.annis.tensioncable.adapter.CableSelectAdapter;
import com.annis.tensioncable.model.TensionCable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SelectCableActivity extends BaseActivity {
    @BindView(R.id.activity_select_cable)
    RecyclerView rv;
    CableSelectAdapter adapter;
    List<TensionCable> cables = new ArrayList<>();
    private String filepath;

    @Override
    protected int getLayout() {
        return R.layout.activity_select_cable;
    }

    @Override
    protected TitleBean getMyTitle() {
        return new TitleBean("选择拉索").setBack(true).setLeftTitle("返回");
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initViewAndEvent() {
        Intent intent = getIntent();
        filepath = intent.getStringExtra("filepath");
        Toast.makeText(this, filepath,Toast.LENGTH_SHORT).show();
        rv.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.custom_divider)));
        rv.addItemDecoration(decoration);
        adapter = new CableSelectAdapter(this);
        rv.setAdapter(adapter);
        adapter.setOnRvItemClickListener((itemView, position) -> {
            selected(position);
        });

    }

    @Override
    protected void laodData() {
        cables = Constants.Object.getTensionCables(this);
        adapter.setDatas(cables);
    }

    @Override
    protected void onResume() {
        super.onResume();
        laodData();
    }

    /**
     * 列表中被选中的 item index
     *
     * @param selectedIndex
     */
    private void selected(int selectedIndex) {
        TensionCable cable = cables.get(selectedIndex);
        showToast(cable.getName() + "被选中");
        startAcitvity(AnalyzeActivity.class, cable,filepath);
    }
}
