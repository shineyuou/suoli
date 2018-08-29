package com.annis.appbase.base;


import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    OnRVItemClickListener rvItemClickListener;
    SparseArray<View> views;

    public BaseViewHolder(View itemView, OnRVItemClickListener itemClickListener) {
        super(itemView);
        views = new SparseArray<>();
        this.rvItemClickListener = itemClickListener;
    }

    public void setOnItemClick(final int position) {
        itemView.setOnClickListener(v -> {
            if (rvItemClickListener != null) {
                rvItemClickListener.onClick(itemView, position);
            }
        });

    }


    public <T extends View> T getView(int viewId) {
        View view = (View) views.get(viewId);
        if (view == null) {
            view = (T) itemView.findViewById(viewId);
        }
        return (T) view;
    }

    /**
     * 给子节点上的itemview 设置点击事件
     *
     * @param viewId
     * @param childClickListener
     */
    public void bindChildClickListener(int viewId, final OnRVItemChildClickListener childClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childClickListener != null) {
                    childClickListener.onItemChildClick(v, getAdapterPosition());
                }
            }
        });
    }

    /**
     * 直接设置数据，后续自行添加
     *
     * @param id
     * @param text
     */

    public void setText(int id, String text) {
        TextView view = getView(id);
        view.setText(text);
    }

    public void setImageRes(int id, int resId) {
        ImageView view = getView(id);
        view.setImageResource(resId);
    }

    public void setBackground(int id, int resId) {
        View view = getView(id);
        view.setBackgroundResource(resId);
    }
}
