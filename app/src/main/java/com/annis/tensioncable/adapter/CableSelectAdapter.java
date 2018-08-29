package com.annis.tensioncable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.annis.appbase.base.BaseViewHolder;
import com.annis.appbase.base.RVBaseAdapter;
import com.annis.tensioncable.R;
import com.annis.tensioncable.model.TensionCable;

public class CableSelectAdapter extends RVBaseAdapter<TensionCable> {
    public CableSelectAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_equipment_status;
    }

    @Override
    public void bindHolder(BaseViewHolder holder, int position) {
        ((TextView)holder.getView(R.id.item_content)).setTextColor(Color.BLACK);
        holder.setText(R.id.item_content, getItem(position).getName());
    }
}