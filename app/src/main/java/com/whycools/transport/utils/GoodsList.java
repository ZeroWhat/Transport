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
 * Created by QL on 2016-09-01.
 */
public class GoodsList {
    private static DBinventory dDBinventory;
    private static final String TAG="调出车辆的所有发货清单";

    public static List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();



    //调出车辆的所有发货清单中某一辆车辆车辆的信息。
    public static List<Goods> getGoodsListByCarId(String carnameinfo,String shipdate,String storeid, Context context){
        list.clear();
        List<Goods> data = new ArrayList<Goods>();
        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Log.i(TAG, "开始查询 ");
        Cursor cursor = db.rawQuery("select b.*, c.is_in, c.barcode as barcode2, a._id as shipid ,a.storeid as astoreid,a.inoutFlag,a.innerno,a.outerno,carId,carname,billno,billtype,a.goodsId,a.goodsname,a.in_qty,a.out_qty,"+
                "a.in_qty-a.cin_qty as inleft," + //待扫内机数
                " a.out_qty-a.cout_qty as outleft,"+ //待扫外机数
                " IN_QTY+OUT_QTY-CIN_QTY-COUT_QTY as qtyleft FROM shiplist a left join barcode_header b on a.goodsId=b.goodsId " +
                " left join code_header_multi c on b.goodsId=c.goodsId " +
                " where   a.carId>0 and a.carname=? and shipdate=?  and astoreid=? order by b.goodsname", new String []{carnameinfo,shipdate,storeid});
        Log.i(TAG, "cursor个数: "+cursor.getCount());
        while (cursor.moveToNext()){
            Map<String,Object> map= new HashMap<String,Object>();
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag"));
            map.put("inoutFlag",inoutFlag);
            Log.i(TAG, "inoutFlag:.>>>>>>>>>>>>>>>> "+cursor.getString(cursor.getColumnIndex("inoutFlag")));
            String _id = cursor.getString(cursor.getColumnIndex("shipid"));
            map.put("_id",_id);
            String carId = cursor.getString(cursor.getColumnIndex("carId"));
            map.put("carId",carId);
            Log.i(TAG, "carId: "+carId);
            String billno = cursor.getString(cursor.getColumnIndex("billno"));
            Log.i(TAG, "billno: "+billno);
            map.put("billno",billno);
            String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
            Log.i(TAG, "billtype: "+billtype);
            map.put("billtype",billtype);
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId"));
            Log.i(TAG, "goodsId: "+goodsId);
            map.put("goodsId",goodsId);
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            Log.i(TAG, "goodsname: "+goodsname);
            map.put("goodsname",goodsname);
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            Log.i(TAG, "barcode_header_in: "+barcode_header_in);

            String IN_QTY = cursor.getString(cursor.getColumnIndex("in_qty"));
            map.put("IN_QTY",IN_QTY);
            Log.i(TAG, "IN_QTY: "+IN_QTY);
            String OUT_QTY = cursor.getString(cursor.getColumnIndex("out_qty"));
            map.put("OUT_QTY",OUT_QTY);
            Log.i(TAG, "OUT_QTY: "+OUT_QTY);
            String inleft = cursor.getString(cursor.getColumnIndex("inleft"));
            map.put("inleft",inleft);//inleft outleft
            Log.i(TAG, "inleft: "+inleft);
            String outleft = cursor.getString(cursor.getColumnIndex("outleft"));
            map.put("outleft",outleft);
            Log.i(TAG, "outleft: "+outleft);
//            String outleft = cursor.getString(cursor.getColumnIndex("outleft"));
//            Log.i(TAG, "outleft: "+outleft);
//            map.put("outleft",outleft);
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            map.put("barcodeLeftpos",barcodeLeftpos);
            Log.i(TAG, "barcodeLeftpos: "+barcodeLeftpos);
            String innerno = cursor.getString(cursor.getColumnIndex("innerno"));
            map.put("innerno",innerno);
            Log.i(TAG, "innerno: "+innerno);
            String outerno = cursor.getString(cursor.getColumnIndex("outerno"));
            map.put("outerno",outerno);
            Log.i(TAG, "outerno: "+outerno);
//            String bar69in = cursor.getString(cursor.getColumnIndex("bar69in"));
//            map.put("bar69in",bar69in);
//            String bar69out = cursor.getString(cursor.getColumnIndex("bar69out"));
//            map.put("bar69out",bar69out);
//            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
//            map.put("minLen",minLen);
//            String barcode2 = cursor.getString(cursor.getColumnIndex("barcode2"));
//            map.put("barcode2",barcode2);
//            Log.i(TAG, "barcode2: "+barcode2);
//            String is_in = cursor.getString(cursor.getColumnIndex("is_in"));
//            map.put("is_in",is_in);
//            Log.i(TAG, "is_in: "+is_in);
            list.add(map);
        }
        Log.i(TAG, "list.size: "+list.size());
        data=getGoodsListMulti(list);//合并相同的goodid
        return data;
    }

    public static String  getshiplistBycar(String carname,String shipdate,String storeid, Context context){
        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * ,a._id,a.goodsId from shiplist a left join barcode_header b on a.goodsId=b.goodsId " +
//                " left join code_header_multi c on b.goodsId=c.goodsId " +
                " where   a.carId>0 and a.carname=? and a.shipdate=?  and a.storeid=? ", new String []{carname,shipdate,storeid});


        while (cursor.moveToNext()){



            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            Log.i(TAG, "_id: "+_id);
            String carId = cursor.getString(cursor.getColumnIndex("carId"));
            Log.i(TAG, "carId: "+carId);
            String billno = cursor.getString(cursor.getColumnIndex("billno"));
            Log.i(TAG, "billno: "+billno);
            String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
            Log.i(TAG, "billtype: "+billtype);
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId"));

            Log.i(TAG, "goodsId: "+goodsId);
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            Log.i(TAG, "goodsname: "+goodsname);
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            Log.i(TAG, "barcode_header_in: "+barcode_header_in);
            String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
            Log.i(TAG, "barcode_header_out: "+barcode_header_out);
            String in_qty = cursor.getString(cursor.getColumnIndex("in_qty"));
            Log.i(TAG, "in_qty: "+in_qty);
            String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));
            Log.i(TAG, "cin_qty: "+cin_qty);

            String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));
            Log.i(TAG, "out_qty: "+out_qty);
            String cout_qty = cursor.getString(cursor.getColumnIndex("cout_qty"));
            Log.i(TAG, "cout_qty: "+cout_qty);


            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            Log.i(TAG, "barcodeLeftpos: "+barcodeLeftpos);
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            Log.i(TAG, "minLen: "+minLen);

            String innerno = cursor.getString(cursor.getColumnIndex("innerno"));
            Log.i(TAG, "innerno: "+innerno);

            String outerno = cursor.getString(cursor.getColumnIndex("outerno"));
            Log.i(TAG, "outerno: "+outerno);

        }
        return "查查的结果"+cursor.getCount();

    }


    //调出车辆的所有发货清单中某一辆车辆车辆的信息。
    public static List<Goods> getGoodsListByDistrict(String District,String shipdate, Context context){
        list.clear();
        List<Goods> data = new ArrayList<Goods>();
        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Log.i(TAG, "开始查询 ");
        Cursor cursor = db.rawQuery("select b.*, c.is_in, c.barcode as barcode2, a._id as shipid ,a.shipdate,a.district,a.inoutFlag,a.innerno,a.outerno,carId,carname,billno,billtype,a.goodsId,a.goodsname,a.in_qty,a.out_qty,"+
                "a.in_qty-a.cin_qty as inleft," + //待扫内机数
                " a.out_qty-a.cout_qty as outleft,"+ //待扫外机数
                " IN_QTY+OUT_QTY-CIN_QTY-COUT_QTY as qtyleft FROM shiplist a inner join barcode_header b on a.goodsId=b.goodsId " +
                " left join code_header_multi c on b.goodsid=c.goodsid " +
                " where   a.carId>0 and a.district=? and a.shipdate=?  order by b.goodsname", new String []{District,shipdate});
        Log.i(TAG, "cursor个数: "+cursor.getCount());
        while (cursor.moveToNext()){
            Map<String,Object> map= new HashMap<String,Object>();
            String inoutFlag = cursor.getString(cursor.getColumnIndex("inoutFlag"));
            map.put("inoutFlag",inoutFlag);
            Log.i(TAG, "inoutFlag:.>>>>>>>>>>>>>>>> "+cursor.getString(cursor.getColumnIndex("inoutFlag")));
            String _id = cursor.getString(cursor.getColumnIndex("shipid"));
            map.put("_id",_id);
            String carId = cursor.getString(cursor.getColumnIndex("carId"));
            map.put("carId",carId);
            Log.i(TAG, "carId: "+carId);
            String billno = cursor.getString(cursor.getColumnIndex("billno"));
            Log.i(TAG, "billno: "+billno);
            map.put("billno",billno);
            String billtype = cursor.getString(cursor.getColumnIndex("billtype"));
            Log.i(TAG, "billtype: "+billtype);
            map.put("billtype",billtype);
            String goodsId = cursor.getString(cursor.getColumnIndex("goodsId"));
            Log.i(TAG, "goodsId: "+goodsId);
            map.put("goodsId",goodsId);
            String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
            Log.i(TAG, "goodsname: "+goodsname);
            map.put("goodsname",goodsname);
            String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
            Log.i(TAG, "barcode_header_in: "+barcode_header_in);

            String IN_QTY = cursor.getString(cursor.getColumnIndex("in_qty"));
            map.put("IN_QTY",IN_QTY);
            Log.i(TAG, "IN_QTY: "+IN_QTY);
            String OUT_QTY = cursor.getString(cursor.getColumnIndex("out_qty"));
            map.put("OUT_QTY",OUT_QTY);
            Log.i(TAG, "OUT_QTY: "+OUT_QTY);
            String inleft = cursor.getString(cursor.getColumnIndex("inleft"));
            map.put("inleft",inleft);//inleft outleft
            Log.i(TAG, "inleft: "+inleft);
            String outleft = cursor.getString(cursor.getColumnIndex("outleft"));
            map.put("outleft",outleft);
            Log.i(TAG, "outleft: "+outleft);
//            String outleft = cursor.getString(cursor.getColumnIndex("outleft"));
//            Log.i(TAG, "outleft: "+outleft);
//            map.put("outleft",outleft);
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            map.put("barcodeLeftpos",barcodeLeftpos);
            Log.i(TAG, "barcodeLeftpos: "+barcodeLeftpos);
            String innerno = cursor.getString(cursor.getColumnIndex("innerno"));
            map.put("innerno",innerno);
            Log.i(TAG, "innerno: "+innerno);
            String outerno = cursor.getString(cursor.getColumnIndex("outerno"));
            map.put("outerno",outerno);
            Log.i(TAG, "outerno: "+outerno);


            String district = cursor.getString(cursor.getColumnIndex("district"));
            Log.i(TAG, "district: "+district);
            String _shipdate = cursor.getString(cursor.getColumnIndex("shipdate"));
            Log.i(TAG, "_shipdate: "+_shipdate);
//            String bar69in = cursor.getString(cursor.getColumnIndex("bar69in"));
//            map.put("bar69in",bar69in);
//            String bar69out = cursor.getString(cursor.getColumnIndex("bar69out"));
//            map.put("bar69out",bar69out);
//            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
//            map.put("minLen",minLen);
//            String barcode2 = cursor.getString(cursor.getColumnIndex("barcode2"));
//            map.put("barcode2",barcode2);
//            Log.i(TAG, "barcode2: "+barcode2);
//            String is_in = cursor.getString(cursor.getColumnIndex("is_in"));
//            map.put("is_in",is_in);
//            Log.i(TAG, "is_in: "+is_in);
            list.add(map);
        }
        Log.i(TAG, "list.size: "+list.size());
        data=getGoodsListMulti(list);//合并相同的goodid
        return data;
    }

    //调出 单据货物清单。
    public static List<Goods> getGoodsListByBillNo(String InoutFlag,String StoreId, String BillType,String BillNo, Context context) {
        List<Goods> tempList = new ArrayList<Goods>();



        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();

        //测试数据 入 98198868   出 98743732
        //测试数据 214 出 98743733
        // 测试流程
        //1 点进去，打开窗口，
        // 加载订单数据：goodsid，billno，qty
        // 12313 98732343  3
        // 32122 98732343  1
        // 放进 List goodsList
        // 2 scangoods
        // 扫货 结果 得到条码  1231312121 ---和goodslist,for  判断 ,
        //   if (goodsid=="1231312121") 找到 第一个型号匹配上了， 数量减少 -1, 提示数量，显示条码。
        //  else 提示错误，数量不减

        //

//        Cursor cursor=db.rawQuery("SELECT * FROM shiplist a where inoutFlag=?  and a.billtype=? and a.billno=? and a.storeid=?" ,  new String[]{InoutFlag,"出","98743733","214"});
//        Cursor curso1r=db.rawQuery("SELECT * FROM shiplist " , null);
        Log.i(TAG, "InoutFlag: "+InoutFlag);
        Log.i(TAG, "BillType: "+BillType);
        Log.i(TAG, "BillNo: "+BillNo);
        Log.i(TAG, "StoreId: "+StoreId);
        /*  外连接分左连接和右连接，
            左连接以左表为基表，左表全部显示包括空值，右表关联展示，右连接反之。
            内连接可以理解为左连接和右连接的交集。*/
        //正确的数据
        Cursor cursor_shiplist =db.rawQuery("SELECT * FROM shiplist a inner join barcode_header b on a.goodsId=b.goodsId " +
                        " left join code_header_multi c on b.goodsid=c.goodsid " +
                        " where  IN_QTY+OUT_QTY-CIN_QTY-COUT_QTY>0 " +
                        " and  inoutFlag=? and a.billtype=? and a.billno=?and storeid=?order by b.goodsname",
                // -1  in 1 ， out -1,billtype="出"  '98421221', 214
                new String[]{InoutFlag,BillType, BillNo, StoreId});
//                new String[]{"-1","出","98743840","214"});//测试数据


        while(cursor_shiplist.moveToNext()){
            String storeid = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("storeid"));
//            Log.i(TAG, "storeid: >>>>>>"+storeid);
            String carname = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("carname"));
//            Log.i(TAG, "carname: >>>>>>"+carname);
            String shipdate = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("shipdate"));
//            Log.i(TAG, "shipdate: >>>>>>"+shipdate);
            String seqno = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("seqno"));
//            Log.i(TAG, "seqno: >>>>>>"+seqno);
            String billtype = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("billtype"));
//            Log.i(TAG, "billtype: >>>>>>"+billtype);
            String billno = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("billno"));
//            Log.i(TAG, "billno: >>>>>>"+billno);
            String company_address = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("company_address"));
//            Log.i(TAG, "company_address: >>>>>>"+company_address);
            String goodsId = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("goodsId"));
//            Log.i(TAG, "goodsId: >>>>>>"+goodsId);
            String goodsname = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("goodsname"));
//            Log.i(TAG, "goodsname: >>>>>>"+goodsname);
            String in_qty = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("in_qty"));
//            Log.i(TAG, "in_qty: >>>>>>"+in_qty);
            String cin_qty = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("cin_qty"));
//            Log.i(TAG, "cin_qty: >>>>>>"+cin_qty);
            String out_qty = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("out_qty"));
//            Log.i(TAG, "out_qty: >>>>>>"+out_qty);
            String cout_qty = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("cout_qty"));
//            Log.i(TAG, "cout_qty: >>>>>>"+cout_qty);
            String innerno = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("innerno"));
//            Log.i(TAG, "innerno: >>>>>>"+innerno);
            String outerno = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("outerno"));
//            Log.i(TAG, "outerno: >>>>>>"+outerno);
            String cm = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("cm"));
//            Log.i(TAG, "cm: >>>>>>"+cm);
            String barcode_header_in = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode_header_in"));
//            Log.i(TAG, "barcode_header_in: >>>>>>"+barcode_header_in);
            String barcode_header_out = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode_header_out"));
//            Log.i(TAG, "barcode_header_out: >>>>>>"+barcode_header_out);
            String minLen = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("minLen"));
//            Log.i(TAG, "minLen: >>>>>>>>>>>"+minLen);
            String revisetime = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("revisetime"));
//            Log.i(TAG, "revisetime: >>>>>>>>>>>"+revisetime);
            String is_in = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("is_in"));
//            Log.i(TAG, "is_in: >>>>>>"+is_in);

            String barcodeLeftpos = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("revisetime"));
            Log.i(TAG, "barcodeLeftpos: >>>>>>"+barcodeLeftpos);




            Goods goods=new Goods();
            if (cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode_header_in")).contains("|")||cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode_header_out")).contains("|")){
                if (cursor_shiplist.getString(cursor_shiplist .getColumnIndex("is_in")).equals("0")){
                    String barcode = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode"));
                    Log.i(TAG, "外机为barcode>>>>>>>>>>>: "+barcode);
                    goods.setBarcode_out(barcode);
                }else{
                    String barcode = cursor_shiplist.getString(cursor_shiplist .getColumnIndex("barcode"));
                    Log.i(TAG, "内机为barcode>>>>>>>>>>>: "+barcode);
                    goods.setBarcode_in(barcode);
                }

            }else{
                goods.setBarcode_in(barcode_header_in);
                goods.setBarcode_out(barcode_header_out);
            }
            tempList.add(goods);

        }

        return tempList;
    }
//

    //上2个函数公用，合并code_header_multi 的 多重条码
    public static List<Goods> getGoodsListMulti(List<Map<String,Object>> listdate){
        Log.i(TAG, "listdate.size: "+listdate.size());
        List<Goods> tempList = new ArrayList<Goods>();
        tempList.clear();

        //合并相同的shipid
//        for  ( int  i  =   0 ; i  <  listdate.size()  -   1 ; i ++ )   {
//            for  ( int  j  =  listdate.size()  -   1 ; j  >  i; j -- )   {
//                if (listdate.get(i).get("goodsId").equals(listdate.get(j).get("goodsId"))) {
//
//                    Map<String, Object> map = new HashMap<>();
//                    String innerno = listdate.get(i).get("innerno") + "|" + listdate.get(j).get("innerno");
//                    map.put("innerno", innerno);
//                    String outerno = listdate.get(i).get("outerno") + "|" + listdate.get(j).get("outerno");
//                    map.put("outerno", outerno);
//                    map.put("billno", listdate.get(i).get("billno").toString()+listdate.get(j).get("billno").toString());
//                    map.put("billtype", listdate.get(i).get("billtype").toString()+listdate.get(j).get("billtype").toString());
//                    String carId = listdate.get(i).get("carId") + "|" + listdate.get(j).get("carId");
//                    map.put("carId", carId);
//                    map.put("goodsId", listdate.get(i).get("goodsId"));
//                    map.put("goodsname", listdate.get(i).get("goodsname").toString()+listdate.get(j).get("goodsname").toString());
//
//                    int inleft=Integer.valueOf(listdate.get(i).get("inleft").toString())+Integer.valueOf(listdate.get(j).get("inleft").toString());
//                    map.put("inleft", String.valueOf(inleft));
//                    int outleft=Integer.valueOf(listdate.get(i).get("outleft").toString())+Integer.valueOf(listdate.get(j).get("outleft").toString());
//                    map.put("outleft", String.valueOf(outleft));
//
//                    int IN_QTY=Integer.valueOf(listdate.get(i).get("IN_QTY").toString())+Integer.valueOf(listdate.get(j).get("IN_QTY").toString());
//                    map.put("IN_QTY", String.valueOf(IN_QTY));
//                    int OUT_QTY=Integer.valueOf(listdate.get(i).get("OUT_QTY").toString())+Integer.valueOf(listdate.get(j).get("OUT_QTY").toString());
//                    map.put("OUT_QTY", String.valueOf(OUT_QTY));
//
//                    map.put("barcodeLeftpos", listdate.get(i).get("barcodeLeftpos").toString()+listdate.get(j).get("barcodeLeftpos").toString());
////                        map.put("bar69in",listdate.get(i).get("bar69in"));
////                        map.put("bar69out",listdate.get(i).get("bar69out"));
////                        map.put("minLen",listdate.get(i).get("minLen"));
////                        map.put("barcode2",listdate.get(i).get("barcode2"));
////                        map.put("is_in",listdate.get(i).get("is_in"));
//                    listdate.set(i, map);
//                    listdate.remove(j);
//
//                }
//            }
//        }

                Log.i(TAG, "listdate.size:合并后的数据 "+listdate.size());
                for (int k = 0; k <listdate.size() ; k++) {
                    Goods goods=new Goods();
                    goods.setCarId(listdate.get(k).get("carId").toString());//carId
                    goods.set_id(listdate.get(k).get("_id").toString());//carId
                    goods.setGoodsId(listdate.get(k).get("goodsId").toString());
                    Log.i(TAG, "goodsId: "+listdate.get(k).get("goodsId").toString());
                    goods.setGoodsname(listdate.get(k).get("goodsname").toString());
                    goods.setBillNo(listdate.get(k).get("billno").toString());
                    goods.setBilltype(listdate.get(k).get("billtype").toString());
                    goods.setBarcode_in(listdate.get(k).get("innerno").toString());
                    goods.setBarcode_out(listdate.get(k).get("outerno").toString());
                    goods.setInQty(listdate.get(k).get("inleft").toString());
                    goods.setOutQty(listdate.get(k).get("outleft").toString());
                    goods.setBarcodeLeftPos(listdate.get(k).get("barcodeLeftpos").toString());
                    goods.setInoutFlag(listdate.get(k).get("inoutFlag").toString());
                    if (listdate.get(k).get("goodsname").toString().contains("空调")){//inleft outleft
                        int allqty=Integer.valueOf(listdate.get(k).get("IN_QTY").toString())+Integer.valueOf(listdate.get(k).get("OUT_QTY").toString());
                        goods.setAllqty(String.valueOf(allqty));
                    }else{
                        goods.setAllqty(listdate.get(k).get("IN_QTY").toString());
                    }

                    tempList.add(goods);
                }


        Log.i(TAG, "返回结果值: ");
            return tempList;

            }
    private static List<Goods> goodslist=new ArrayList<>();
    public static List<Goods> getScanDataCompare(String id,Context context){
        dDBinventory=new DBinventory(context);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        Log.i(TAG, "开始查询 ");
        Cursor cursor = db.rawQuery("select b.*,c.*,a.*,a._id  ,a.in_qty-a.cin_qty as inleft,a.out_qty-a.cout_qty as outleft  FROM shiplist a inner join barcode_header b on a.goodsId=b.goodsId " +
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
            String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos"));
            Log.i(TAG, "标准表:barcodeLeftpos"+barcodeLeftpos);
            String minLen = cursor.getString(cursor.getColumnIndex("minLen"));
            Log.i(TAG, "标准表:minLen"+minLen);



            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>临时单个条码表<<<<<<<<<<<<<<<<<<<<<<<<<");
            String is_in = cursor.getString(cursor.getColumnIndex("is_in"));
            Log.i(TAG, "临时单个条码表:is_in: "+is_in);
            String barcode = cursor.getString(cursor.getColumnIndex("barcode"));
            Log.i(TAG, "临时单个条码表:barcode: "+barcode);
            if (cursor.getCount()>1){
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
            goodslist.add(goods);
        }
        return goodslist;
    }


    }

