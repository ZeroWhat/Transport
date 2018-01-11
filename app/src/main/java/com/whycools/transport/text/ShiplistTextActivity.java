package com.whycools.transport.text;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.GoodsList;

/**
 * Created by Zero on 2017-03-03.
 */

public class ShiplistTextActivity extends BaseActivity {
    private TextView shiplist_text_tv;
    private DBinventory dDBinventory;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_shiplist_text);
    }

    @Override
    public void initViews() {
        shiplist_text_tv= findViewById(R.id.shiplist_text_tv);
        dDBinventory= new DBinventory(this);

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        StringBuffer buffer=new StringBuffer();
        buffer.append("下载清单的总个数为"+getShiplist()+"\r\n");
        buffer.append("下载清单billtype=入为"+getShiplistIn()+"\r\n");
        buffer.append("下载清单billtype=出为"+getShiplistOut()+"\r\n");
        buffer.append("标准表中所有个数"+getnewbarcode_headerint()+"\r\n");

        buffer.append("-------------------\r\n");


        buffer.append("carId>0个数为"+getBycar()+"\r\n");
        buffer.append("carId>0并且billtype=入的个数为"+getBycarIn()+"\r\n");
        buffer.append("今天carId>0并且billtype=入的个数为"+getBycarInDate()+"\r\n");
        buffer.append("carId>0并且billtype=出的个数为"+getBycarOut()+"\r\n");
        buffer.append("今天carId>0并且billtype=出的个数为"+getBycarOutDate()+"\r\n");

        buffer.append("-------------------\r\n");

        buffer.append("carId=0个数为"+getByShiplist()+"\r\n");
        buffer.append("carId=0并且billtype=入的个数为"+getByShiplistIn()+"\r\n");
        buffer.append("carId=0并且billtype=出的个数为"+getByShiplistOut()+"\r\n");

        buffer.append("-------------------\r\n");



        buffer.append("依维柯 沪F B6991[1次]>>>>"+getDate()+"\r\n");
        buffer.append("海尔洗衣机EMB75F5GU1-------->>>>>>>>>>>>>>>>>>"+get()+"\r\n");

        buffer.append(GoodsList.getshiplistBycar("依维柯 沪F B6991[1次]","2017-03-10","214",ShiplistTextActivity.this)+"\r\n");


        buffer.append("---------"+getBarcodeHeader()+"\r\n");
        buffer.append("-----------"+getNewBarcodeHeader()+"\r\n");
        shiplist_text_tv.setText(buffer.toString());

       // upDataShipList();


    }
    private int  getShiplist(){

        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist ", null);
        return  cursor.getCount();
    }

    private int  getShiplistIn(){

        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where billtype=?", new String[]{"入"});
        return  cursor.getCount();
    }
    private int  getShiplistOut(){

        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where billtype=?", new String[]{"出"});
        return  cursor.getCount();
    }
    private int getnewbarcode_headerint(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from newbarcode_header ", null);
        return  cursor.getCount();

    }
    private int getBycar(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId>0", null);
        return  cursor.getCount();
    }
    private int getBycarIn(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId>0  and billtype=?", new String[]{"入"});
        return  cursor.getCount();
    }

    private int getBycarInDate(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"入", DateUtile.getdata(0)});
        return  cursor.getCount();
    }

    private int getBycarOut(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId>0  and billtype=?", new String[]{"出"});
        return  cursor.getCount();
    }

    private int getBycarOutDate(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"出",DateUtile.getdata(0)});
        return  cursor.getCount();
    }

    private int getByShiplist(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId=0", null);
        return  cursor.getCount();
    }

    private int getByShiplistIn(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId=0 and billtype=?", new String[]{"入"});
        return  cursor.getCount();
    }

    private int getByShiplistOut(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId=0 and billtype=?", new String[]{"出"});
        return  cursor.getCount();
    }//依维柯 沪F B6991[1次]
    private String  getDate(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where goodsname=?", new String[]{"海尔空调KFR-26GW/06NCA13 1P分体挂机"});//98766452  carId=0   海尔洗衣机B7001Z71V 7公斤
        StringBuffer buffer =new StringBuffer();
        Log.i(TAG, "-----------------------------------------------------: ");
        // 98766372 海尔冰柜BD-226W 冷冻柜
        while (cursor.moveToNext()) {                                                                   //98766478   海尔洗衣机XQBM30-R01W 3公斤

            buffer.append(">>>>>>>>>>>>>>>\r\n");
            String storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            String carId = cursor.getString(cursor.getColumnIndex("carId"));
            Log.i(TAG, "carId: "+carId);


            String carname = cursor.getString(cursor.getColumnIndex("carname"));
            Log.i(TAG, "carname: "+carname);

            String billno = cursor.getString(cursor.getColumnIndex("billno"));
            Log.i(TAG, "billno: "+billno);


            String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            Log.i(TAG, "goodsname: "+goodsname);
            String shipdate = cursor.getString(cursor.getColumnIndex("shipdate"));
            String innerno = cursor.getString(cursor.getColumnIndex("innerno"));
            String outerno = cursor.getString(cursor.getColumnIndex("outerno"));
            String goodsid = cursor.getString(cursor.getColumnIndex("goodsId"));
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag"));

            Log.i(TAG, "goodsid: "+goodsid);
            buffer.append(storeid+billtype+goodsname+shipdate+goodsid+innerno+outerno+inoutFlag+"----"+carId+"\r\n");

        }
        return  buffer.toString();
    }


    private String  get(){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from newbarcode_header where goodsname=?", new String[]{"海尔空调KFR-26GW/06NHB13 1P定频分体挂机"});
        StringBuffer buffer =new StringBuffer();
        Log.i(TAG, "get: "+cursor.getCount());
        while (cursor.moveToNext()) {

            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            Log.i(TAG, "barcodeLeftpos: "+barcodeLeftpos);
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            Log.i(TAG, "minLen: "+minLen);
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            String barcode_header = cursor.getString(cursor.getColumnIndex("barcode_header"));
            Log.i(TAG, "barcode_header: "+barcode_header);

            Log.i(TAG, "goodsname: "+goodsname);
//            String shipdate = cursor.getString(cursor.getColumnIndex("shipdate"));
            String goodsid = cursor.getString(cursor.getColumnIndex("goodsId"));
//            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            buffer.append(goodsname+goodsid+"-----"+barcode_header+"------------"+minLen+"\r\n");

        }
        return  buffer.toString();
    }


    private String  getBarcodeHeader(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from barcode_header ",null);
      return "标准表的个数"+cursor.getCount();
    }
    private String getNewBarcodeHeader(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from newbarcode_header where goodsId=35028",null);
        StringBuffer str=new StringBuffer();
        while (cursor.moveToNext()) {
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            String barcode_header = cursor.getString(cursor.getColumnIndex("barcode_header"));
            str.append(goodsname+"----"+barcode_header);
        }
        return "新标准表的个数"+cursor.getCount()+"---"+str;
    }

    public void upDataShipList(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        db.execSQL("update shiplist set billtype=? ",new Object[]{"入"});
        Log.i(TAG, "upDataShipList: 数据更改成功");

    }


}
