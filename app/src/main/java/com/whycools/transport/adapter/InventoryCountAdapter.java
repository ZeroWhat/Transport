package com.whycools.transport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.service.NewStkcksumService;

/**
 * Created by Zero on 2016-12-12.
 */

public class InventoryCountAdapter extends CursorAdapter {

    public InventoryCountAdapter(Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        //获取view
        View view = View.inflate(context, R.layout.activity_inventory_count_lv_item, null);
        viewHolder.inventory_count_lv_item_tv_goodsname = view.findViewById(R.id.inventory_count_lv_item_tv_goodsname);
        viewHolder.inventory_ount_lv_item_tv_number = view.findViewById(R.id.inventory_ount_lv_item_tv_number);
        viewHolder.inventory_count_lv_item_tv_number = view.findViewById(R.id.inventory_count_lv_item_tv_number);
        viewHolder.inventory_count_item_ed_number = view.findViewById(R.id.inventory_count_item_ed_number);
        viewHolder.inventory_count_item_bt_save = view.findViewById(R.id.inventory_count_item_bt_save);
        viewHolder.inventory_count_item_bt_same = view.findViewById(R.id.inventory_count_item_bt_same);
        viewHolder.inventory_count_lv_item_layout = view.findViewById(R.id.inventory_count_lv_item_layout);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final ViewHolder viewHolder= (ViewHolder) view.getTag();
        //从cursor中获取值
        final String _id = cursor.getString(cursor.getColumnIndex("_id"));
        String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
        Log.i("goodsname", "--------------: "+goodsname);
        final String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal"));
        Log.i("qmyereal", "---------------: "+qmyereal);
        final String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount"));
        Log.i("qmyecount", "---------------: "+qmyeCount);
        //把数据设置到控件上面

        String isMove=cursor.getString(cursor.getColumnIndex("isMove"));
        Log.i("----------------", "isMove: "+isMove);
        viewHolder.inventory_count_lv_item_tv_goodsname.setText("机器名称："+goodsname);
        viewHolder.inventory_ount_lv_item_tv_number.setText("库存个数："+qmyereal);
        if(qmyeCount.length()>0){
            viewHolder.inventory_count_item_ed_number.setText(qmyeCount);
        }else{
            viewHolder.inventory_count_item_ed_number.setText(0+"");
        }
        if (isMove.equals("0")){
            viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else if(isMove.equals("1")){
            viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.red));
        }else{
            viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }


        viewHolder.inventory_count_item_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewStkcksumService newStkcksumService=new NewStkcksumService(context);
                newStkcksumService.updateStkcksum(viewHolder.inventory_count_item_ed_number.getText().toString(),_id);
                viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.red));

            }
        });
        viewHolder.inventory_count_item_bt_same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.inventory_count_item_ed_number.setText(qmyereal);
            }
        });


    }

    class ViewHolder{
        TextView inventory_count_lv_item_tv_goodsname = null;
        TextView inventory_ount_lv_item_tv_number = null;
        TextView inventory_count_lv_item_tv_number = null;
        EditText inventory_count_item_ed_number=null;
        Button inventory_count_item_bt_save=null;
        Button inventory_count_item_bt_same=null;
        LinearLayout inventory_count_lv_item_layout=null;
    }





}
