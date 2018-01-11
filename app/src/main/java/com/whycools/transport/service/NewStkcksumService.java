package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Stkcksum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-08-08.
 */

public class NewStkcksumService {
    DBinventory dDBinventory;
    Context context;

    public NewStkcksumService(Context context) {
        if(dDBinventory==null){
            this.dDBinventory = new DBinventory(context);
        }
        this.context = context;
    }

    // 添加数据
    public void addStkcksum(String auto_id,String sumdate,String storeid,String goodsno,String classname,String goodsname,String qmyereal,String qmyeCount,String isMove) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();

        Log.i("Stkcksum数据库表", "新表添加成功");
        db.execSQL(
                "insert into newstkcksum(" +
                        "auto_id," + // 1
                        "sumdate," + // a1
                        "storeid," + // 3
                        "goodsno," + // 5
                        "classname," + // a1
                        "goodsname," + // 6
                        "qmyereal," + // 7
                        "qmyeCount," + // 8
                        "isMove" + // 10
                        ") values(?,?,?,?,?,?,?,?,?)",
                new Object[] {
                        auto_id,
                        sumdate,
                        storeid,
                        goodsno,
                        classname,
                        goodsname,
                        qmyereal,
                        qmyeCount,
                        isMove
                });
    }

    // 清除数据表所有内容
    public void clearStkcksum() {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM newstkcksum");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
        Log.i("Stkcksum数据表", "数据清除了");
    }
    // 删出某一条数据
    public void deleteStkcksum(String auto_id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("delete from newstkcksum where auto_id=?", new String[] { auto_id });
        Log.i("Stkcksum数据库", "成功删除");
    }
    // 修改某一条数据
    public void updateStkcksum(String  qmyeCount,String id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("update newstkcksum set qmyeCount=? , isMove=? where _id=?",
                new Object[] {qmyeCount ,"1",id});
        Log.i("Stkcksum数据库", "修改数据成功");

    }
    // 修改某一条数据
    public void updateStkcksum2(String  qmyeCount,String auto_id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("update newstkcksum set qmyeCount=? , isMove=? where auto_id=?",
                new Object[] {qmyeCount ,"1",auto_id});
        Log.i("Stkcksum数据库", "修改数据成功");

    }


    // 查询某一条数据
    public Stkcksum findStkcksum(String auto_id) {
        Stkcksum stkcksum = new Stkcksum();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from newstkcksum where auto_id=?",
                new String[] { auto_id });
        if (cursor.moveToFirst()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

            String sumdate = cursor.getString(cursor.getColumnIndex("sumdate")); // a1

            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3

            String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8
            String isCounted = cursor.getString(cursor.getColumnIndex("isCounted")); // 9
            String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10
            stkcksum.set_id(_id);
            stkcksum.setAuto_id(_auto_id);
            stkcksum.setSumdate(sumdate);
            stkcksum.setStoreid(storeid);

            stkcksum.setGoodsno(goodsno);
            stkcksum.setGoodsname(goodsname);
            stkcksum.setQmyereal(qmyereal);
            stkcksum.setQmyeCount(qmyeCount);
            stkcksum.setIsCounted(isCounted);
            stkcksum.setIsMove(isMove);


        }
        Log.i("Stkcksum数据库", "查找一条数据正确");
        cursor.close();
        return stkcksum;
    }

    // 查询某一条数据
    public Stkcksum findByIDStkcksum(String id) {
        Stkcksum stkcksum = new Stkcksum();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from newstkcksum where _id=?",
                new String[] { id });
        if (cursor.moveToFirst()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

            String sumdate = cursor.getString(cursor.getColumnIndex("SumDate")); // a1

            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
            String classname = cursor.getString(cursor.getColumnIndex("classname")); // 4
            String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8

            String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10


            stkcksum.set_id(_id);
            stkcksum.setAuto_id(_auto_id);
            stkcksum.setSumdate(sumdate);
            stkcksum.setStoreid(storeid);

            stkcksum.setGoodsno(goodsno);
            stkcksum.setGoodsname(goodsname);
            stkcksum.setQmyereal(qmyereal);
            stkcksum.setQmyeCount(qmyeCount);

            stkcksum.setIsMove(isMove);




        }
        Log.i("Stkcksum数据库", "查找一条数据正确");
        cursor.close();
        return stkcksum;
    }

    public  List<String> getClassName(){
        List<String> list=new ArrayList<>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select classname from newstkcksum  ", null);
        while (cursor.moveToNext()) {
            String classname=cursor.getString(cursor.getColumnIndex("classname"));
            list.add(classname);
        }
        return list;
    }

    // 查找数据库两面全部数据
    public List<Stkcksum> getAllStkcksum() {
        List<Stkcksum> data = new ArrayList<Stkcksum>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from newstkcksum  order by  auto_id asc", null);
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

            String sumdate = cursor.getString(cursor.getColumnIndex("SumDate")); // a1

            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
            String classname = cursor.getString(cursor.getColumnIndex("classname")); // 4
            String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8

            String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10

            Stkcksum stkcksum = new Stkcksum();
            stkcksum.set_id(_id);
            stkcksum.setAuto_id(_auto_id);
            stkcksum.setSumdate(sumdate);
            stkcksum.setStoreid(storeid);

            stkcksum.setGoodsno(goodsno);
            stkcksum.setGoodsname(goodsname);
            stkcksum.setQmyereal(qmyereal);
            stkcksum.setQmyeCount(qmyeCount);

            stkcksum.setIsMove(isMove);

            data.add(stkcksum);
        }
        Log.i("Stkcksum数据库", "查找所有数据正确");
        cursor.close();
        return data;
    }


    // 查找数据库两面全部数据
    public List<Stkcksum> getAllUpStkcksum() {
        List<Stkcksum> data = new ArrayList<Stkcksum>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from newstkcksum where qmyecount <> ''  ", null);
        while (cursor.moveToNext()) {
            String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

            String sumdate = cursor.getString(cursor.getColumnIndex("SumDate")); // a1

            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
            String classname = cursor.getString(cursor.getColumnIndex("classname")); // 4
            String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8

            String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10

            Stkcksum stkcksum = new Stkcksum();

            stkcksum.setAuto_id(_auto_id);
            stkcksum.setSumdate(sumdate);
            stkcksum.setStoreid(storeid);

            stkcksum.setGoodsno(goodsno);
            stkcksum.setGoodsname(goodsname);
            stkcksum.setQmyereal(qmyereal);
            stkcksum.setQmyeCount(qmyeCount);

            stkcksum.setIsMove(isMove);

            data.add(stkcksum);
        }
        Log.i("Stkcksum数据库", "查找所有数据正确");
        cursor.close();
        return data;
    }


    public List<Stkcksum>   getStkcksum(int  index,String name){
        List<Stkcksum> data = new ArrayList<Stkcksum>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();

        Cursor cursor=null;
//        cursor = db.rawQuery("select * from newstkcksum  ", null);
       if(index==0){

            cursor = db.rawQuery("select * from newstkcksum  where"+name, null);


        }else if (index==1){
            cursor = db.rawQuery("select * from newstkcksum  where qmyereal<>qmyecount and "+name, null);
        }else if (index==2){
            cursor = db.rawQuery("select * from newstkcksum  where isMove=1 and "+name, null);

        }else if (index==3){
            cursor = db.rawQuery("select * from newstkcksum  where isMove=1 and qmyeCount<> '' and "+name, null);

        }

        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
            String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

            String sumdate = cursor.getString(cursor.getColumnIndex("SumDate")); // a1

            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
            String classname = cursor.getString(cursor.getColumnIndex("classname")); // 4
            String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
            String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
            String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8

            String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10

            Stkcksum stkcksum = new Stkcksum();
            stkcksum.set_id(_id);
            stkcksum.setAuto_id(_auto_id);
            stkcksum.setSumdate(sumdate);
            stkcksum.setStoreid(storeid);

            stkcksum.setGoodsno(goodsno);
            stkcksum.setGoodsname(goodsname);
            stkcksum.setQmyereal(qmyereal);
            stkcksum.setQmyeCount(qmyeCount);

            stkcksum.setIsMove(isMove);

            data.add(stkcksum);
        }
        Log.i("Stkcksum数据库", "查找所有数据正确");
        cursor.close();
        return data;



    }

}