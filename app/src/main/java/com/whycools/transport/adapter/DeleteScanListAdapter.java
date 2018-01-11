package com.whycools.transport.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.activity.DeleteScanListDetailsActivity;
import com.whycools.transport.entity.ScanRec;

import java.util.List;

/**
 * Created by Zero on 2016-12-12.
 */

public class DeleteScanListAdapter extends RecyclerView.Adapter {
    private List<ScanRec> list = null;
    private Context context;


    public DeleteScanListAdapter(Context context, List<ScanRec> list) {
        this.context=context;
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_scanonce_lv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).scanonce_lv_item_tv_barcodes.setText("产品型号："+list.get(position).getGoodsname()+"\r\n单据编号："+list.get(position).getBillno()+"\r\n条形码："+list.get(position).getBarcodes()+"\r\n"+list.get(position).getBilltype());
        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView scanonce_lv_item_tv_barcodes = null;

        public ViewHolder(View view) {
            super(view);
            scanonce_lv_item_tv_barcodes = view.findViewById(R.id.scanonce_lv_item_tv_barcodes);
        }



    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }





}

