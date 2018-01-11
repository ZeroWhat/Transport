package com.whycools.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.entity.ScanRec;

import java.util.List;

/**
 * Created by Zero on 2016-12-08.
 */

public class ScanOnceAdapter extends BaseAdapter {
    private List<ScanRec> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private ScanOnceAdapter.ViewHolder viewHolder = null;
    public ScanOnceAdapter(Context context, List<ScanRec> list) {
        this.list = list;
        this.context=context;
        layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ScanOnceAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.activity_scanonce_lv_item,parent,false);
            viewHolder.scanonce_lv_item_tv_barcodes = convertView.findViewById(R.id.scanonce_lv_item_tv_barcodes);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ScanOnceAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.scanonce_lv_item_tv_barcodes.setText(list.get(position).getBarcodes()+"\r\n"+list.get(position).getGoodsname());

        return convertView;
    }

    private  class ViewHolder {
        TextView scanonce_lv_item_tv_barcodes = null;


    }
}
