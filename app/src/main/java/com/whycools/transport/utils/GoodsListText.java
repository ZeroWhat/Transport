package com.whycools.transport.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zero on 2017-01-05.
 */

public class GoodsListText {
    private List<Goods> list=new ArrayList<>();
    private DBinventory dDBinventory;
    private static final String TAG="标准表，清单表，临时单个表关联";
    private Context context;
    public GoodsListText(Context context){
        this.context=context;
    }
    public List<Goods> getScanDataCompare(String id){
        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Log.i(TAG, "开始查询 ");
        Cursor cursor = db.rawQuery("select b.*,b.barcodeLeftpos as bbarcodeLeftpos,c.*,a.*,a._id  ,a.in_qty-a.cin_qty as inleft,a.out_qty-a.cout_qty as outleft  FROM shiplist a left join barcode_header b on a.goodsId=b.goodsId " +
                " left join code_header_multi c on b.goodsId=c.goodsId   where a._id=? ", new String []{id});

        Log.i(TAG, "cursor个数: "+cursor.getCount());
        while (cursor.moveToNext()){
            Goods goods=new Goods();
            Log.i(TAG, ">>>>>>>>>>>>>>>>>>发货清单<<<<<<<<<<<<<<<<<");
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            Log.i(TAG, "发货清单:aid "+_id);
            String storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            Log.i(TAG, "发货清单:storeid "+storeid);
            String carId = cursor.getString(cursor.getColumnIndex("carId"));
            Log.i(TAG, "发货清单:carId "+carId);
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag"));
            Log.i(TAG, "发货清单:inoutFlag "+inoutFlag);
            String carname = cursor.getString(cursor.getColumnIndex("carname"));
            Log.i(TAG, "发货清单:carname "+carname);
            String shipdate = cursor.getString(cursor.getColumnIndex("shipdate"));
            Log.i(TAG, "发货清单:shipdate "+shipdate);
            String seqno = cursor.getString(cursor.getColumnIndex("seqno"));
            Log.i(TAG, "发货清单:seqno "+seqno);
            String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
            Log.i(TAG, "发货清单:billtype "+billtype);
            String billno = cursor.getString(cursor.getColumnIndex("billno"));
            Log.i(TAG, "发货清单:billno "+billno);
            String company_address = cursor.getString(cursor.getColumnIndex("company_address"));
            Log.i(TAG, "发货清单:company_address "+company_address);
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId"));
            Log.i(TAG, "发货清单:goodsId "+goodsId);
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty"));
            Log.i(TAG, "发货清单:in_qty "+in_qty);
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));
            Log.i(TAG, "发货清单:cin_qty "+cin_qty);
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));
            Log.i(TAG, "发货清单:out_qty "+out_qty);
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty"));
            Log.i(TAG, "发货清单:cout_qty "+cout_qty);
            String innerno = cursor.getString(cursor.getColumnIndex("innerno"));
            Log.i(TAG, "发货清单:innerno "+innerno);
            String outerno = cursor.getString(cursor.getColumnIndex("outerno"));
            Log.i(TAG, "发货清单:outerno "+outerno);
            String address = cursor.getString(cursor.getColumnIndex("address"));
            Log.i(TAG, "发货清单:address "+address);
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber"));
            Log.i(TAG, "发货清单:Phonenumber "+Phonenumber);
            String distance = cursor.getString(cursor.getColumnIndex("distance"));
            Log.i(TAG, "发货清单:distance "+distance);
            String isStart = cursor.getString(cursor.getColumnIndex("isStart"));
            Log.i(TAG, "发货清单:isStart "+isStart);
            String stateId = cursor.getString(cursor.getColumnIndex("stateId"));
            Log.i(TAG, "发货清单:stateId "+stateId);
            String cm = cursor.getString(cursor.getColumnIndex("cm"));
            Log.i(TAG, "发货清单:cm "+cm);


            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>标准表<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            Log.i(TAG, "标准表:barcode_header_in"+barcode_header_in);
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            Log.i(TAG, "标准表:barcode_header_out"+barcode_header_out);
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("bbarcodeLeftpos"));
            Log.i(TAG, "标准表:barcodeLeftpos>>>>>>>>><<<<<<<<<<<<<<<<<"+barcodeLeftpos);
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            Log.i(TAG, "标准表:minLen"+minLen);



            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>临时单个条码表<<<<<<<<<<<<<<<<<<<<<<<<<");
            String is_in = cursor.getString(cursor.getColumnIndex("is_in"));
            Log.i(TAG, "临时单个条码表:is_in: "+is_in);
            String barcode = cursor.getString(cursor.getColumnIndex("barcode"));
            Log.i(TAG, "临时单个条码表:barcode: "+barcode);
            if (is_in!=null&&cursor.getCount()>1){
                Log.i(TAG, "个数大于1");
                if(is_in.equals("1")){
                    goods.setInoutFlag(inoutFlag);
                    goods.setBarcodeLeftPos(barcodeLeftpos);
                    goods.setMinLen(minLen);
                    goods.setBarcode_in(barcode);
                    goods.setBarcode_out("");
                }else if(is_in.equals("0")){
                    goods.setInoutFlag(inoutFlag);
                    goods.setBarcodeLeftPos(barcodeLeftpos);
                    goods.setMinLen(minLen);
                    goods.setBarcode_in("");
                    goods.setBarcode_out(barcode);
                }
            }else{
                Log.i(TAG, "个数没有大于1");
                goods.setInoutFlag(inoutFlag);
                goods.setBarcodeLeftPos(barcodeLeftpos);
                goods.setMinLen(minLen);
                goods.setBarcode_in(barcode_header_in);
                goods.setBarcode_out(barcode_header_out);
            }
            list.add(goods);
        }
        return list;
    }
}
