package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Upstkcksum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-08-25.
 */

public class UpstkcksumService {
    DBinventory dDBinventory;
    Context context;

    public UpstkcksumService(Context context) {
        if(dDBinventory==null){
            this.dDBinventory = new DBinventory(context);
        }
        this.context = context;
    }

    // 添加数据
    public void addUpStkcksum(Upstkcksum upstkcksum) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();

        Log.i("Stores数据库表", "添加成功");
        db.execSQL(
                "insert into upstkcksum(" +
                        "auto_id," + // 1
                        "SumDate," + // a1
                        "storeid," + // 3
                        "time," + // 4
                        "qmyereal," + // 4
                        "goodsname," + // 4
                        "qmyeCount" + // 5
                        ") values(?,?,?,?,?,?,?)",
                new Object[] {
                        upstkcksum.getAuto_id(),
                        upstkcksum.getSumDate(),
                        upstkcksum.getStoreid(),
                        upstkcksum.getTime(),
                        upstkcksum.getQmyereal(),
                        upstkcksum.getGoodsname(),
                        upstkcksum.getQmyeCount()
                });
    }

    // 清除数据表所有内容
    public void clearUpStkcksum() {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        try {
            db.execSQL("delete from upstkcksum");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
        Log.i("Stores数据表", "数据清除了");
    }
    // 删出某一条数据
    public void deleteUpStkcksum(String _id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("delete from upstkcksum where _id=?", new String[] { _id });
        Log.i("Stores数据库", "成功删除");
    }
    // 修改某一条数据
    public void updateUpStkcksum(String qmyeCount,String  id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("update upstkcksum set qmyeCount=? where _id=?",
                new Object[] {qmyeCount, id });


    }


    // 查询某一条数据
    public Upstkcksum findUpStkcksum(String id) {
        Upstkcksum upstkcksum = new Upstkcksum();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from upstkcksum where _id=?",
                new String[] { id });
        if (cursor.moveToFirst()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // a1
            String sumDate = cursor.getString(cursor.getColumnIndex("SumDate")); // 3
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 4
            String time = cursor.getString(cursor.getColumnIndex("time")); // 5
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 5
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 5


            upstkcksum.set_id(_id);
            upstkcksum.setAuto_id(auto_id);
            upstkcksum.setSumDate(sumDate);
            upstkcksum.setStoreid(storeid);
            upstkcksum.setTime(time);
            upstkcksum.setQmyereal(qmyereal);
            upstkcksum.setQmyeCount(qmyeCount);
            upstkcksum.setGoodsname(goodsname);
        }
        Log.i("Stores数据库", "查找一条数据正确");
        cursor.close();
        return upstkcksum;
    }

    // 查找数据库两面全部数据
    public List<Upstkcksum> getAllUpStkcksum() {
        List<Upstkcksum> data = new ArrayList<Upstkcksum>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from upstkcksum", null);
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // a1
            String sumDate = cursor.getString(cursor.getColumnIndex("SumDate")); // 3
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 4
            String time = cursor.getString(cursor.getColumnIndex("time")); // 5
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 5
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 5

            Upstkcksum upstkcksum = new Upstkcksum();
            upstkcksum.set_id(_id);
            upstkcksum.setAuto_id(auto_id);
            upstkcksum.setSumDate(sumDate);
            upstkcksum.setStoreid(storeid);
            upstkcksum.setTime(time);
            upstkcksum.setQmyereal(qmyereal);
            upstkcksum.setQmyeCount(qmyeCount);
            upstkcksum.setGoodsname(goodsname);


            data.add(upstkcksum);
        }
        Log.i("upstkcksum数据库", "查找所有数据正确");
        cursor.close();
        return data;
    }
}
