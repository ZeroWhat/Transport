package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.ShipList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zero on 2016-12-06.
 */

public class ShipListService {
    DBinventory dDBinventory;
    Context context;

    public ShipListService(Context context) {
        if(dDBinventory==null){
            this.dDBinventory = new DBinventory(context);
        }
        this.context = context;
    }

    // shiplist数据库添加数据province, city, district
    public void addShipListData(ShipList shiplist) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL(
                "insert into shiplist(" + "storeid,"+"carId," + "inoutFlag," + "carname,"
                        + "shipdate," + "seqno," + "billtype," + "billno,"
                        + "company_address," + "goodsId," + "goodsname,"
                        + "in_qty," + "cin_qty," + "out_qty," + "cout_qty,"
                        + "innerno," + "outerno,"+"address,"+"Phonenumber,"+"isfinish,"+"distance,"+"isStart,stateId,province,city,district,"
                        + "cm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[] { shiplist.getStoreid(),shiplist.getCarId(), shiplist.getInoutFlag(),
                        shiplist.getCarname(), shiplist.getShipdate(),
                        shiplist.getSeqno(), shiplist.getBilltype(),
                        shiplist.getBillno(), shiplist.getCompany_address(),
                        shiplist.getGoodsId(), shiplist.getGoodsname(),
                        shiplist.getIn_qty(), shiplist.getCin_qty(),
                        shiplist.getOut_qty(), shiplist.getCout_qty(),
                        shiplist.getInnerno(), shiplist.getOuterno(),shiplist.getAddress(),shiplist.getPhonenumber(),shiplist.getIsfinish(),shiplist.getDistance().toString(),shiplist.getIsStart(),shiplist.getStateId(),shiplist.getProvince(),shiplist.getCity(),shiplist.getDistrict(),
                        shiplist.getCm() });
        Log.i("shiplist.getCm()", "addShipListData: "+shiplist.getCm()+"添加成功");
    }

    // 清除shiplist数据表所有内容
    public void clearShipListData() {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("DELETE FROM shiplist");
    }

    // 修改内机已出个数
    public void upshiplistinqtydata(String cin_qty,String _id){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Log.i("shiplist数据库", "内机数量已修改"+_id);
        db.execSQL("update shiplist set cin_qty=? where _id=?",new Object[]{cin_qty,_id});
    }

    // 修改外机已出个数
    public void upshiplistoutqtydata(String cout_qty,String _id){
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Log.i("shiplist数据库", "distance已修改cout_qty:"+cout_qty);

        db.execSQL("update shiplist set cout_qty=? where _id=?",new Object[]{cout_qty,_id});
    }

    // 查询内机已经发出的个数数据
    public String  findshiplistCinqty(String _id) {

        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where _id=?",
                new String[] { _id });
        if (cursor.moveToFirst()) {
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            cursor.close();
            return cin_qty;
        }
        Log.i("Stkcksum数据库", "查找一条数据正确");
        cursor.close();
        return "0";
    }
    public int findorderno(String orderno){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where billno=?",new String[] { orderno });
        int data=cursor.getCount();
        cursor.close();
        return data;
    }
    // 删出某一条数据
    public void deleteorderno(String orderno) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        db.execSQL("delete from shiplist where billno=?", new String[] { orderno });
        Log.i("shiplist数据库", "成功删除");
    }



    // 查询外机已经发出的个数数据
    public String  findshiplistCoutqty(String _id) {

        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where _id=?",
                new String[] { _id });
        if (cursor.moveToFirst()) {
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty"));// 13
            cursor.close();
            return cout_qty;
        }
        Log.i("Stkcksum数据库", "查找一条数据正确");
        cursor.close();
        return "0";
    }



    // 查询
    public ShipList findbilltype(String _id) {
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery( "select a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist a left join barcode_header b on a.goodsId=b.goodsId   where a._id=?", new String[]{_id});
        ShipList shiplist=new ShipList();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 4
            String billtype = cursor.getString(cursor .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17


            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            shiplist.set_id(id);
            shiplist.setStoreid(storeid);
            shiplist.setCarId(carId);
            shiplist.setInoutFlag(inoutFlag);
            shiplist.setCarname(carname);
            shiplist.setBilltype(billtype);
            shiplist.setBillno(billno);
            shiplist.setGoodsId(goodsId);
            shiplist.setGoodsname(goodsname);
            shiplist.setIn_qty(in_qty);
            shiplist.setCin_qty(cin_qty);
            shiplist.setOut_qty(out_qty);
            shiplist.setCout_qty(cout_qty);
            shiplist.setInnerno(innerno);
            shiplist.setOuterno(outerno);
            shiplist.setBarcode_header_in(barcode_header_in);
            shiplist.setBarcode_header_out(barcode_header_out);
            shiplist.setBarcodeLeftpos(barcodeLeftpos);
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setMinLen(minLen);

        }
        cursor.close();
        return shiplist;

    }
    public List<String> allStoreid(String  carname,String billtype ,String shipdate){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor;
        if (billtype.equals("出")){
             cursor = db.rawQuery("select * from shiplist where carname=? and billtype=? and inoutFlag<0 and shipdate=?", new String []{carname,"出",shipdate});
        }else{
             cursor = db.rawQuery("select * from shiplist where carname=? and billtype=? and inoutFlag>0 and shipdate=?", new String []{carname,"出",shipdate});
        }
//        Cursor cursor = db.rawQuery("select * from shiplist where carname=? and billtype=?", new String []{carname,"出"});
        List<String> list=new ArrayList<>();
        while (cursor.moveToNext()) {
            String storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            list.add(storeid);
        }
        cursor.close();
        return list;
    }

    // 查找全部数据
    public List<ShipList> getAllShipList(Context context) {
        List<ShipList> data = new ArrayList<ShipList>();
        dDBinventory = new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where billtype=?", new String []{"出"});
        while (cursor.moveToNext()) {
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            Log.i("storeid", "getAllShipList: "+storeid);
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 2
            Log.i("storeid", "getAllShipList: "+storeid);
            String shipdate = cursor.getString(cursor.getColumnIndex("shipdate")); // 2
            Log.i("所有数据", "shipdate:>>>>>>>>>>>>>>>>>>>> "+shipdate);
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            Log.i("所有数据","carId: "+carId);
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            Log.i("inoutFlag", "getAllShipList: "+inoutFlag);
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            Log.i("所有数据","carname: "+carname);
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            Log.i("billtype", "getAllShipList: "+billtype);
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            Log.i("cm", "getAllShipList: "+cm);
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }

    // 查询shiplistcarid大于0
    public List<ShipList> getshiplistcarid(String shipdate,String billtp) {
        List<ShipList> data = new ArrayList<ShipList>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist ", null);
        Log.i("数据总个数", "cursor1: "+cursor.getCount());
        if (billtp.equals("出")){
             cursor = db.rawQuery("select * from shiplist where carId>0 and billtype=? and shipdate=? and inoutFlag<0", new String[]{"出",shipdate});
        }else {
            cursor = db.rawQuery("select * from shiplist where carId>0 and billtype=? and shipdate=? and inoutFlag>0", new String[]{"出",shipdate});
        }

//        Cursor cursor = db.rawQuery("select * from shiplist where  billtype=?and shipdate=?", new String[]{"出",shipdate});
        Log.i("查询carid>0", "cursor个数为: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); //
            Log.i("查询carid>0", "_id"+_id);
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            Log.i("车id", "carId: "+carId);
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            Log.i("carname", "carname: "+carname);
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            //province, city, district
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String district = cursor.getString(cursor.getColumnIndex("district"));

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            Log.i("查询carid>0", "billno: "+billno);
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setProvince(province);
            shiplist.setCity(city);
            shiplist.setDistrict(district);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }
    //
    public List<ShipList> getAllExpressScan(String ccm) {
        List<ShipList> data = new ArrayList<ShipList>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where cm=?", new String []{ccm});
        Log.i("查找的快递单号个数为", "getAllExpressScan: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            Log.i("storeid", "getAllShipList: "+storeid);
            String _id = cursor.getString(cursor.getColumnIndex("_id")); // 2
            Log.i("storeid", "getAllShipList: "+storeid);
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            Log.i("所有数据","carId: "+carId);
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            Log.i("inoutFlag", "getAllShipList: "+inoutFlag);
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            Log.i("所有数据","carname: "+carname);
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            Log.i("billtype", "getAllShipList: "+billtype);
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }

    // 查询shiplistcarid等于0
    public List<ShipList> getshiplistbillno() {
        List<ShipList> data = new ArrayList<ShipList>();
        dDBinventory = new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where carId=0 and billtype=? and isfinish=?", new String[]{"出","未完成"});
        Log.i("查询carid>0", "cursor个数为: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); //
            Log.i("查询carid>0", "_id"+_id);
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            Log.i("查询carid>0", "billno: "+billno);
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }
    // 修改是否完成
    public void upshiplistisfinishdate(String isfinish,String _id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Log.i("shiplist数据库", "isfinish已修改为"+isfinish +"_id"+_id);
        db.execSQL("update shiplist set isfinish=? where _id=?",new Object[]{isfinish,_id});
    }

    // 修改是否发车
    public void upshiplistIsStartdate(String isStart,String _id) {
        SQLiteDatabase db = dDBinventory.getWritableDatabase();
        Log.i("shiplist数据库", "isfinish已修改为"+isStart +"_id"+_id);
        db.execSQL("update shiplist set isStart=? where _id=?",new Object[]{isStart,_id});
    }


    // 查询shiplist完成的订单
    public List<ShipList> getshiplistfinish() {
        List<ShipList> data = new ArrayList<ShipList>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where  isfinish=?", new String[]{"已完成"});
        Log.i("查询carid>0", "cursor个数为: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); //
            Log.i("查询carid>0", "_id"+_id);
            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            Log.i("查询carid>0", "billno: "+billno);
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }


    // 查询
    public List<ShipList> findBystoresid(String storeid) {
        List<ShipList> data = new ArrayList<ShipList>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from shiplist where  storeid=?", new String[]{storeid});
        Log.i("查询", "cursor个数为: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex("_id")); //

            String _storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            String shipdate = cursor.getString(cursor.getColumnIndex("shipdate")); //
            Log.i("查询carid>0", "_id"+_id);
//            String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 2
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor
                    .getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
            String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
            String billtype = cursor.getString(cursor
                    .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String company_address = cursor.getString(cursor
                    .getColumnIndex("company_address")); // 9
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor
                    .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

            String cout_qty = cursor.getString(cursor
                    .getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

            String address = cursor.getString(cursor.getColumnIndex("address")); // 17
            String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
            String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
            String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
            String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

            String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
            ShipList shiplist = new ShipList();
            shiplist.set_id(_id);
            shiplist.setStoreid(_storeid);
            shiplist.setShipdate(shipdate);
            shiplist.setStoreid(storeid);// 2
            shiplist.setCarId(carId);// 3
            shiplist.setInoutFlag(inoutFlag);// 4
            shiplist.setCarname(carname);// 5
            shiplist.setSeqno(seqno);// 6
            shiplist.setBilltype(billtype);// 7
            shiplist.setBillno(billno);// 8
            shiplist.setCompany_address(company_address);// 9
            shiplist.setGoodsId(goodsId);// 10
            shiplist.setGoodsname(goodsname);// 11
            shiplist.setIn_qty(in_qty);// 12
            shiplist.setCin_qty(cin_qty);// 13
            shiplist.setOut_qty(out_qty);// 14
            shiplist.setCout_qty(cout_qty);// 15
            shiplist.setInnerno(innerno);// 16
            shiplist.setOuterno(outerno);// 17
            shiplist.setAddress(address);
            shiplist.setPhonenumber(Phonenumber);
            shiplist.setIsfinish(isfinish);
            shiplist.setDistance(distance);
            shiplist.setIsStart(isStart);
            shiplist.setCm(cm);// 18
            data.add(shiplist);
        }
        cursor.close();
        return data;
    }
    public List<ShipList> findByCarData(String carname,String date,String storeid,String billtp){

        List<ShipList> data = new ArrayList<ShipList>();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist a left join barcode_header b on a.goodsId=b.goodsId where carId>0  and billtype=? and carname=?and shipdate=? and  storeid=? and inoutFlag<0", new String[]{billtp, carname,date,storeid});

        Log.i("-------", "------: "+carname+"-----"+date+"--------"+storeid+"-----"+billtp);

        if (billtp.equals("出")) {
             cursor =db.rawQuery("select a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist a left join barcode_header b on a.goodsId=b.goodsId where carId>0  and billtype=? and carname=?and shipdate=? and  storeid=? and inoutFlag<0", new String[]{"出", carname,date,storeid});
        }else {
            cursor =db.rawQuery("select  a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist  a left join barcode_header b on a.goodsId=b.goodsId where carId>0  and billtype=? and carname=?and shipdate=? and  storeid=? and inoutFlag>0", new String[]{"出", carname,date,storeid});

        }

        Log.i("查询", "cursor个数为: "+cursor.getCount());
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));

            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String _storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3

            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag")); // 4
            String billtype = cursor.getString(cursor .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));


            ShipList shiplist = new ShipList();
            shiplist.set_id(id);
            shiplist.setStoreid(_storeid);
            shiplist.setCarId(carId);
            shiplist.setInoutFlag(inoutFlag);
            shiplist.setBilltype(billtype);
            shiplist.setBillno(billno);
            shiplist.setGoodsId(goodsId);
            shiplist.setGoodsname(goodsname);
            shiplist.setIn_qty(in_qty);
            shiplist.setCin_qty(cin_qty);
            shiplist.setOut_qty(out_qty);
            shiplist.setCout_qty(cout_qty);
            shiplist.setInnerno(innerno);
            shiplist.setOuterno(outerno);
            shiplist.setBarcode_header_in(barcode_header_in);
            shiplist.setBarcode_header_out(barcode_header_out);
            shiplist.setBarcodeLeftpos(barcodeLeftpos);
            shiplist.setMinLen(minLen);
            data.add(shiplist);

        }
        cursor.close();
        return data;

    }
    public ShipList findid(String _id){
        ShipList shiplist = new ShipList();
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist a left join barcode_header b on a.goodsId=b.goodsId where a._id=?", new String[]{_id});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String _storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 4
            String billtype = cursor.getString(cursor .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));



            shiplist.set_id(id);
            shiplist.setStoreid(_storeid);
            shiplist.setCarId(carId);
            shiplist.setInoutFlag(inoutFlag);
            shiplist.setCarname(carname);
            shiplist.setBilltype(billtype);
            shiplist.setBillno(billno);
            shiplist.setGoodsId(goodsId);
            shiplist.setGoodsname(goodsname);
            shiplist.setIn_qty(in_qty);
            shiplist.setCin_qty(cin_qty);
            shiplist.setOut_qty(out_qty);
            shiplist.setCout_qty(cout_qty);
            shiplist.setInnerno(innerno);
            shiplist.setOuterno(outerno);
            shiplist.setBarcode_header_in(barcode_header_in);
            shiplist.setBarcode_header_out(barcode_header_out);
            shiplist.setBarcodeLeftpos(barcodeLeftpos);
            shiplist.setMinLen(minLen);

        }
        cursor.close();
        return shiplist;

    }
    //获取按车发货个数
    public int getByCar(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from shiplist where carId>0  and billtype=? and inoutFlag<0", new String[]{"出"});
        int data=cursor.getCount();
        cursor.close();
        return data;
    }
    //获取按车发货个数
    public int getByCarIn(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from shiplist where carId>0  and billtype=? and inoutFlag>0", new String[]{"出"});
        int data=cursor.getCount();
        cursor.close();
        return data;
    }
    //获取按单发货个数
    public int getByList(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from shiplist where carId=0  and billtype=? ", new String[]{"出"});
        int data=cursor.getCount();
        cursor.close();
        return data;
    }
    //获取采购单个数
    public int getByAchat(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from shiplist where  carId=0 and inoutFlag=? ", new String[]{"1"});
        int data=cursor.getCount();
        cursor.close();
        return data;
    }
    //获取按调度单个数
    public int getByDispatch(){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from shiplist where  billtype=? and inoutFlag=?", new String[]{"调","-1"});
        int data=cursor.getCount();
        cursor.close();
        return data;
    }


    public  ShipList  match(String _id){
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Cursor cursor =db.rawQuery( "select a.*,b.barcode_header_in,b.barcode_header_out,b.barcodeLeftpos,b.minLen from shiplist a left join barcode_header b on a.goodsId=b.goodsId   where a._id=?", new String[]{_id});
        ShipList shiplist=new ShipList();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String _storeid = cursor.getString(cursor.getColumnIndex("storeid"));
            String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag")); // 4
            String carname = cursor.getString(cursor.getColumnIndex("carname")); // 4
            String billtype = cursor.getString(cursor .getColumnIndex("billtype")); // 7
            String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
            String goodsname = cursor.getString(cursor .getColumnIndex("goodsname")); // 11
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty")); // 15
            String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
            String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            shiplist.set_id(id);
            shiplist.setStoreid(_storeid);
            shiplist.setCarId(carId);
            shiplist.setInoutFlag(inoutFlag);
            shiplist.setCarname(carname);
            shiplist.setBilltype(billtype);
            shiplist.setBillno(billno);
            shiplist.setGoodsId(goodsId);
            shiplist.setGoodsname(goodsname);
            shiplist.setIn_qty(in_qty);
            shiplist.setCin_qty(cin_qty);
            shiplist.setOut_qty(out_qty);
            shiplist.setCout_qty(cout_qty);
            shiplist.setInnerno(innerno);
            shiplist.setOuterno(outerno);
            shiplist.setBarcode_header_in(barcode_header_in);
            shiplist.setBarcode_header_out(barcode_header_out);
            shiplist.setBarcodeLeftpos(barcodeLeftpos);
            shiplist.setMinLen(minLen);

        }
        cursor.close();
        return shiplist;


    }




}
