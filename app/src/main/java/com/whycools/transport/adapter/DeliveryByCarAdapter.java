package com.whycools.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.activity.DeliveryByCarActivity;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.utils.Error;

import java.util.List;

/**
 * Created by Zero on 2016-12-06.
 */

public class DeliveryByCarAdapter extends BaseAdapter {

    private List<ShipList> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private DeliveryByCarAdapter.ViewHolder viewHolder = null;
    public DeliveryByCarAdapter(Context context, List<ShipList> list) {
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
            viewHolder = new DeliveryByCarAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.activity_delivery_by_car_lv_item,parent,false);
            viewHolder.delivery_by_car_lv_item_tv_info = convertView.findViewById(R.id.delivery_by_car_lv_item_tv_info);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeliveryByCarAdapter.ViewHolder) convertView.getTag();
        }
        Error.contentToTxt(context,"按车扫描车名称列表"+list.get(position).getCarname());
        viewHolder.delivery_by_car_lv_item_tv_info.setText(list.get(position).getCarname());
        return convertView;
    }

    private  class ViewHolder {
        TextView delivery_by_car_lv_item_tv_info = null;

    }
}
