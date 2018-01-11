package com.whycools.transport.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.Goods;
import com.whycools.transport.utils.GoodsList;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.whycools.transport.utils.SendData;

/**
 * 按区发货详情
 * Created by Zero on 2017-02-16.
 */

public class DeliveryByRegionDetailsActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout delivery_by_region_retails_layout_back;
    private TextView delivery_by_region_retails_tv_context;
    private List<Map<String, Object>> adapterlist = new ArrayList<Map<String, Object>>();
    private String district;
    private String shipdate;
    private ScanlistService scanlistService;
    private ShipListService shiplistservice;
    private Boolean isSan = false;//用来判断条码正确，false Toast输出条码不正确
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_region_retails);

    }

    @Override
    public void initViews() {
        delivery_by_region_retails_layout_back= findViewById(R.id.delivery_by_region_retails_layout_back);
        delivery_by_region_retails_tv_context= findViewById(R.id.delivery_by_region_retails_tv_context);

    }

    @Override
    public void initListeners() {
        delivery_by_region_retails_layout_back.setOnClickListener(this);

    }

    @Override
    public void initData() {

//        try {
            String scan= SharedPreferencesUtil.getData(DeliveryByRegionDetailsActivity.this,"scan","1").toString();
            if(scan.equals("1")){
                sm = new ScanDevice();
            }
            scanlistService=new ScanlistService(mContext);
            shiplistservice=new ShipListService(mContext);
            List<Goods> goodslist = new ArrayList<>();
            Intent intent=getIntent();
            district=intent.getStringExtra("district");
            shipdate=intent.getStringExtra("shipdate");
            Log.i(TAG, "shipdate: "+shipdate);
            goodslist = GoodsList.getGoodsListByDistrict(district,shipdate, DeliveryByRegionDetailsActivity.this);
            Log.i(TAG, "goodslist: "+goodslist.size());
            for (int i = 0; i < goodslist.size(); i++) {
                Log.i(TAG, "inoutFlag:<<<<<<<<<<<<<<<<<< "+goodslist.get(i).getInoutFlag());
                Log.i(TAG, "_id: "+goodslist.get(i).get_id());
                Log.i(TAG, "Innerno: " + goodslist.get(i).getBarcode_in());
                Log.i(TAG, "Outerno: " + goodslist.get(i).getBarcode_out());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("_id", goodslist.get(i).get_id());
                map.put("inoutFlag", goodslist.get(i).getInoutFlag());
                map.put("Goodsname", goodslist.get(i).getGoodsname());
                map.put("In_qty", goodslist.get(i).getInQty());
                Log.i(TAG, "内机个数getInQty: " + goodslist.get(i).getInQty());
                map.put("Out_qty", goodslist.get(i).getOutQty());
                Log.i(TAG, "外机个数getOutQty: " + goodslist.get(i).getOutQty());
                map.put("all", goodslist.get(i).getAllqty());
                Log.i(TAG, "总个数: "+goodslist.get(i).getAllqty());
                map.put("Innerno", goodslist.get(i).getBarcode_in());
                map.put("Outerno", goodslist.get(i).getBarcode_out());
                map.put("carId", goodslist.get(i).getCarId());
                Log.i(TAG, "carId: " + goodslist.get(i).getCarId());
                map.put("billno", goodslist.get(i).getBillNo());
                Log.i(TAG, "billno: " + goodslist.get(i).getBillNo());
                map.put("billtype", goodslist.get(i).getBilltype());
                Log.i(TAG, "billtype: " + goodslist.get(i).getBilltype());
                map.put("GoodsId", goodslist.get(i).getGoodsId());
                map.put("Goodsname", goodslist.get(i).getGoodsname());
                map.put("BarcodeLeftPos", goodslist.get(i).getBarcodeLeftPos());
                Log.i(TAG, "BarcodeLeftPos: " + goodslist.get(i).getBarcodeLeftPos());
                adapterlist.add(map);
            }
            showContext();//展示信息
//        }catch (Exception e){
//            Error.contentToTxt(Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" +  getserialnumber()+ DateUtils
//                    .date2String(new java.util.Date(),"yyyyMMdd") + ".log","按车发货详情页面数据加载异常"+e.getMessage());//异常写入文档
//
//        }


    }
    private void showContext() {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < adapterlist.size(); i++) {
            if (adapterlist.get(i).get("Goodsname").toString().contains("空调")) {
                buff.append(adapterlist.get(i).get("Goodsname") + "\r\n");
                buff.append("总共：" + adapterlist.get(i).get("all") + "台\r\n");
                buff.append("剩余内机：" + adapterlist.get(i).get("In_qty") + "台\r\n");
                buff.append("剩余外机：" + adapterlist.get(i).get("Out_qty") + "台\r\n");
            } else {
                buff.append(adapterlist.get(i).get("Goodsname") + "\r\n");
                buff.append("总共：" + adapterlist.get(i).get("all") + "台\r\n");
                buff.append("剩余台数为：" + adapterlist.get(i).get("In_qty") + "台\r\n");
            }


        }
        delivery_by_region_retails_tv_context.setText(buff);
    }

    /**
     * 条形码扫描
     */
    ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";//扫描标识用来启动广播
    private String barcodeStr;//条形码
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 如果获取到该广播。
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            barcodeStr = new String(barocode, 0, barocodelen);//获取条形码
//            Toast.makeText(DeliveryByCarDetailsActivity.this, ""+barcodeStr, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "条形码: " + barcodeStr);

//            barcodeStr="BS01U000N114338";//测试用的数据
//            Log.i(TAG, "测试barcodeStr: "+barcodeStr);
            if (scanlistService.findbarcodes(barcodeStr)){
                TransparentDialogUtil.showErrorMessage(DeliveryByRegionDetailsActivity.this,"该条码已经添加");
                stopMusic();
                shake();
            }else {
                for (int i = 0; i < adapterlist.size(); i++) {
                    if (adapterlist.get(i).get("Innerno").toString().length() <= barcodeStr.length()) {
                        //内机条形码对比
                        if ((!adapterlist.get(i).get("Innerno").toString().equals("")) && adapterlist.get(i).get("Innerno").toString().equals(barcodeStr.substring(Integer.valueOf(adapterlist.get(i).get("BarcodeLeftPos").toString()) - 1, Integer.valueOf(adapterlist.get(i).get("BarcodeLeftPos").toString()) - 1 + adapterlist.get(i).get("Innerno").toString().length()))) { /// goods  234516|123456
                            if (Integer.valueOf(adapterlist.get(i).get("In_qty").toString()) > 0) {//用来判断内机个数是否大于0
                                isSan = true;//用来判断有没有集合中有没有这个条码
                                int add_in = Integer.valueOf(adapterlist.get(i).get("In_qty").toString()) - 1;//内机个数减少一个
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("Goodsname", adapterlist.get(i).get("Goodsname"));
                                map.put("In_qty", String.valueOf(add_in));
                                Log.i(TAG, "扫描后的个数: " + add_in);
                                map.put("Out_qty", adapterlist.get(i).get("Out_qty"));
                                map.put("all", adapterlist.get(i).get("all"));
                                Log.i(TAG, "扫描后的总个数: " + adapterlist.get(i).get("all"));
                                map.put("Innerno", adapterlist.get(i).get("Innerno"));
                                map.put("Outerno", adapterlist.get(i).get("Outerno"));
                                map.put("billno", adapterlist.get(i).get("billno"));
                                map.put("BarcodeLeftPos", adapterlist.get(i).get("BarcodeLeftPos"));
                                map.put("billtype", adapterlist.get(i).get("billtype"));
                                map.put("GoodsId", adapterlist.get(i).get("GoodsId"));
                                map.put("Goodsname", adapterlist.get(i).get("Goodsname"));
                                map.put("carId", adapterlist.get(i).get("carId"));
                                map.put("_id", adapterlist.get(i).get("_id"));
                                map.put("inoutFlag", adapterlist.get(i).get("inoutFlag"));
                                adapterlist.set(i, map);
                                Toast.makeText(getApplicationContext(), "扫描成功", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "inoutFlag: "+adapterlist.get(i).get("inoutFlag").toString());
                                addScanlistService(barcodeStr,adapterlist.get(i).get("carId").toString(), "9574", adapterlist.get(i).get("billno").toString(), adapterlist.get(i).get("billtype").toString(), adapterlist.get(i).get("GoodsId").toString(), adapterlist.get(i).get("Goodsname").toString(), "1",adapterlist.get(i).get("inoutFlag").toString());
                                Log.i(TAG, "onReceive: "+shiplistservice.findshiplistCinqty(adapterlist.get(i).get("_id").toString()));
                                int cin=Integer.valueOf(shiplistservice.findshiplistCinqty(adapterlist.get(i).get("_id").toString()))+1;
                                Log.i(TAG, "cin: "+cin);
                                Log.i(TAG, "_id: "+adapterlist.get(i).get("_id").toString());
                                shiplistservice.upshiplistinqtydata(String.valueOf(cin), adapterlist.get(i).get("_id").toString());//修改清单数据库个数
                                Log.i(TAG, "onReceive: "+shiplistservice.findshiplistCinqty(adapterlist.get(i).get("_id").toString()));
//                                new SendData(mContext,barcodeStr);
                            } else {

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "该产品的数量扫描完了", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        }
                        //外机条形码对比
                        if ((!adapterlist.get(i).get("Outerno").toString().equals("")) && adapterlist.get(i).get("Outerno").toString().equals(barcodeStr.substring(Integer.valueOf(adapterlist.get(i).get("BarcodeLeftPos").toString()) - 1, Integer.valueOf(adapterlist.get(i).get("BarcodeLeftPos").toString()) - 1 + adapterlist.get(i).get("Outerno").toString().length()))) {
                            if (Integer.valueOf(adapterlist.get(i).get("Out_qty").toString()) > 0) {//用来判断外机个数是否大于0
                                isSan = true;//
                                Log.i(TAG, "外机: ");
                                int add_out = Integer.valueOf(adapterlist.get(i).get("Out_qty").toString()) - 1;
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("Goodsname", adapterlist.get(i).get("In_qty"));
                                map.put("In_qty", adapterlist.get(i).get("In_qty"));
                                map.put("Out_qty", String.valueOf(add_out));
                                map.put("all", adapterlist.get(i).get("all"));
                                map.put("Innerno", adapterlist.get(i).get("Innerno"));
                                map.put("Outerno", adapterlist.get(i).get("Outerno"));
                                map.put("billno", adapterlist.get(i).get("billno"));
                                map.put("billtype", adapterlist.get(i).get("billtype"));
                                map.put("GoodsId", adapterlist.get(i).get("GoodsId"));
                                map.put("Goodsname", adapterlist.get(i).get("Goodsname"));
                                map.put("carId", adapterlist.get(i).get("carId"));
                                map.put("_id", adapterlist.get(i).get("_id"));
                                map.put("inoutFlag", adapterlist.get(i).get("inoutFlag"));
                                adapterlist.set(i, map);
                                Toast.makeText(getApplicationContext(), "扫描成功", Toast.LENGTH_SHORT).show();
                                addScanlistService(barcodeStr, adapterlist.get(i).get("carId").toString(),"0", adapterlist.get(i).get("billno").toString(), adapterlist.get(i).get("billtype").toString(), adapterlist.get(i).get("GoodsId").toString(), adapterlist.get(i).get("Goodsname").toString(), "0",adapterlist.get(i).get("inoutFlag").toString());
                                shiplistservice.upshiplistoutqtydata(String.valueOf((Integer.valueOf(shiplistservice.findshiplistCoutqty(adapterlist.get(i).get("_id").toString()))+1)), adapterlist.get(i).get("_id").toString());//修改清单数据库个数
//                                new SendData(mContext,barcodeStr);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "该产品的数量扫描完了", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        }
                    }

                }
                //判断所扫描的条形码是否和数据库数据一样
                if (isSan == false) {
                    shake();
                    TransparentDialogUtil.showErrorMessage(DeliveryByRegionDetailsActivity.this,"条形码不对,请核对后再扫描");
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "条形码不对,请核对后在扫描", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();

                }

                showContext();//重新加载数据并显示
            }


        }
    };


    /**
     * 添加扫描后的条码数据
     */
    private void addScanlistService(String Barcodes,String carid, String StoreId, String billno, String billtype, String GoodsId, String Goodsname, String isin,String inoutFlag) {
        ScanRec scanlist = new ScanRec();

        TelephonyManager tm = (TelephonyManager) getSystemService(
                TELEPHONY_SERVICE);//获取设备信息

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        scanlist.setStoreId(StoreId);// 1
        if (tm.getDeviceId()!=null){
            scanlist.setShipId(tm.getDeviceId());
        }else{
            scanlist.setShipId("0000");
        }
        scanlist.setCarId(carid);
        Log.i(TAG, "添加carid: "+carid);
        scanlist.setUserId(SharedPreferencesUtil.getData(mContext,"userid","000").toString());
        scanlist.setCarname("");// 4车名称
        scanlist.setBillno(billno);// 5单据编号
        scanlist.setBilltype(billtype);// 6单据类别
        scanlist.setGoodsId(GoodsId);// 7产品id
        scanlist.setGoodsname(Goodsname);// 8产品名
        scanlist.setBar69("");// 9 69码
        scanlist.setBarcodes(Barcodes);// 10
        scanlist.setQty(String.valueOf(Integer.valueOf(inoutFlag)*1));// 11 数量
        scanlist.setTime(str);// 12 扫码时间
        scanlist.setIsInCode(isin);// 13
        scanlist.setGoodsno("车");
        scanlistService.addScanlist(scanlist);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delivery_by_region_retails_layout_back:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // 如果再活动不可见的时候，如果激光扫描还不为空的情况下，就将激光扫描关闭。
        if (sm != null) {
            sm.stopScan();
        }
        // 同时将广播撤销。
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 注册了一个动态广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }
}
