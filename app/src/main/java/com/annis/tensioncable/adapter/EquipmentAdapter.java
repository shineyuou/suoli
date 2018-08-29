package com.annis.tensioncable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextClock;
import android.widget.TextView;

import com.annis.appbase.base.BaseViewHolder;
import com.annis.appbase.base.RVBaseAdapter;
import com.annis.tensioncable.R;
import com.annis.tensioncable.model.Equipment;


public class EquipmentAdapter extends RVBaseAdapter<Equipment> {
    public EquipmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_equipment_status;
    }

    @Override
    public void bindHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.item_content, getItem(position).getName());
        switch (getItem(position).getStatus()) {
            /**
             * 测量状态 0: 已停止 1.测量中 2.已暂停
             */
            case 0:
                holder.setText(R.id.item_status_tv, "已停止");
                holder.setImageRes(R.id.item_status_iv, R.drawable.tz_two);
                ((TextView) holder.getView(R.id.item_status_tv)).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                holder.setImageRes(R.id.item_status_iv, R.drawable.ks);
                holder.setText(R.id.item_status_tv, "测量中");
                ((TextView) holder.getView(R.id.item_status_tv)).setTextColor(Color.rgb(255, 0, 0));
                break;
            case 2:
                holder.setText(R.id.item_status_tv, "已暂停");
                holder.setImageRes(R.id.item_status_iv, R.drawable.zt_two);
                ((TextView) holder.getView(R.id.item_status_tv)).setTextColor(Color.rgb(0, 89, 255));
                break;
        }
    }
}
