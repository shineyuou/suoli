package com.annis.tensioncable.adapter;

import android.content.Context;

import com.annis.appbase.base.BaseViewHolder;
import com.annis.appbase.base.RVBaseAdapter;
import com.annis.tensioncable.R;
import com.annis.tensioncable.model.TensionCable;

public class TensionCableAdapter extends RVBaseAdapter<TensionCable> {
    public TensionCableAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_equipment_status;
    }

    @Override
    public void bindHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.item_content, getItem(position).getName());
    }
}
