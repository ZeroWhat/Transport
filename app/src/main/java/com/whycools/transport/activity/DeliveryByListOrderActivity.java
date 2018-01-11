package com.whycools.transport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.adapter.AchatListViewCursorAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 按单发送
 * Created by Zero on 2017-01-06.
 */

public class DeliveryByListOrderActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout delivery_by_list_order_layout_back;
    private Spinner delivery_by_list_order_sp_shipdate;
    private EditText delivery_by_list_order_et;
    private ListView delivery_by_list_order_lv;
    private DBinventory dDBinventory;
    private AchatListViewCursorAdapter adapter;//适配器
    private Cursor cursor;
    private List<String > shipdate=new ArrayList<>();
    private ArrayAdapter<String> shipdateadapter;//日期适配器
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_list_order);
    }

    @Override
    public void initViews() {
        delivery_by_list_order_layout_back= findViewById(R.id.delivery_by_list_order_layout_back);
        delivery_by_list_order_sp_shipdate= findViewById(R.id.delivery_by_list_order_sp_shipdate);
        delivery_by_list_order_et= findViewById(R.id.delivery_by_list_order_et);
        delivery_by_list_order_lv= findViewById(R.id.delivery_by_list_order_lv);

    }

    private void showListView() {
        dDBinventory= new DBinventory(mContext);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        cursor = db.rawQuery("select * from shiplist where billno like '%" + delivery_by_list_order_et.getText() + "%' and billtype=? and shipdate=? and billno<>''", new String[]{"出",delivery_by_list_order_sp_shipdate.getSelectedItem().toString()});
        Log.i(TAG, "cursor个数2: "+cursor.getCount());

        adapter=new AchatListViewCursorAdapter(mContext, cursor);
        delivery_by_list_order_lv.setAdapter(adapter);
        delivery_by_list_order_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                Log.i(TAG, "_id: "+_id);
                String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 仓库id
                String carId = cursor.getString(cursor.getColumnIndex("carId")); // 车id
                String inoutFlag = cursor.getString(cursor .getColumnIndex("inoutFlag")); //是否为内机
                String carname = cursor.getString(cursor.getColumnIndex("carname")); // 车名称
                String seqno = cursor.getString(cursor.getColumnIndex("seqno")); //
                String billtype = cursor.getString(cursor.getColumnIndex("billtype")); //
                String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
                Log.i(TAG, "billno: "+billno);

                String company_address = cursor.getString(cursor.getColumnIndex("company_address")); //公司地址
                String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); //产品编号
                String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 产品名称
                String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); //内机个数
                String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));//已经出的内机个数
                String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 外机个数
                String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 已经出的外机个数
                Log.i(TAG, "in_qty: "+in_qty);
                Log.i(TAG, "cin_qty: "+cin_qty);
                Log.i(TAG, "out_qty: "+out_qty);
                Log.i(TAG, "cout_qty: "+cout_qty);
                String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 内机编码
                String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 外机编码
                String address = cursor.getString(cursor.getColumnIndex("address")); // 地址
                String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 电话号码
                String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); //是否已经完成
                String distance = cursor.getString(cursor.getColumnIndex("distance")); //距离值
                String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 是否已经发车
                String cm = cursor.getString(cursor.getColumnIndex("cm")); //
                Intent intent=new Intent(mContext,NoFinishShipListDetailsActivity.class);
                intent.putExtra("_id",_id);
                intent.putExtra("billno",billno);
                intent.putExtra("address",address);
                intent.putExtra("Phonenumber",Phonenumber);
                intent.putExtra("goodsId",goodsId);
                intent.putExtra("goodsname",goodsname);
                intent.putExtra("in_qty",in_qty);
                intent.putExtra("cin_qty",cin_qty);
                intent.putExtra("out_qty",out_qty);
                intent.putExtra("cout_qty",cout_qty);
                intent.putExtra("billtype",billtype);
                intent.putExtra("innerno",innerno);
                intent.putExtra("outerno",outerno);
                intent.putExtra("storeid",storeid);
                intent.putExtra("carId",carId);
                intent.putExtra("carname",carname);
                startActivity(intent);
            }
        });
    }


    @Override
    public void initListeners() {
        try {
            delivery_by_list_order_layout_back.setOnClickListener(this);
            delivery_by_list_order_sp_shipdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    loadData(delivery_by_list_order_sp_shipdate.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception e){
            Error.contentToTxt(DeliveryByListOrderActivity.this,"采购单页面按钮监听异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initData() {
        try {
            shipdate.add(DateUtile.getdata(0));
            shipdate.add(DateUtile.getdata(1));
            shipdate.add(DateUtile.getdata(2));
            shipdateadapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, shipdate);
            //仓库适配器设置样式
            shipdateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //仓库下拉框加载适配器
            delivery_by_list_order_sp_shipdate.setAdapter(shipdateadapter);
            delivery_by_list_order_sp_shipdate.setSelection(0);

            loadData(delivery_by_list_order_sp_shipdate.getSelectedItem().toString());


        }catch (Exception e){
            Error.contentToTxt(DeliveryByListOrderActivity.this,"采购单页面数据加载异常"+e.getMessage());//异常写入文档

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delivery_by_list_order_layout_back:
                finish();
                break;
        }
    }

    private void loadData(String shipdate){
        dDBinventory=new DBinventory(DeliveryByListOrderActivity.this);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        cursor = db.rawQuery("select * from shiplist where billtype=? and shipdate=?and billno<>''", new String[]{"出",shipdate});
        Log.i(TAG, "cursor个数: "+cursor.getCount());
        if (cursor.getCount()>0){
            adapter=new AchatListViewCursorAdapter(mContext, cursor);
            delivery_by_list_order_lv.setAdapter(adapter);
            delivery_by_list_order_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);
                    String _id = cursor.getString(cursor.getColumnIndex("_id"));
                    String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 仓库id
                    String carId = cursor.getString(cursor.getColumnIndex("carId")); // 车id
                    Log.i(TAG, "carId: "+carId);
                    String inoutFlag = cursor.getString(cursor .getColumnIndex("inoutFlag")); //是否为内机
                    String carname = cursor.getString(cursor.getColumnIndex("carname")); // 车名称
                    String seqno = cursor.getString(cursor.getColumnIndex("seqno")); //
                    String billtype = cursor.getString(cursor.getColumnIndex("billtype")); //
                    String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
                    Log.i(TAG, "billno: "+billno);
                    String company_address = cursor.getString(cursor.getColumnIndex("company_address")); //公司地址
                    String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); //产品编号
                    String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 产品名称
                    String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); //内机个数
                    String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));//已经出的内机个数
                    String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 外机个数
                    String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 已经出的外机个数
//                    Log.i(TAG, "in_qty: "+in_qty);
//                    Log.i(TAG, "cin_qty: "+cin_qty);
//                    Log.i(TAG, "out_qty: "+out_qty);
//                    Log.i(TAG, "cout_qty: "+cout_qty);
                    String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 内机编码
                    String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 外机编码
                    String address = cursor.getString(cursor.getColumnIndex("address")); // 地址
                    String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 电话号码
                    String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); //是否已经完成
                    String distance = cursor.getString(cursor.getColumnIndex("distance")); //距离值
                    String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 是否已经发车

                    String cm = cursor.getString(cursor.getColumnIndex("cm")); //
                    Intent intent=new Intent(mContext,NoFinishShipListDetailsActivity.class);
                    intent.putExtra("_id",_id);
                    intent.putExtra("billno",billno);
                    intent.putExtra("address",address);
                    intent.putExtra("Phonenumber",Phonenumber);
                    intent.putExtra("goodsId",goodsId);
                    intent.putExtra("goodsname",goodsname);
                    intent.putExtra("in_qty",in_qty);
                    intent.putExtra("cin_qty",cin_qty);
                    intent.putExtra("out_qty",out_qty);
                    intent.putExtra("cout_qty",cout_qty);
                    intent.putExtra("billtype",billtype);
                    intent.putExtra("innerno",innerno);
                    intent.putExtra("outerno",outerno);
                    intent.putExtra("storeid",storeid);
                    intent.putExtra("carId",carId);
                    intent.putExtra("carname",carname);
                    startActivity(intent);
                }
            });
        }else{
            TransparentDialogUtil.showErrorMessage(mContext,"当前没有数据，同步一下试试");
        }


        delivery_by_list_order_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0){
                    showListView();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
