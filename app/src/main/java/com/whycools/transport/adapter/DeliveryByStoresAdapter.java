package com.whycools.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.entity.Stores;

import java.util.List;

/**
 * Created by Zero on 2017-02-28.
 */

public class DeliveryByStoresAdapter extends BaseAdapter {

    private List<Stores> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private DeliveryByStoresAdapter.ViewHolder viewHolder = null;
    public DeliveryByStoresAdapter(Context context, List<Stores> list) {
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
            viewHolder = new DeliveryByStoresAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.activity_delivery_by_car_lv_item,parent,false);
            viewHolder.delivery_by_car_lv_item_tv_info = convertView.findViewById(R.id.delivery_by_car_lv_item_tv_info);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeliveryByStoresAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.delivery_by_car_lv_item_tv_info.setText(list.get(position).getStorename());
        return convertView;
    }

    private  class ViewHolder {
        TextView delivery_by_car_lv_item_tv_info = null;

    }
}