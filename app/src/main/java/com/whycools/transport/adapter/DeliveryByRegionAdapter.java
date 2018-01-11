package com.whycools.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.entity.ShipList;

import java.util.List;

/**
 * Created by Zero on 2017-02-16.
 */

public class DeliveryByRegionAdapter extends BaseAdapter {

    private List<ShipList> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private DeliveryByRegionAdapter.ViewHolder viewHolder = null;
    public DeliveryByRegionAdapter(Context context, List<ShipList> list) {
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
            viewHolder = new DeliveryByRegionAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.activity_delivery_by_car_lv_item,parent,false);
            viewHolder.delivery_by_car_lv_item_tv_info = convertView.findViewById(R.id.delivery_by_car_lv_item_tv_info);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeliveryByRegionAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.delivery_by_car_lv_item_tv_info.setText(list.get(position).getDistrict());
        return convertView;
    }

    private  class ViewHolder {
        TextView delivery_by_car_lv_item_tv_info = null;

    }
}

