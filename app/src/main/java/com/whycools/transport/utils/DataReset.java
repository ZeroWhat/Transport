package com.whycools.transport.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.whycools.transport.AppInterface;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.entity.Stores;
import com.whycools.transport.service.BarcodeHeaderService;
import com.whycools.transport.service.CodeHeaderMultiService;
import com.whycools.transport.service.NewbarcodeheaderService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.service.StkcksumService;
import com.whycools.transport.service.StoresService;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 数据重置中，所有数据库表中的数据全部清除
 * Created by Zero on 2016-12-06.
 */
public class DataReset implements AppInterface{

    private static final String TAG="数据重置中";
    private BarcodeHeaderService barcodeHeaderService;//标准表数据库
    private NewbarcodeheaderService newbarcodeheaderService;//新标准表数据库
    private CodeHeaderMultiService codeHeaderMultiService;//单个标准表数据库
    private StkcksumService stkcksumService;//盘点表数据库
    private ShipListService shiplistservice;//发货清单
    private StoresService  storesservice;
    private Context context;
    private String url;

    public  DataReset(Context context,String url){
        this.url=url;
        this.context=context;
        barcodeHeaderService=new BarcodeHeaderService(context);//标准数据库实例化
        newbarcodeheaderService=new NewbarcodeheaderService(context);//标准数据库实例化
        codeHeaderMultiService=new CodeHeaderMultiService(context);//单个标准数据库实例化
        stkcksumService=new StkcksumService(context);//盘点表数据库实例化
        shiplistservice=new ShipListService(context);
        storesservice=new StoresService(context);
            TransparentDialogUtil.showLoadingMessage(context,"盘点表数据重置中。。。",true);//对话框显示
            getStkcksumThrend();

    }
    public void getStkcksumThrend(){
        new Thread(){
            @Override
            public void run() {

                //获取仓库
                String getStoreid=SharedPreferencesUtil.getData(context,"StoreId","214").toString();
                String strzip="getInventory  "+getStoreid;
//                Log.i(TAG, "盘点表请求参数: "+strzip);
                String url_stkcksum=url+"rent/rProxy.jsp?deviceid="+"&s="+Zip.compress(strzip);
                Log.i(TAG, "盘点表文件下载url: "+url_stkcksum);
                String resultStkcksum= RequestData.getResult(url_stkcksum);
                stkcksumService.clearStkcksum();//盘点表数据库清除
                try {
//                    Log.i(TAG, "resultStkcksum: "+resultStkcksum);
                    JSONObject obj=new JSONObject(resultStkcksum);
                    JSONArray array = obj.getJSONArray("results");
                    double  y=(double)100/(double)array.length();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        String SumDate = data.has("SumDate") ? data.getString("SumDate") : "";//发货日期  2116-01-25
                        String storeid = data.has("storeid") ? data.getString("storeid") : "";//仓库id214
                        String auto_id = data.has("auto_id") ? data.getString("auto_id") : "";
                        String goodsno = data.has("goodsno") ? data.getString("goodsno") : "";
                        String goodsname = data.has("goodsname") ? data.getString("goodsname") :"";
                        String qmyereal = data.has("qmyereal") ? data.getString("qmyereal") : "";//入
                        String qmyecount = data.has("qmyecount") ? data.getString("qmyecount") : "";//发货编号
                        String isMove = data.has("isMove") ? data.getString("isMove") : "";//地址
                        stkcksumService.addStkcksum(auto_id,SumDate,storeid,goodsno,goodsname,qmyereal,qmyecount,isMove);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void getBarcodeThread(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                barcodeHeaderService.clearBarcodeHeader();//标准表数据清除
                codeHeaderMultiService.clearCodeHeaderMulti();//单个条形码数据清除

//                BarcodeHeaderService barcodeHeaderService=new BarcodeHeaderService(LoginActivity.this);
//                String lasttime=barcodeHeaderService.lastTime();//获取标准表数据库最后的时间
//                if (lasttime.length()>0){
//                    isupData=true;
//                }
                //http://10.10.10.2:89/rent/getBarcodeHeader.jsp?deviceid=800&revisetime=2011-01-14 15:25:04.803
                String url_barcodeHeader=url+"rent/getBarcodeHeader.jsp?deviceid="+"&revisetime=";
                Log.i(TAG, "标准码文件url: "+url_barcodeHeader);
                String resultBarcode=RequestData.getResult(url_barcodeHeader);

                Log.i(TAG, "resultBarcode: "+ resultBarcode);
                String[] strArray = resultBarcode.split(String.valueOf(new char[]{(char) 129}));
                for (int i = 0; i < strArray.length; i++) {
//                        Log.i("数据库每一条完整的数据为", "结果: "+strArray[i].toString()+">>>>>>>>>>>"+i);
                    String res = strArray[i].toString();
                    String[] strArray2; //回车分割
                    //10001海信空调KFR-2826GW/BP 1P分体挂机1KB28901CNG1KB28901CNN12011-01-14 15:25:04.80323
                    strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
                    Log.i(TAG, " >>>>>>>>>>>>>>>" + strArray2.length);
                    if (strArray2.length == 9) {
                        Log.i(TAG, "run0: "+strArray2[0]);
                        Log.i(TAG, "run1: "+strArray2[1]);
                        Log.i(TAG, "run2: "+strArray2[2]);
                        Log.i(TAG, "run3: "+strArray2[3]);
                        Log.i(TAG, "run4: "+strArray2[4]);
                        Log.i(TAG, "run5: "+strArray2[5]);
                        Log.i(TAG, "run6: "+strArray2[6]);
                        Log.i(TAG, "run7: "+strArray2[7]);
                        Log.i(TAG, "run8: "+strArray2[8]);

                        if(strArray2[1].equals("长虹空调KFR-35GW/DHF(W1-H)+1 1.5P壁挂式")){
                            Log.e(TAG, "run0: "+strArray2[0]);
                            Log.e(TAG, "run1: "+strArray2[1]);
                            Log.e(TAG, "run2: "+strArray2[2]);
                            Log.e(TAG, "run3: "+strArray2[3]);
                            Log.e(TAG, "run4: "+strArray2[4]);
                            Log.e(TAG, "run5: "+strArray2[5]);
                            Log.e(TAG, "run6: "+strArray2[6]);
                            Log.e(TAG, "run7: "+strArray2[7]);
                            Log.e(TAG, "run8: "+strArray2[8]);
                        }
                        barcodeHeaderService.addBarcodeHeader(false, strArray2[0], strArray2[1], strArray2[2], strArray2[3], strArray2[4], strArray2[5], strArray2[6], strArray2[7], strArray2[8]);
                        Error.contentToTxt(context,"标准条码插入数据:"+strArray2[0]+"----"+ strArray2[1]+"----"+strArray2[2]+"----"+ strArray2[3]+"----"+ strArray2[4]+"----"+strArray2[5]+"----"+ strArray2[6]+"----"+strArray2[7]+"----"+ strArray2[8]);
                        if (strArray2[2].indexOf("|") > 0) {
                            String[] str_in = strArray2[2].split("\\|");
                            if (str_in.length > 1) {
                                for (int a = 0; a < str_in.length; a++) {
//                                        Log.i(TAG, "str_in: " + strArray2[a1]+">>>>>"+a);
                                    codeHeaderMultiService.addCodeHeaderMulti(strArray2[0], "1", strArray2[6], str_in[a], strArray2[4]);
//                                        Log.i(TAG, "多重内机编码: " + str_in[a]);
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

                }
                Message msg=new Message();
                msg.what=5;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void getNewBarcodeThread(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                newbarcodeheaderService.clearNewbarcodeheader();
//                String lasttime=newbarcodeheaderService.lastTime();//获取标准表数据库最后的时间
//                //http://10.10.10.2:89/rent/getBarcodeHeader.jsp?deviceid=800&revisetime=2011-01-14 15:25:04.803
                String url_barcodeHeader=url+"rent/getBarcodeHeader.jsp?deviceid="+"&revisetime=";
                Log.i(TAG, "标准码文件url: "+url_barcodeHeader);

                Error.contentToTxt(context,"标准条码的请求url:"+url_barcodeHeader);
                String resultBarcode=RequestData.getResult(url_barcodeHeader);

                Log.i(TAG, "resultBarcode: "+ resultBarcode);
                String[] strArray = resultBarcode.split(String.valueOf(new char[]{(char) 129}));
                for (int i = 0; i < strArray.length; i++) {
//                        Log.i("数据库每一条完整的数据为", "结果: "+strArray[i].toString()+">>>>>>>>>>>"+i);
                    String res = strArray[i].toString();
                    String[] strArray2; //回车分割
                    //10001海信空调KFR-2826GW/BP 1P分体挂机1KB28901CNG1KB28901CNN12011-01-14 15:25:04.80323
                    strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
                    Log.i(TAG, " >>>>>>>>>>>>>>>" + strArray2.length);
                    if (strArray2.length == 9) {
                        if(strArray2[1].equals("长虹空调KFR-35GW/DHF(W1-H)+1 1.5P壁挂式")){
                            Log.e(TAG, "run0: "+strArray2[0]);
                            Log.e(TAG, "run1: "+strArray2[1]);
                            Log.e(TAG, "run2: "+strArray2[2]);
                            Log.e(TAG, "run3: "+strArray2[3]);
                            Log.e(TAG, "run4: "+strArray2[4]);
                            Log.e(TAG, "run5: "+strArray2[5]);
                            Log.e(TAG, "run6: "+strArray2[6]);
                            Log.e(TAG, "run7: "+strArray2[7]);
                            Log.e(TAG, "run8: "+strArray2[8]);
                        }


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

                        Log.i(TAG, "run:------------strArray2[a1]---------- "+strArray2[2].indexOf("|"));
                        Log.i(TAG, "run:------------strArray2[3]---------- "+strArray2[3].indexOf("|"));

                            Log.i(TAG, "标准内机单条: "+strArray2[2]);
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
                            Log.i(TAG, "标准外机单条: "+strArray2[3]);



                    }

                }
                Message msg=new Message();
                msg.what=2;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void getshiplist(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                shiplistservice.clearShipListData();
                String shiplist_url = url + "rent/rProxy.jsp?deviceid=" + "&s=" + Zip.compress("select * from getShiplistReady ");
                Log.i(TAG, "shiplist清单: "+shiplist_url);
                String result = RequestData.getResult(shiplist_url);
                Log.i(TAG, "result: "+result);
                if (result.length()<15){
                    result="数据请求失败，请重新请求";
                }else{
                    shiplistservice.clearShipListData();//清除shiplist数据库表中的数据
                    try{
                        JSONObject obj = new JSONObject(result);
                        JSONArray array = obj.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);
                            String storeid = data.has("storeid") ? data.getString("storeid") : "";//仓库id  214
                            Log.i(TAG, "storeid: "+storeid);
                            String shipdate = data.has("shipdate") ? data.getString("shipdate") : "";//发货日期  2116-01-25
                            Log.i(TAG, "shipdate: "+shipdate);
                            String seqno = data.has("seqno") ? data.getString("seqno") : "";//0
                            Log.i(TAG, "seqno: "+seqno);
                            String carid = data.has("carid") ? data.getString("carid") : "";    //0
                            String carname = data.has("carname") ? data.getString("carname") : "";//""
                            Log.i(TAG, "carname: "+carname);
                            String billtype = data.has("billtype") ? data.getString("billtype") : "";//入
                            String orderno = data.has("orderno") ? data.getString("orderno") : "";//发货编号
                            Log.i(TAG, "orderno: "+orderno);
                            String address_company = data.has("address_company") ? data.getString("address_company") : "";//地址
                            String goodsid = data.has("goodsid") ? data.getString("goodsid") : "";//产品编号33311
                            String goodsname = data.has("goodsname") ? data.getString("goodsname") : "";//产品名称
                            String innerno = data.has("innerno") ? data.getString("innerno") : "";//编码
                            String outerno = data.has("outerno") ? data.getString("outerno") : "";//外机编码
                            Log.i(TAG, "outerno: "+outerno);
                            String inoutFlag = data.has("inoutFlag") ? data.getString("inoutFlag") : "";//-1
                            String address = data.has("address") ? data.getString("address") : "";//地址
                            String gsm = data.has("gsm") ? data.getString("gsm") : "";//电话号码
                            String qty = data.has("qty") ? data.getString("qty") : "";//数量
                            Log.i(TAG, "qty: "+qty);
                            String stateId = data.has("stateId") ? data.getString("stateId") : "";//标识
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
                            if (goodsname.contains("空调")){
                                shiplist.setIn_qty(qty);
                                shiplist.setOut_qty(qty);
                            }else{
                                shiplist.setIn_qty(qty);
                                shiplist.setOut_qty("0");
                            }
                            shiplist.setCin_qty("0");
                            shiplist.setCout_qty("0");
                            shiplist.setIsfinish("完成");
                            shiplist.setIsStart("发车");
                            shiplist.setDistance("0.0公里");
                            shiplist.setStateId(stateId);
                            shiplistservice.addShipListData(shiplist);



                        }
                        Log.i(TAG, "数据请求成功");


                        result="数据请求成功";
                    }catch (Exception e){
                        Log.i(TAG, "错误: "+e.getMessage());
                    }
                }
                Message msg = new Message();
                msg.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }.start();

    }

    //网络请求仓库数据
    public void getStoresData() {
           new Thread(){
               @Override
               public void run() {
                   super.run();
                   String url_stores=url+"rent/rProxyDset.jsp?deviceid="+"&s="+ Zip.compress("select auto_id,storename,address,phoneno, manager from stores (nolock) where storetypeid=1 and is_use=1 order by storename");
                   Log.i(TAG, "仓库文件下载url: "+url_stores);
                   String returnStores=RequestData.getResult(url_stores);
                   storesservice.clearstores();
                   Log.i(TAG, "returnStores: "+returnStores);
                   String[]	 strArray = returnStores.split(String.valueOf(new char[]{(char) 129}));
                   for (int i = 0; i < strArray.length; i++) {
                       String res = strArray[i].toString();
                       String[] strArray2; //回车分割
                       strArray2 = res.split(String.valueOf(new char[]{(char) 128}));
                       Log.i(">>>>>>>>>>>>>>>>>>>>", " >>>>>>>>>>>>" + strArray2.length);
                       if (strArray2.length == 5) {
                           Stores stores = new Stores();
                           stores.setStoreId(strArray2[0]);
                           stores.setStorename(strArray2[1]);
                           stores.setAddress(strArray2[2]);
                           stores.setPhoneno(strArray2[3]);
                           stores.setManager(strArray2[4]);
                           storesservice.addStkcksum(stores);
                       }
                   }

                   Message msg=new Message();
                   msg.what=4;
                   handler.sendMessage(msg);


               }
           }.start();
    }

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.dismiss();
                    TransparentDialogUtil.showLoadingMessage(context,"标准表数据重置中。。。",true);//对话框显示
                    getBarcodeThread();
                    break;
                case 2:
                    TransparentDialogUtil.dismiss();
                    TransparentDialogUtil.showLoadingMessage(context,"发货清单表数据重置中。。。",true);//对话框显示
                    getshiplist();
                    break;
                case 3:
                    TransparentDialogUtil.dismiss();
                    TransparentDialogUtil.showLoadingMessage(context,"仓库表数据重置中。。。",true);//对话框显示
                    getStoresData();
                    break;
                case 4:
                    TransparentDialogUtil.dismiss();
                    break;
                case 5:
                    getNewBarcodeThread();
                    break;
            }
        }
    };

}
