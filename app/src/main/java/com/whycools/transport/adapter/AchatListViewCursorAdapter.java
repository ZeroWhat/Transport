package com.whycools.transport.adapter;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.whycools.transport.R;


/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class AchatListViewCursorAdapter extends CursorAdapter{
    public AchatListViewCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        //获取view
        View view = View.inflate(context, R.layout.activity_achat_lv_item, null);
        //寻找控件
        viewHolder.tv= view.findViewById(R.id.achat_lv_item_tv);

        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder= (ViewHolder) view.getTag();
        //从cursor中获取值
        String billno = cursor.getString(cursor.getColumnIndex("billno"));
        String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
        String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
        //把数据设置到控件上面
        viewHolder.tv.setText(billno+","+billtype+","+goodsname);

    }

    class ViewHolder{
        TextView tv;
    }
}
