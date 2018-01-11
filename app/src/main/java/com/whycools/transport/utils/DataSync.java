package com.whycools.transport.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.whycools.transport.AppInterface;
import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.crash.util.DateUtils;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.Scanlistbak;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.entity.Stkcksum;
import com.whycools.transport.entity.Stores;
import com.whycools.transport.service.BarcodeHeaderService;
import com.whycools.transport.service.CodeHeaderMultiService;
import com.whycools.transport.service.NewStkcksumService;
import com.whycools.transport.service.NewbarcodeheaderService;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ScanlistbakService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.service.StoresService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库中的数据与服务器同步
 * Created by Zero on 2016-12-06.
 */

public class DataSync implements AppInterface {
    private static final String TAG="数据同步中";
    private BarcodeHeaderService barcodeHeaderService;//标准表数据库
    private NewbarcodeheaderService newbarcodeheaderService;//新标准表数据库
    private CodeHeaderMultiService codeHeaderMultiService;//单个标准表数据库
    private NewStkcksumService newStkcksumService;//盘点表数据库
    private ShipListService shiplistservice;//发货清单
    private StoresService storesservice;
    private Context context;
    private String url;
    private String serialnumber;
    private MaterialDialog dialog;

    public  DataSync(Context context,String url,String serialnumber){
        this.serialnumber=serialnumber;
        this.url=url;
        this.context=context;
        barcodeHeaderService=new BarcodeHeaderService(context);//标准数据库实例化
        newbarcodeheaderService=new NewbarcodeheaderService(context);//标准数据库实例化
        codeHeaderMultiService=new CodeHeaderMultiService(context);//单个标准数据库实例化
        newStkcksumService=new NewStkcksumService(context);//盘点表数据库实例化
        shiplistservice=new ShipListService(context);
        storesservice=new StoresService(context);

        dialog = new MaterialDialog.Builder(context)
                .title("数据同步中")
                .content("数据正在加载中，请稍后...")
                .progress(false, 100, true)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        new Thread(){
            @Override
            public void run() {
                super.run();
                //仓库
                getStoresData();
                //盘点表
                gettNewStkcksumData();
                //标准表
                getBarcodeData();
                //新标准表
                //getgetNewBarcodeData();
                //发货清单
                getShipListData();

                Message msg=new Message();
                msg.what=2;
                handler.sendMessage(msg);

            }
        }.start();

    }

    //盘点表数据加载中
    public void gettNewStkcksumData(){
        String getStoreid=SharedPreferencesUtil.getData(context,"StoreId","214").toString();
        String strzip="getInventory  "+getStoreid;
        String url_stkcksum=url+"rent/rProxy.jsp?deviceid="+"&s="+Zip.compress(strzip);
        Log.i(TAG, "盘点表文件下载url: "+url_stkcksum);
        String resultStkcksum= RequestData.getResult(url_stkcksum);
        Log.i(TAG, "盘点表的数据: "+resultStkcksum);
        newStkcksumService.clearStkcksum();//盘点表数据库清除
        try {
            JSONObject obj=new JSONObject(resultStkcksum);
            JSONArray array = obj.getJSONArray("results");
            dialog.setMaxProgress(array.length());
            sendContext(R.string.sys_stkcksum);
            for (int i = 0; i < array.length(); i++) {
                JSONObject data = array.getJSONObject(i);
                String SumDate = data.has("SumDate") ? data.getString("SumDate") : "";//发货日期  2116-01-25
                String storeid = data.has("storeid") ? data.getString("storeid") : "";//仓库id214
                String auto_id = data.has("goodsId") ? data.getString("goodsId") : "";
                String classname = data.has("classname") ? data.getString("classname") : "";
                String goodsno = data.has("goodsno") ? data.getString("goodsno") : "";
                String goodsname = data.has("goodsname") ? data.getString("goodsname") :"";
                String qmyereal = data.has("qmyereal") ? data.getString("qmyereal") : "";//入
                String qmyecount = data.has("qmyecount") ? data.getString("qmyecount") : "";//发货编号
                String isMove = data.has("isMove") ? data.getString("isMove") : "";//地址
                newStkcksumService.addStkcksum(auto_id,SumDate,storeid,goodsno,classname,goodsname,qmyereal,qmyecount,isMove);
                sendMsg(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //标准表数据同步中
    public void getBarcodeData(){
        String lasttime=barcodeHeaderService.lastTime();//获取标准表数据库最后的时间
        //http://10.10.10.2:89/rent/getBarcodeHeader.jsp?deviceid=800&revisetime=2011-01-14 15:25:04.803
        String url_barcodeHeader=url+"rent/getBarcodeHeader.jsp?deviceid="+"&revisetime="+lasttime;
        Log.i(TAG, "标准码文件url: "+url_barcodeHeader);

        String resultBarcode=RequestData.getResult(url_barcodeHeader);

        Log.i(TAG, "resultBarcode: "+ resultBarcode);
        String[] strArray = resultBarcode.split(String.valueOf(new char[]{(char) 129}));
        dialog.setMaxProgress(strArray.length);
        sendContext(R.string.sys_barcode);
        for (int i = 0; i < strArray.length; i++) {
            String res = strArray[i].toString();
            String[] strArray2; //回车分割
            //10001海信空调KFR-2826GW/BP 1P分体挂机1KB28901CNG1KB28901CNN12011-01-14 15:25:04.80323
            strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
            if (strArray2.length == 9) {
                barcodeHeaderService.addBarcodeHeader(false, strArray2[0], strArray2[1], strArray2[2], strArray2[3], strArray2[4], strArray2[5], strArray2[6], strArray2[7], strArray2[8]);

                if (strArray2[2].indexOf("|") > 0) {
                    String[] str_in = strArray2[2].split("\\|");
                    if (str_in.length > 1) {
                        for (int a = 0; a < str_in.length; a++) {
                            codeHeaderMultiService.addCodeHeaderMulti(strArray2[0], "1", strArray2[6], str_in[a], strArray2[4]);
                        }
                    }
                }
                if (strArray2[3].indexOf("|") > 0) {
                    String[] str_out = strArray2[3].split("\\|");
                    if (str_out.length > 1) {
                        for (int b = 0; b < str_out.length; b++) {
                            codeHeaderMultiService.addCodeHeaderMulti(strArray2[0], "0", strArray2[7], str_out[b], strArray2[4]);
                        }
                    }
                }
            }
            sendMsg(i);
        }

    }
    public void getgetNewBarcodeData(){
        String lasttime=newbarcodeheaderService.lastTime();//获取标准表数据库最后的时间
        String url_barcodeHeader=url+"rent/getBarcodeHeader.jsp?deviceid="+"&revisetime="+lasttime;
        String resultBarcode=RequestData.getResult(url_barcodeHeader);
        String[] strArray = resultBarcode.split(String.valueOf(new char[]{(char) 129}));
        dialog.setMaxProgress(strArray.length);
        sendContext(R.string.sys_newBarcode);
        //   dialog.setContent("新标准表同步中...");
        for (int i = 0; i < strArray.length; i++) {
            String res = strArray[i].toString();
            String[] strArray2; //回车分割
            //10001海信空调KFR-2826GW/BP 1P分体挂机1KB28901CNG1KB28901CNN12011-01-14 15:25:04.80323
            strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
            Log.i(TAG, " >>>>>>>>>>>>>>>" + strArray2.length);
            if (strArray2.length == 9) {
                if(strArray2[2].indexOf("|")!=-1){
                    Log.i(TAG, "多重内机----: "+strArray2[2]);
                    String []  inbarcode=strArray2[2].split("\\|");
                    for (int k = 0; k <inbarcode.length; k++) {
                        Log.i(TAG, "多重内机----: "+inbarcode[k]);
                        if(inbarcode[k].length()>0){
                            Newbarcodeheader newbarcodeheader=new Newbarcodeheader();
                            newbarcodeheader.setGoodsId(strArray2[0]);
                            newbarcodeheader.setGoodsname(strArray2[1]);
                            newbarcodeheader.setBarcode_header(inbarcode[k]);
                            newbarcodeheader.setIsIn("1");
                            newbarcodeheader.setBarcodeLeftpos(strArray2[4]);
                            newbarcodeheader.setRevisetime(strArray2[5]);
                            newbarcodeheader.setBar69in(strArray2[6]);
                            newbarcodeheader.setBar69out(strArray2[7]);
                            newbarcodeheader.setMinLen(strArray2[8]);
                            newbarcodeheaderService.addNewbarcodeheader(newbarcodeheader);
                        }
                    }
                }
                if(strArray2[3].indexOf("|")!=-1){
                    Log.i(TAG, "多重外机: "+strArray2[3]);
                    String []  outbarcode=strArray2[3].split("\\|");
                    for (int f = 0; f <outbarcode.length; f++) {
                        Log.i(TAG, "多重外机: "+outbarcode[f]);
                        if(outbarcode[f].length()>0){
                            Newbarcodeheader newbarcodeheader=new Newbarcodeheader();
                            newbarcodeheader.setGoodsId(strArray2[0]);
                            newbarcodeheader.setGoodsname(strArray2[1]);
                            newbarcodeheader.setBarcode_header(outbarcode[f]);
                            newbarcodeheader.setIsIn("0");
                            newbarcodeheader.setBarcodeLeftpos(strArray2[4]);
                            newbarcodeheader.setRevisetime(strArray2[5]);
                            newbarcodeheader.setBar69in(strArray2[6]);
                            newbarcodeheader.setBar69out(strArray2[7]);
                            newbarcodeheader.setMinLen(strArray2[8]);
                            newbarcodeheaderService.addNewbarcodeheader(newbarcodeheader);

                        }

                    }

                }

                if(strArray2[2].length()>0&&strArray2[2].indexOf("|")==-1){
                    Newbarcodeheader newbarcodeheader1=new Newbarcodeheader();
                    newbarcodeheader1.setGoodsId(strArray2[0]);
                    newbarcodeheader1.setGoodsname(strArray2[1]);
                    newbarcodeheader1.setBarcode_header(strArray2[2]);
                    newbarcodeheader1.setIsIn("1");
                    newbarcodeheader1.setBarcodeLeftpos(strArray2[4]);
                    newbarcodeheader1.setRevisetime(strArray2[5]);
                    newbarcodeheader1.setBar69in(strArray2[6]);
                    newbarcodeheader1.setBar69out(strArray2[7]);
                    newbarcodeheader1.setMinLen(strArray2[8]);
                    newbarcodeheaderService.addNewbarcodeheader(newbarcodeheader1);
                }
                if(strArray2[3].length()>0&&strArray2[3].indexOf("|")==-1){
                    Newbarcodeheader newbarcodeheader2=new Newbarcodeheader();
                    newbarcodeheader2.setGoodsId(strArray2[0]);
                    newbarcodeheader2.setGoodsname(strArray2[1]);
                    newbarcodeheader2.setBarcode_header(strArray2[3]);
                    newbarcodeheader2.setIsIn("0");
                    newbarcodeheader2.setBarcodeLeftpos(strArray2[4]);
                    newbarcodeheader2.setRevisetime(strArray2[5]);
                    newbarcodeheader2.setBar69in(strArray2[6]);
                    newbarcodeheader2.setBar69out(strArray2[7]);
                    newbarcodeheader2.setMinLen(strArray2[8]);
                    newbarcodeheaderService.addNewbarcodeheader(newbarcodeheader2);
                }

            }
            sendMsg(i);

        }


    }

    public void getShipListData(){
        shiplistservice.clearShipListData();//"getShiplist4gun  '"+sShipdate+"',"+iUserId  //getShiplist4gun  '"+2017-01-05', 201
        String userid="getShiplist4gun "+SharedPreferencesUtil.getData(context,"userid","").toString();
        Log.i(TAG, "清单压缩字符串: "+userid);
        String shiplist_url = url + "rent/rProxy.jsp?deviceid=" + "&s=" + Zip.compress(userid);
        Log.i(TAG, "shiplist清单: "+shiplist_url);
        String result = RequestData.getResult(shiplist_url);
        result=result.replaceAll( "\\\\",  "");
        Log.i(TAG, "result: "+result);//province, city, district
        if (result.length()<15){
            result="数据请求失败，请重新请求";
            Log.i(TAG, "数据请求失败，请重新请求");
        }else{
            shiplistservice.clearShipListData();//清除shiplist数据库表中的数据
            Log.i(TAG, "shiplistservice数据库表中数据清除并开始解析数据添加到数据");
            try{
                Log.i(TAG, "解析result"+result);
                JSONObject obj = new JSONObject(result);
                JSONArray array = obj.getJSONArray("results");

                Log.i(TAG, "shiplist清单下载的个数: "+array.length());
                dialog.setMaxProgress(array.length());
                sendContext(R.string.sys_shiplist);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String storeid = data.has("storeid") ? data.getString("storeid") : "";//仓库id  214
                    String shipdate = data.has("shipdate") ? data.getString("shipdate") : "";//发货日期  2116-01-25
                    Log.i(TAG, "shipdate: "+shipdate);
                    String seqno = data.has("seqno") ? data.getString("seqno") : "";//0
                    String carid = data.has("carid") ? data.getString("carid") : "";    //0
                    Log.i(TAG, "carid: "+carid);
                    String carname = data.has("carname") ? data.getString("carname") : "";//""
                    Log.i(TAG, "carname: "+carname);
                    String billtype = data.has("billtype") ? data.getString("billtype") : "";//入
                    Log.i(TAG, "billtype: "+billtype);
                    String orderno = data.has("orderno") ? data.getString("orderno") : "";//发货编号
                    Log.i(TAG, "发货编号: "+orderno);
                    String address_company = data.has("address_company") ? data.getString("address_company") : "";//地址
                    String goodsid = data.has("goodsid") ? data.getString("goodsid") : "";//产品编号33311
                    String goodsname = data.has("goodsname") ? data.getString("goodsname") : "";//产品名称
                    String innerno = data.has("innerno") ? data.getString("innerno") : "";//编码
                    String outerno = data.has("outerno") ? data.getString("outerno") : "";//外机编码
                    String inoutFlag = data.has("inoutFlag") ? data.getString("inoutFlag") : "";//-1
                    Log.i(TAG, "inoutFlag: "+inoutFlag);
                    String address = data.has("address") ? data.getString("address") : "";//地址
                    String gsm = data.has("gsm") ? data.getString("gsm") : "";//电话号码
                    String qty = data.has("qty") ? data.getString("qty") : "";//数量
                    String stateId = data.has("stateId") ? data.getString("stateId") : "";//标识

                    String province = data.has("province") ? data.getString("province") : "";//省
                    String city = data.has("city") ? data.getString("city") : "";//市
                    String district = data.has("district") ? data.getString("district") : "";//区
                    Log.i(TAG, "district: "+district);
                    ShipList shiplist=new ShipList();
                    shiplist.setStoreid(storeid);
                    shiplist.setShipdate(shipdate);
                    shiplist.setSeqno(seqno);
                    shiplist.setCarId(carid);
                    shiplist.setCarname(carname);
                    shiplist.setBilltype(billtype);
                    shiplist.setBillno(orderno);
                    shiplist.setCompany_address(address_company);
                    shiplist.setGoodsId(goodsid);
                    shiplist.setGoodsname(goodsname);
                    shiplist.setInnerno(innerno);
                    shiplist.setOuterno(outerno);
                    shiplist.setInoutFlag(inoutFlag);
                    shiplist.setAddress(address);
                    shiplist.setPhonenumber(gsm);
                    shiplist.setProvince(province);
                    shiplist.setCity(city);
                    shiplist.setDistrict(district);
                    if (goodsname.contains("空调")||goodsname.contains("三菱重工海尔风管机RFUTD50WDV 2P")){
                        if (goodsname.contains("内机")||goodsname.contains("外机")){
                            shiplist.setIn_qty(qty);
                            shiplist.setOut_qty("0");
                        }else{
                            shiplist.setIn_qty(qty);
                            shiplist.setOut_qty(qty);
                        }

                    }else {
                        shiplist.setIn_qty(qty);
                        shiplist.setOut_qty("0");
                    }
                    shiplist.setCin_qty("0");
                    shiplist.setCout_qty("0");
                    if (stateId.equals("3")){
                        shiplist.setIsfinish("已完成");
                    }else{
                        shiplist.setIsfinish("完成");
                    }

                    if (stateId.equals("0")){
                        shiplist.setIsStart("送达");
                    }else{
                        shiplist.setIsStart("发车");
                    }

                    shiplist.setDistance("0.0公里");
                    shiplist.setStateId(stateId);
                    shiplistservice.addShipListData(shiplist);

                    sendMsg(i);
                }
                Log.i(TAG, "数据请求成功添加数据");


                result="数据请求成功";
            }catch (Exception e){
                Log.i(TAG, "错误: "+e.getMessage());
            }
        }
        Error.contentToTxt(context,getDBData());
    }
    public void getStoresData(){
        String url_stores=url+"rent/rProxyDset.jsp?deviceid="+"&s="+ Zip.compress("select auto_id,storename,address,phoneno, manager from stores (nolock) where is_use=1 order by storename");
        Log.i(TAG, "仓库文件下载url: "+url_stores);
        String returnStores=RequestData.getResult(url_stores);
        storesservice.clearstores();
        Log.i(TAG, "returnStores: "+returnStores);
        String[]	 strArray = returnStores.split(String.valueOf(new char[]{(char) 129}));
        //   dialog.setContent("仓库数据同步中");
        dialog.setMaxProgress(strArray.length);
        sendContext(R.string.sys_stores);
        for (int i = 0; i < strArray.length; i++) {
            String res = strArray[i].toString();
            String[] strArray2; //回车分割
            strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
            Log.i(">>>>>>>>>>>>>>>>>>>>", " >>>>>>>>>>>>" + strArray2.length);
            if (strArray2.length == 5) {
                Stores stores = new Stores();
                Log.i(TAG, "StoreId: >>>>>>>>>>>>>>><<<<<<<"+strArray2[0]);
                stores.setStoreId(strArray2[0]);
                stores.setStorename(strArray2[1]);
                stores.setAddress(strArray2[2]);
                stores.setPhoneno(strArray2[3]);
                stores.setManager(strArray2[4]);
                storesservice.addStkcksum(stores);
            }
            sendMsg(i);
        }


    }

    /**
     * 上传错误的log日志
     */
    private void sendLog( ) {
        String path = Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + serialnumber+DateUtils
                .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
        File file= new File(path);
        String lid="1c827923-620b-47be-a8c7-7b34d87d705f";
        RequestParams params = new RequestParams();
        params.addBodyParameter("attach", file, "txt");
        String url_serveraddress= SharedPreferencesUtil.getData(context,"ServerAddress",SERVERDDRESS).toString();
        //http://10.10.10.2:89/rent/simpleUpload
        String url=url_serveraddress+"update/simpleUpload.jsp?path=applog";
        uploadMethod(params,url);
    }
    private void uploadMethod(RequestParams params, String uploadHost) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        dialog.setContent(R.string.sys_log);
                    }
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        dialog.setMaxProgress((int)total);
                        dialog.setProgress((int)current);
                    }

                    @Override
                    public void onSuccess(
                            ResponseInfo<String> responseInfo) {
                        dialog.setContent(R.string.sys_log_success);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(HttpException error,
                                          String msg) {
                        dialog.setContent(R.string.sys_log_fail);
                        dialog.dismiss();

                    }
                });

    }
   private String getDBData(){
       DBinventory dDBinventory= new DBinventory(context);
       SQLiteDatabase db = dDBinventory.getWritableDatabase();
       StringBuffer buffer=new StringBuffer();
       Cursor cursor = db.rawQuery("select * from shiplist ", null);
       buffer.append("shiplist清单总数为："+cursor.getCount()+"\r\n");
       Cursor cursor_billtype_in = db.rawQuery("select * from shiplist where billtype=?", new String[]{"入"});
       buffer.append("shiplist清单中billtype=入所有个数 ："+cursor_billtype_in.getCount()+"\r\n");
       Cursor cursor_billtype_out = db.rawQuery("select * from shiplist where billtype=?", new String[]{"出"});
       buffer.append("shiplist清单中billtype=出所有个数 ："+cursor_billtype_out.getCount()+"\r\n");
       Cursor cursor_carId = db.rawQuery("select * from shiplist where carId>0", null);
       buffer.append("shiplist清单中carId>0所有个数 ："+cursor_carId.getCount()+"\r\n");
       Cursor cursor_carId_billtype_in = db.rawQuery("select * from shiplist where carId>0  and billtype=?", new String[]{"入"});
       buffer.append("shiplist清单中carId>0billtype=入所有个数 ："+cursor_carId_billtype_in.getCount()+"\r\n");
       Cursor cursor_carId_billtype_in_today = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"入", DateUtile.getdata(0)});
       buffer.append("shiplist清单中carId>0billtype=入今天所有个数 ："+cursor_carId_billtype_in_today.getCount()+"\r\n");
       Cursor cursor_carId_billtype_in_tomorrow = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"入", DateUtile.getdata(1)});
       buffer.append("shiplist清单中carId>0billtype=入明天所有个数 ："+cursor_carId_billtype_in_tomorrow.getCount()+"\r\n");
       Cursor cursor_carId_billtype_out = db.rawQuery("select * from shiplist where carId>0  and billtype=?", new String[]{"出"});
       buffer.append("shiplist清单中carId>0billtype=出所有个数 ："+cursor_carId_billtype_out.getCount()+"\r\n");
       Cursor cursor_carId_billtype_out_today = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"出",DateUtile.getdata(0)});
       buffer.append("shiplist清单中carId>0billtype=出今天所有个数 ："+cursor_carId_billtype_out_today.getCount()+"\r\n");
       Cursor cursor_carId_billtype_out_tomorrow = db.rawQuery("select * from shiplist where carId>0  and billtype=? and shipdate=?", new String[]{"出",DateUtile.getdata(1)});
       buffer.append("shiplist清单中carId>0billtype=出明天所有个数 ："+cursor_carId_billtype_out_tomorrow.getCount()+"\r\n");
       Cursor cursorlist = db.rawQuery("select * from shiplist where carId=0", null);
       buffer.append("shiplist清单中carId=0所有个数 ："+cursorlist.getCount()+"\r\n");
       Cursor cursorlist_in = db.rawQuery("select * from shiplist where carId=0 and billtype=?", new String[]{"入"});
       buffer.append("shiplist清单中carId=0billtype=入所有个数 ："+cursorlist_in.getCount()+"\r\n");

       Cursor cursorlist_out = db.rawQuery("select * from shiplist where carId=0 and billtype=?", new String[]{"出"});
       buffer.append("shiplist清单中carId=0billtype=出所有个数 ："+cursorlist_out.getCount()+"\r\n");

       Cursor cursorlist_out_today = db.rawQuery("select * from shiplist where carId=0  and billtype=? and shipdate=?", new String[]{"出",DateUtile.getdata(0)});
       buffer.append("shiplist清单中carId=0billtype=出今天所有个数 ："+cursorlist_out_today.getCount()+"\r\n");
       Cursor cursorlist_outt_tomorrow = db.rawQuery("select * from shiplist where carId=0  and billtype=? and shipdate=?", new String[]{"出",DateUtile.getdata(1)});

       buffer.append("shiplist清单中carId=0billtype=出明天所有个数 ："+cursorlist_outt_tomorrow.getCount()+"\r\n");
       return buffer.toString();
   }
   private void sendMsg(int index){
       Message msg=new Message();

       Bundle bundle=new Bundle();
       bundle.putInt("index",index);
       msg.setData(bundle);
       msg.what=1;
       handler.sendMessage(msg);
   }
    private void sendContext(int strcontext){
        Message msg=new Message();

        Bundle bundle=new Bundle();
       bundle.putInt("context",strcontext);
        msg.setData(bundle);
        msg.what=3;
        handler.sendMessage(msg);
    }
   Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case 1:
                   dialog.setProgress(msg.getData().getInt("index"));
                   break;
               case 2:
                   sendLog();
                   break;
               case 3:
                   dialog.setContent(msg.getData().getInt("context"));
                   break;
           }
       }
   };


}
