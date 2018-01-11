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
 * Created by Zero on 2016-12-07.
 */

public class NoFinishShipListDetailsAdapter extends BaseAdapter {
    private List<ScanRec> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private NoFinishShipListDetailsAdapter.ViewHolder viewHolder = null;
    public NoFinishShipListDetailsAdapter(Context context, List<ScanRec> list) {
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
            viewHolder = new NoFinishShipListDetailsAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.activity_nofinishshiplistdetails_lv_item,parent,false);
            viewHolder.nofinishshiplistdetails_lv_item_tv_Billno = convertView.findViewById(R.id.nofinishshiplistdetails_lv_item_tv_Billno);
            viewHolder.nofinishshiplistdetails_lv_item_tv_barcodes = convertView.findViewById(R.id.nofinishshiplistdetails_lv_item_tv_barcodes);
            viewHolder.nofinishshiplistdetails_lv_item_tv_Time = convertView.findViewById(R.id.nofinishshiplistdetails_lv_item_tv_Time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NoFinishShipListDetailsAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.nofinishshiplistdetails_lv_item_tv_Billno.setText(list.get(position).getBillno());
        viewHolder.nofinishshiplistdetails_lv_item_tv_barcodes.setText(list.get(position).getBarcodes());
        viewHolder.nofinishshiplistdetails_lv_item_tv_Time.setText(list.get(position).getTime());
        return convertView;
    }

    private  class ViewHolder {
        TextView nofinishshiplistdetails_lv_item_tv_Billno = null;
        TextView nofinishshiplistdetails_lv_item_tv_barcodes = null;
        TextView nofinishshiplistdetails_lv_item_tv_Time = null;

    }
}
