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
 * 已经完成清单的适配器
 * Created by Zero on 2016-12-09.
 */

public class FinishShipListAdapter extends BaseAdapter {
    private List<ShipList> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private FinishShipListAdapter.ViewHolder viewHolder = null;
    public FinishShipListAdapter(Context context, List<ShipList> list) {
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
            viewHolder = new FinishShipListAdapter.ViewHolder();

            convertView = layoutInflater.inflate(R.layout.fragment_finishshiplist_lv_item,parent,false);
            viewHolder.finishshiplist_lv_item_tv_phonenumber = convertView.findViewById(R.id.finishshiplist_lv_item_tv_phonenumber);
            viewHolder.finishshiplist_lv_item_tv_billno = convertView.findViewById(R.id.finishshiplist_lv_item_tv_billno);
            viewHolder.finishshiplist_lv_item_goodsname = convertView.findViewById(R.id.finishshiplist_lv_item_goodsname);
            viewHolder.finishshiplist_lv_item_tv_address = convertView.findViewById(R.id.finishshiplist_lv_item_tv_address);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FinishShipListAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.finishshiplist_lv_item_tv_phonenumber.setText("电话号码："+list.get(position).getPhonenumber());
        viewHolder.finishshiplist_lv_item_tv_billno.setText("订单编号："+list.get(position).getBillno());
        viewHolder.finishshiplist_lv_item_goodsname.setText("机器名称："+list.get(position).getGoodsname());
        viewHolder.finishshiplist_lv_item_tv_address.setText("地址："+list.get(position).getAddress());

        return convertView;
    }

    private  class ViewHolder {
        TextView finishshiplist_lv_item_tv_phonenumber = null;
        TextView finishshiplist_lv_item_tv_billno = null;
        TextView finishshiplist_lv_item_goodsname = null;
        TextView finishshiplist_lv_item_tv_address = null;


    }
}
