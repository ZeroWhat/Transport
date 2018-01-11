package com.whycools.transport.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.CodeHeaderMulti;


/**
 * Created by QL on 2016-08-31.
 */
public class CodeHeaderMultiService {
    DBinventory dDBinventory;
    Context context;

    public CodeHeaderMultiService(Context context) {
        if(dDBinventory==null){
            this.dDBinventory = new DBinventory(context);
        }
        this.context = context;
    }

    // 添加数据
    public void addCodeHeaderMulti(String goodsId,String  is_in,String bar69,String barcode,String barcodeLeftpos) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL(
                "insert into code_header_multi(" +
                        "goodsId," + // 1
                        "is_in," + // a1
                        "bar69," + // 3
                        "barcode," + // 4
                        "barcodeLeftpos" + // 5
                        ") values(?,?,?,?,?)",
                new Object[] {
                        goodsId,
                        is_in,
                        bar69,
                        barcode,
                        barcodeLeftpos
                });

    }

    // 清除数据表所有内容
    public void clearCodeHeaderMulti() {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("DELETE FROM code_header_multi");
        // Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
        Log.i("code_header_multi", "清除了");
    }
    public CodeHeaderMulti findbarcode(String barcode){
        CodeHeaderMulti codeHeaderMulti=new CodeHeaderMulti();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from code_header_multi where barcode=?",
                new String[] { barcode });
        if (cursor.moveToFirst()) {
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 1
            String is_in = cursor.getString(cursor.getColumnIndex("is_in")); // 2
            String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 3
            String _barcode = cursor.getString(cursor.getColumnIndex("barcode")); // 4
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos")); // 5
            codeHeaderMulti.setGoodsId(goodsId);
            codeHeaderMulti.setIs_in(is_in);
            codeHeaderMulti.setBar69(bar69);
            codeHeaderMulti.setBarcode(_barcode);
            codeHeaderMulti.setBarcodeLeftpos(barcodeLeftpos);
        }
        cursor.close();
        return codeHeaderMulti;
    }
}
