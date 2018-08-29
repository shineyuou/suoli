package com.annis.tensioncable.adapter;

import android.content.Context;
import android.view.View;

import com.annis.appbase.base.BaseViewHolder;
import com.annis.appbase.base.RVBaseAdapter;
import com.annis.tensioncable.R;
import com.annis.tensioncable.model.TensionCable;

public class CableSettingSelectorAdapter extends RVBaseAdapter<TensionCable> {
    public CableSettingSelectorAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_selector_tensions;
    }

    @Override
    public void bindHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.text, getItem(position).getName());
        holder.getView(R.id.iv_status).setVisibility(getItem(position).isClicked() ? View.VISIBLE : View.GONE);
    }
}
