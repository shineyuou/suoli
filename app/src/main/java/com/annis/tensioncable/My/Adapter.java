package com.annis.tensioncable.My;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annis.tensioncable.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

public class Adapter extends SwipeMenuRecyclerView.Adapter<Adapter.ViewHolder>{

    private List<FileBean> mDataList;

    public Adapter(List<FileBean> s){
        mDataList=s;

    }
    public void notifyDataSetChanged(List<FileBean> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_main,
                parent,false));
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        holder.setDate(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList==null?0:mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView file_type;
        TextView file_name;
        TextView file_time;
        TextView file_size;

        ViewHolder(View itemView) {
            super(itemView);
            file_type = itemView.findViewById(R.id.file_type);
            file_name = itemView.findViewById(R.id.file_name);
            file_time = itemView.findViewById(R.id.file_time);
            file_size = itemView.findViewById(R.id.file_size);
        }
        @SuppressLint("SetTextI18n")
        void setDate(FileBean file){
            file_type.setImageResource(file.getFile_type());
            file_name.setText(file.getFile_name());
            file_time.setText(file.getFile_time());
            file_size.setText(""+file.getFile_size());

        }
    }
}
