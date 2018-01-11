package com.whycools.transport.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;

import java.util.List;

/**
 * 未完成适配器
 * Created by Zero on 2016-12-06.
 */

public class NoFinishShipListAdapter extends BaseAdapter {

    private List<ShipList> list = null;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private  ViewHolder viewHolder = null;
    public NoFinishShipListAdapter(Context context, List<ShipList> list) {
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
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.fragment_nofinishshiplist_lv_item,parent,false);
            viewHolder.nofinishshiplist_lv_item_tv_address = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_address);
            viewHolder.nofinishshiplist_lv_item_tv_billno = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_billno);
            viewHolder.nofinishshiplist_lv_item_bt_start= convertView.findViewById(R.id.nofinishshiplist_lv_item_bt_start);
            viewHolder.nofinishshiplist_lv_item_tv_finish= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_finish);
            viewHolder.nofinishshiplist_lv_item_tv_distance= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nofinishshiplist_lv_item_tv_address.setText(list.get(position).getAddress());
        viewHolder.nofinishshiplist_lv_item_tv_billno.setText(list.get(position).getBillno());
        viewHolder.nofinishshiplist_lv_item_bt_start.setText(list.get(position).getIsStart());
        viewHolder.nofinishshiplist_lv_item_tv_finish.setText(list.get(position).getIsfinish());
        viewHolder.nofinishshiplist_lv_item_tv_distance.setText(list.get(position).getDistance());
        viewHolder.nofinishshiplist_lv_item_bt_start.setOnClickListener(new btClick(position));//发车按钮监听
        return convertView;
    }

    private  class ViewHolder {
        TextView nofinishshiplist_lv_item_tv_address = null;
        TextView nofinishshiplist_lv_item_tv_billno = null;
        Button nofinishshiplist_lv_item_bt_start=null;
        TextView nofinishshiplist_lv_item_tv_finish=null;
        TextView nofinishshiplist_lv_item_tv_distance=null;
    }

    /*
* 此为listview条目中的按钮点击事件的写法
*/
    class btClick implements View.OnClickListener {
        private int position;
        public btClick(int pos){  // 在构造时将position传给它，这样就知道点击的是哪个条目的按钮
            this.position = pos;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.nofinishshiplist_lv_item_bt_start:

                    if( list.get(position).getIsfinish().equals("未完成")){//list.get(position).getIsStart().equals("完成")&&list.get(position).getIn_qty().equals(list.get(position).getCin_qty())&&list.get(position).getOut_qty().equals(list.get(position).getCout_qty()
                        Log.i("完成按钮", "getIn_qty: "+list.get(position).getIn_qty()+">>>>>>>>>>>>>>getCin_qty"+list.get(position).getCin_qty()+">>>>>>getOut_qty"+list.get(position).getOut_qty()+">>>>>>>>>>getCout_qty"+list.get(position).getCout_qty());
//                        if (list.get(position).getIn_qty().equals(list.get(position).getCin_qty())&&list.get(position).getOut_qty().equals(list.get(position).getCout_qty())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("你确定扫码完成了吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                list.get(position).setAddress(list.get(position).getAddress());
                                list.get(position).setBillno(list.get(position).getBillno());
                                list.get(position).setPhonenumber(list.get(position).getPhonenumber());
                                list.get(position).setDistance(list.get(position).getDistance());
                                list.get(position).setStoreid(list.get(position).getStoreid());
                                list.get(position).setGoodsname(list.get(position).getGoodsname());

                                list.get(position).setCarId(list.get(position).getCarId());
                                list.get(position).setCarname(list.get(position).getCarname());
                                list.get(position).setBilltype(list.get(position).getBilltype());
                                list.get(position).setGoodsId(list.get(position).getGoodsId());
                                list.get(position).setInnerno(list.get(position).getInnerno());
                                list.get(position).setOuterno(list.get(position).getOuterno());
                                list.get(position).setInoutFlag(list.get(position).getInoutFlag());
                                list.get(position).setIn_qty(list.get(position).getIn_qty());
                                list.get(position).setOut_qty(list.get(position).getOut_qty());
                                list.get(position).setCin_qty(list.get(position).getCin_qty());
                                list.get(position).setCout_qty(list.get(position).getCout_qty());
                                list.get(position).setIsStart("完成");




                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        builder.create().show();
//                        }else {
//                            Toast.makeText(context, "扫码还没有完成", Toast.LENGTH_SHORT).show();
//                        }
                    }

            }

        }

    }




}
