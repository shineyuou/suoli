package com.annis.appbase.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecylerView的适配器
 */
public abstract class RVBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    List<T> mDatas;
    int layoutId;
    Context context;
    OnRVItemClickListener itemClickListener;


    public RVBaseAdapter(Context context) {
        this.context = context;
        mDatas = new ArrayList<>();
    }


    public abstract int getLayoutId();

    public Context getContext() {
        return context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.layoutId == 0) {
            this.layoutId = getLayoutId();
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        fillItem(view);
        BaseViewHolder holder = new BaseViewHolder(view, itemClickListener);
        return holder;
    }

    public void setOnRvItemClickListener(OnRVItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * 依据实际设计填充item
     *
     * @param itemView
     */
    public void fillItem(View itemView) {

    }

    /**
     * 数据绑定
     *
     * @param holder
     * @param position
     */
    public abstract void bindHolder(BaseViewHolder holder, int position);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setOnItemClick(position);
        bindHolder(holder, position);
    }

    public void addData(List<T> list) {
        if (list != null)
            mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        if (datas == null) return;
        if (mDatas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
        } else {
            mDatas = datas;
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }


    /**
     * 删除指定数据条目
     *
     * @param model
     */
    public void removeItem(T model) {
        removeItem(mDatas.indexOf(model));
    }

    /**
     * 在指定位置添加数据条目
     *
     * @param position
     * @param model
     */
    public void addItem(int position, T model) {
        mDatas.add(position, model);
        notifyDataSetChanged();
    }


    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}