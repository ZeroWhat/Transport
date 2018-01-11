package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.BarcodeHeader;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.Stores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-08-11.
 */

/*
// 新条码标准表⑤
        db.execSQL("create table newbarcode_header ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "goodsId int not null,"
                + "goodsname nvarchar(60) not null,"
                + "bar69in nvarchar(20),"
                + "bar69out nvarchar(20),"
                + "barcode_header nvarchar(30),"
                + "isIn int,"
                + "barcodeLeftpos int, "
                + "minLen int,"
                + "revisetime datetime)");*/

public class NewbarcodeheaderService {
    DBinventory dDBinventory;
    Context context;

    public NewbarcodeheaderService(Context context) {
        if(dDBinventory==null){
            this.dDBinventory = new DBinventory(context);
        }
        this.context = context;
    }

    // 添加数据
    public void addNewbarcodeheader(Newbarcodeheader newbarcodeheader) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();

        Log.i("标准表数据添加中--------------------------------", "添加成功");
        db.execSQL(
                "insert into newbarcode_header(" +
                        "goodsId," + // 1
                        "goodsname," + // 2
                        "bar69in," + // 3
                        "bar69out," + // 4
                        "barcode_header," + // 4
                        "isIn," + // 4
                        "barcodeLeftpos," + // 4
                        "minLen," + // 4
                        "revisetime" + // 5
                        ") values(?,?,?,?,?,?,?,?,?)",
                new Object[] {
                        newbarcodeheader.getGoodsId(),
                        newbarcodeheader.getGoodsname(),
                        newbarcodeheader.getBar69in(),
                        newbarcodeheader.getBar69out(),
                        newbarcodeheader.getBarcode_header(),
                        newbarcodeheader.getIsIn(),
                        newbarcodeheader.getBarcodeLeftpos(),
                        newbarcodeheader.getMinLen(),
                        newbarcodeheader.getRevisetime()
                });
    }

    // 清除数据表所有内容
    public void clearNewbarcodeheader() {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM newbarcode_header");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
        Log.i("Stores数据表", "数据清除了");
    }
    // 删出某一条数据
    public void deleteStkcksum(String storeId) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("delete from stores where storeId=?", new String[] { storeId });
        Log.i("Stores数据库", "成功删除");
    }
    // 修改某一条数据
    public void updateStkcksum(Stores stores) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("update stores set storename=? where storeId=?",
                new Object[] { stores.getStorename() });
        Log.i("Stores数据库", "修改数据成功");

    }


    // 查询某一条数据
    public Stores findNewbarcodeheader(String storeId) {
        Stores stores = new Stores();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from stores where storeId=?",
                new String[] { storeId });
        if (cursor.moveToFirst()) {
            String _storeId = cursor.getString(cursor.getColumnIndex("storeId")); // 1

            String storename = cursor.getString(cursor.getColumnIndex("storename")); // 2

            String address = cursor.getString(cursor.getColumnIndex("address")); // 3
            String phoneno = cursor.getString(cursor.getColumnIndex("phoneno")); // 4
            String manager = cursor.getString(cursor.getColumnIndex("manager")); // 5

            stores.setStoreId(_storeId);
            stores.setStorename(storename);
            stores.setAddress(address);
            stores.setPhoneno(phoneno);
            stores.setManager(manager);
        }
        Log.i("Stores数据库", "查找一条数据正确");
        cursor.close();
        return stores;
    }

    public String lastTime(){
        String s = "";
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(revisetime) as revisetime from newbarcode_header",null);
        while (cursor.moveToNext())
        {
            if (cursor.getString(cursor.getColumnIndex("revisetime"))!=null)
            {
                s=cursor.getString(cursor.getColumnIndex("revisetime"));
            }
        }
        db.close();
        cursor.close();
        return s;


    }

    // 查找数据库两面全部数据
    public List<Newbarcodeheader> getAllNewbarcodeheader(String barcode) {
        List<Newbarcodeheader> data = new ArrayList<Newbarcodeheader>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from newbarcode_header  order by revisetime desc", null);
        Log.i("数据库储存的个数为", "--------------: "+cursor.getCount());
        while (cursor.moveToNext()) {


            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 1
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 2
            String bar69in = cursor.getString(cursor.getColumnIndex("bar69in")); // 3
            String bar69out = cursor.getString(cursor.getColumnIndex("bar69out")); // 4
            String barcode_header = cursor.getString(cursor.getColumnIndex("barcode_header")); // 5
            String isIn = cursor.getString(cursor.getColumnIndex("isIn")); // 5
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos")); // 5
            String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 5
            String revisetime = cursor.getString(cursor.getColumnIndex("revisetime")); // 5
//            if (barcodeLeftpos.equals("2")&&goodsname.equals("三菱电机空调MSZ-ZFJ12VA(R410) 1.5P变频分体挂机")){
//                Log.i("--------------", "--------------------: ");
//                Log.i("数据库", "barcodeLeftpos: "+barcodeLeftpos);
//                Log.i("数据库", "minLen: "+minLen);
//                Log.i("数据库", "barcode_header: "+barcode_header);
//                Log.i("数据库", "isIn: "+isIn);
//                Log.i("数据库", "goodsname: "+goodsname);
//                Log.i("------", "----: "+barcode.substring(Integer.valueOf(barcodeLeftpos)-1,Integer.valueOf(barcodeLeftpos)+barcode_header.length()));
//                Log.i("------", "----: "+barcode.substring(Integer.valueOf(barcodeLeftpos)-1,Integer.valueOf(barcodeLeftpos)-1+barcode_header.length()).equals(barcode_header));
//                Log.i("--------------", "--------------------: ");
//            }

            if (barcodeLeftpos==null||barcodeLeftpos.equals("null")){
                barcodeLeftpos="1";
            }
//            Log.i("-----------------", "barcode.length(): "+barcode.length());
//            Log.i("-----------------", "barcode: "+barcode);
//            Log.i("-----------------", "barcode_header.length(): "+barcode_header.length());
//            Log.i("-----------------", "barcode_header: "+barcode_header);
//            Log.i("-----------------", "Integer.valueOf(barcodeLeftpos)-1: "+(Integer.valueOf(barcodeLeftpos)-1));
//            Log.i("-----------------", "Integer.valueOf(barcodeLeftpos)-1: "+(Integer.valueOf(barcodeLeftpos)-1));
//            Log.i("-----------------", "Integer.valueOf(barcodeLeftpos)-1+barcode_header.length(): "+(Integer.valueOf(barcodeLeftpos)-1+barcode_header.length()));
            if(barcode.length()>=barcode_header.length()&&(Integer.valueOf(barcodeLeftpos)-1+barcode_header.length())<=barcode.length()&&barcode.substring(Integer.valueOf(barcodeLeftpos)-1,Integer.valueOf(barcodeLeftpos)-1+barcode_header.length()).equals(barcode_header)){
                Newbarcodeheader newbarcodeheader = new Newbarcodeheader();

                newbarcodeheader.setGoodsId(goodsId);
                newbarcodeheader.setGoodsname(goodsname);
                newbarcodeheader.setBar69in(bar69in);
                newbarcodeheader.setBar69out(bar69out);
                newbarcodeheader.setBarcode_header(barcode_header);
                newbarcodeheader.setIsIn(isIn);
                newbarcodeheader.setBarcodeLeftpos(barcodeLeftpos);
                newbarcodeheader.setMinLen(minLen);
                newbarcodeheader.setRevisetime(revisetime);
                data.add(newbarcodeheader);

            }


        }
        Log.i("Stores数据库", "查找所有数据正确");
        cursor.close();
        return data;
    }


    public List<Newbarcodeheader> findsame(){
        List<Newbarcodeheader> data = new ArrayList<Newbarcodeheader>();
        data.clear();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("select * from newbarcode_header ", null);
        Log.i("新标准表中所有的个数", "个数-----: "+cursor1.getCount());
        Cursor cursor2 = db.rawQuery("select * from newstkcksum  ", null);
        Log.i("新盘点表的个数", "个数----: "+cursor2.getCount());
        Cursor cursor = db.rawQuery("select a.barcode_header,a.isIn,a.barcodeLeftpos,a.minLen,a.goodsId,b._id,b.auto_id from newbarcode_header a inner   join newstkcksum  b on a.goodsId=b.auto_id", null);
        Log.i("内连接的个数", "cursor: "+cursor.getCount());
        while (cursor.moveToNext()) {

            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 6
//            Log.i("标准表中的goodsid", "goodsId: "+goodsId);
            String auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 6
//            Log.i("盘点表中auto_id", "auto_id: "+auto_id);
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 6
//            Log.i("盘点表中的_id", "_id---------: "+_id);
            String barcode_header = cursor.getString(cursor.getColumnIndex("barcode_header")); // 6
//            Log.i("标准表中", "barcode_header: "+barcode_header);
            String isIn = cursor.getString(cursor.getColumnIndex("isIn")); // 7
//            Log.i("标准表中", "isIn: "+isIn);
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos")); // 8
//            Log.i("标准表中", "barcodeLeftpos: "+barcodeLeftpos);
            String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
//            Log.i("标准表中", "minLen: "+minLen);
//            Log.i("-------------", "--------------");

            if(barcode_header!=null&&barcode_header.length()>0&&!barcode_header.equals("null")){
                Newbarcodeheader newbarcodeheader = new Newbarcodeheader();
                newbarcodeheader.set_id(_id);
                newbarcodeheader.setBarcode_header(barcode_header);
              //  Log.i("------------", "---------: "+barcode_header);
                newbarcodeheader.setBarcodeLeftpos(barcodeLeftpos);
                newbarcodeheader.setMinLen(minLen);
                data.add(newbarcodeheader);
            }



        }
        cursor.close();
        return data;


    }

}
