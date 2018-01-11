package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.adapter.DeliveryByCarDetailsAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.BarcodeHeader;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.BarcodeHeaderService;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.Error;
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
 * 按车发货详情
 * Created by Zero on 2017-03-08.
 */

public class DeliveryByCarDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout layout_deliveryByCarDetails_back;//返回按钮
    private TextView deliveryByCarDetails_tv_total_number;//内容
    private TextView deliveryByCarDetails_tv_total_number2;//内容
    private ListView delivery_by_car_details_lv_context;//内容
    private String carname;
    private String shipdate;
    private String storeId;
    private String intent_billtype;
    private ScanlistService scanlistService;
    private ShipListService shiplistservice;
    private BarcodeHeaderService barcodeHeaderService;

    private List<ShipList> shipListByCar = new ArrayList<>();
    private List<Map<String, Object>> listDataBaCar = new ArrayList<>();
    private DeliveryByCarDetailsAdapter deliveryByCarDetailsAdapter;
    private TextView deliveryByCarDetails_title;

    @Override
    public void setContentView() {

        try {
            setContentView(R.layout.activity_delivery_by_car_details);
        } catch (Exception e) {
            Error.contentToTxt(DeliveryByCarDetailsActivity.this, "按车发货详情页面启动异常" + e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initViews() {
        try {
            layout_deliveryByCarDetails_back = findViewById(R.id.layout_deliveryByCarDetails_back);
            deliveryByCarDetails_tv_total_number = findViewById(R.id.deliveryByCarDetails_tv_total_number);
            deliveryByCarDetails_tv_total_number2 = findViewById(R.id.deliveryByCarDetails_tv_total_number2);
            delivery_by_car_details_lv_context = findViewById(R.id.delivery_by_car_details_lv_context);
            deliveryByCarDetails_title = findViewById(R.id.deliveryByCarDetails_title);

        } catch (Exception e) {
            Error.contentToTxt(DeliveryByCarDetailsActivity.this, "按车发货详情页面控件实例化异常" + e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initListeners() {
        try {
            layout_deliveryByCarDetails_back.setOnClickListener(this);

            delivery_by_car_details_lv_context.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(DeliveryByCarDetailsActivity.this, DeliveryByCarDetailsSingleActivity.class);
                    intent.putExtra("_id", listDataBaCar.get(i).get("_id").toString());
                    intent.putExtra("index", i + 1 + "");
                    startActivity(intent);//goodsname

                }
            });
        } catch (Exception e) {
            Error.contentToTxt(DeliveryByCarDetailsActivity.this, "按车发货详情页面控件按钮监听异常" + e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initData() {

        try {
            String scan = SharedPreferencesUtil.getData(DeliveryByCarDetailsActivity.this, "scan", "1").toString();
            if (scan.equals("1")) {
                sm = new ScanDevice();
            }

            scanlistService = new ScanlistService(mContext);
            shiplistservice = new ShipListService(mContext);
            barcodeHeaderService = new BarcodeHeaderService(mContext);
            Intent intent = getIntent();
            carname = intent.getStringExtra("carname");
            shipdate = intent.getStringExtra("shipdate");
            storeId = intent.getStringExtra("storeId");
            intent_billtype = intent.getStringExtra("billtype");
            if (intent_billtype.equals("出")) {
                deliveryByCarDetails_title.setText("按车发货详情");
            } else {
                deliveryByCarDetails_title.setText("按车入库详情");
            }


//            Error.contentToTxt(DeliveryByCarDetailsActivity.this,"按车扫描车详情数据"+"_id→"+ goodslist.get(i).get_id()+"   inoutFlag→"+ goodslist.get(i).getInoutFlag()+"   Goodsname→"+ goodslist.get(i).getGoodsname()+"   In_qty→"+ goodslist.get(i).getInQty()+"   Out_qty→"+goodslist.get(i).getOutQty()+"   all→"+ goodslist.get(i).getAllqty()+"   Innerno→"+goodslist.get(i).getBarcode_in()+"   Outerno→"+ goodslist.get(i).getBarcode_out()+"   carId→"+ goodslist.get(i).getCarId()+"   billno→"+ goodslist.get(i).getBillNo()+"   billtype→"+ goodslist.get(i).getBilltype()+"   GoodsId→"+ goodslist.get(i).getGoodsId()+"   Goodsname→"+ goodslist.get(i).getGoodsname()+"   BarcodeLeftPos→"+ goodslist.get(i).getBarcodeLeftPos()+"   第"+i+"条");

            showContext(-1);//展示信息
        } catch (Exception e) {
            Error.contentToTxt(DeliveryByCarDetailsActivity.this, "按车发货详情页面数据加载异常" + e.getMessage());//异常写入文档

        }

    }

    private void showContext(int index) {
        Log.i(TAG, "重新获取数据: 扫描后加载");
        shipListByCar.clear();
        listDataBaCar.clear();
        Log.i(TAG, "重新获取数据: shipListByCar清除了" + shipListByCar.size());
        shipListByCar = shiplistservice.findByCarData(carname, shipdate, storeId, intent_billtype);
//        deliveryByCarDetails_tv_total_number.setText("共" + shipListByCar.size() + "条");
//        Log.i(TAG, "重新获取数据个数: " + shipListByCar.size());
        int barcodeNumber = 0;
        int barcodeNumber2 = 0;

        for (int i = 0; i < shipListByCar.size(); i++) {
            barcodeNumber = barcodeNumber + Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + Integer.valueOf(shipListByCar.get(i).getOut_qty().toString());
            barcodeNumber2 = barcodeNumber2 + Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + Integer.valueOf(shipListByCar.get(i).getOut_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCout_qty().toString());
            StringBuffer buff = new StringBuffer();
            buff.append("第" + (i + 1) + "条" + "\r\n");
            Log.i(TAG, "重新获取数据:getGoodsname " + shipListByCar.get(i).getGoodsname().toString());
            buff.append(shipListByCar.get(i).getGoodsname().toString() + "\r\n");
            if (shipListByCar.get(i).getGoodsname().toString().contains("空调") || shipListByCar.get(i).getGoodsname().toString().contains("三菱重工海尔风管机RFUTD50WDV 2P")) {
                Log.i(TAG, "重新获取数据:总数 " + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + Integer.valueOf(shipListByCar.get(i).getOut_qty().toString())));
                Log.i(TAG, "重新获取数据:内机 " + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString())));
                Log.i(TAG, "重新获取数据:外机 " + (Integer.valueOf(shipListByCar.get(i).getOut_qty().toString()) + Integer.valueOf(shipListByCar.get(i).getCout_qty().toString())));
                if (shipListByCar.get(i).getGoodsname().toString().contains("内机") || shipListByCar.get(i).getGoodsname().toString().contains("外机") || shipListByCar.get(i).getGoodsname().toString().contains("遥控器")) {
                    buff.append("总共：" + Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + "台\r\n");
                    buff.append("剩余台数为：" + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString())) + "台\r\n");
                } else {
                    buff.append("总共：" + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + Integer.valueOf(shipListByCar.get(i).getOut_qty().toString())) + "台\r\n");
                    buff.append("剩余内机：" + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString())) + "台\r\n");
                    buff.append("剩余外机：" + (Integer.valueOf(shipListByCar.get(i).getOut_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCout_qty().toString())) + "台\r\n");
                }

            } else {
                Log.i(TAG, "重新获取数据:总数 " + Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()));
                Log.i(TAG, "重新获取数据:内机 " + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString())));

                buff.append("总共：" + Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) + "台\r\n");
                buff.append("剩余台数为：" + (Integer.valueOf(shipListByCar.get(i).getIn_qty().toString()) - Integer.valueOf(shipListByCar.get(i).getCin_qty().toString())) + "台\r\n");
            }
            buff.append("-----------------------" + "\r\n");

            Map<String, Object> map = new HashMap<>();
            map.put("ByCarData", buff.toString());
            map.put("carname", carname);
            map.put("goodsname", shipListByCar.get(i).getGoodsname().toString());
            map.put("InoutFlag", shipListByCar.get(i).getInoutFlag().toString());
            map.put("CarId", shipListByCar.get(i).getCarId().toString());
            map.put("GoodsId", shipListByCar.get(i).getGoodsId().toString());
            map.put("Billno", shipListByCar.get(i).getBillno().toString());
            map.put("Billtype", shipListByCar.get(i).getBilltype().toString());
            map.put("_id", shipListByCar.get(i).get_id().toString());
            listDataBaCar.add(map);

        }
        deliveryByCarDetails_tv_total_number.setText("总共：" + barcodeNumber);
        deliveryByCarDetails_tv_total_number2.setText("剩余：" + barcodeNumber2);
        if (index > 0) {
            listDataBaCar.add(0, listDataBaCar.get(index));
            listDataBaCar.remove(index + 1);
        }

        deliveryByCarDetailsAdapter = new DeliveryByCarDetailsAdapter(DeliveryByCarDetailsActivity.this, listDataBaCar);
        delivery_by_car_details_lv_context.setAdapter(deliveryByCarDetailsAdapter);


    }
    List<ShipList>   scanlist;

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
//            int math=(int)((Math.random()*9+1)*100000);
//             barcodeStr="AB8XW0007"+"12345"+math;
            // barcodeStr="3900112345";


            Log.i(TAG, "测试barcodeStr: " + barcodeStr);
            if (scanlistService.findbarcodes(barcodeStr)) {
                TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "该条码已经添加");
                stopMusic();
                shake();
            } else {

                scanlist=findbarcode(barcodeStr, shipListByCar);
                if (scanlist.size()==1){

                    if (scanlist.get(0).getIsheaderin() == 1) {
                        if ((Integer.valueOf(scanlist.get(0).getIn_qty().toString()) - Integer.valueOf(scanlist.get(0).getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "该型号内机已经扫描完了");
                            stopMusic();
                            shake();
                            return;
                        } else {
                            int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(scanlist.get(0).get_id().toString())) + 1;
                            shiplistservice.upshiplistinqtydata(String.valueOf(cin), scanlist.get(0).get_id().toString());//修改清单数据库个数
                            addScanlistService(barcodeStr, scanlist.get(0).getCarId().toString(), scanlist.get(0).getStoreid(), scanlist.get(0).getBillno().toString(), scanlist.get(0).getBilltype().toString(), scanlist.get(0).getGoodsId().toString(), scanlist.get(0).getGoodsname().toString(), "1", scanlist.get(0).getInoutFlag().toString(),scanlist.get(0).get_id());
                            playMusic();
                            Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                            showContext(scanlist.get(0).getIsShowId());//重新加载数据并显示
                            return;
                        }
                    } else if (scanlist.get(0).getIsheaderin() == 0) {
                        if ((Integer.valueOf(scanlist.get(0).getOut_qty().toString()) - Integer.valueOf(scanlist.get(0).getCout_qty().toString())) <= 0) {

                            // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "该型号外机已经扫描完了");
                            stopMusic();
                            shake();
                            return;
                        } else {
                            int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty(scanlist.get(0).get_id().toString())) + 1;

                            shiplistservice.upshiplistoutqtydata(String.valueOf(cout), scanlist.get(0).get_id().toString());//修改清单数据库个数
                            addScanlistService(barcodeStr, scanlist.get(0).getCarId().toString(), scanlist.get(0).getStoreid(), scanlist.get(0).getBillno().toString(), scanlist.get(0).getBilltype().toString(), scanlist.get(0).getGoodsId().toString(), scanlist.get(0).getGoodsname().toString(), "0", scanlist.get(0).getInoutFlag().toString(),scanlist.get(0).get_id());

                            playMusic();
                            Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                            showContext(scanlist.get(0).getIsShowId());//重新加载数据并显示
                            return;

                        }

                    }

                }else  if(scanlist.size()>1){

                    handler.sendMessage(new Message());
                }else{
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "没有匹配到相应的条码");
                }


            }


        }
    };

    private List<ShipList> findbarcode(String strBarcode, List<ShipList> shipList) {
        List<ShipList> list=new ArrayList<>();
        for (int i = 0; i <shipList.size() ; i++) {

            String barcodeLeftpos = shipList.get(i).getBarcodeLeftpos();
            String minLen = shipList.get(i).getMinLen();
            String barcode_header_in = shipList.get(i).getBarcode_header_in();
            String barcode_header_out = shipList.get(i).getBarcode_header_out();
            if (barcode_header_in != null && barcode_header_in.length() > 0 && barcode_header_in.indexOf("|") > 0) {
                String[] str_in = barcode_header_in.split("\\|");
                for (int a = 0; a < str_in.length; a++) {
                    if (strBarcode.length() >= str_in[a].length() && strBarcode.length() >= Integer.valueOf(minLen) && str_in[a].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                            Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_in[a].length())))) {
                        System.out.println("----内机匹配正确");
                        shipList.get(i).setIsheaderin(1);
                        shipList.get(i).setIsShowId(i);
                        list.add(shipList.get(i));
                    }
                }

            }
            if (barcode_header_out != null && barcode_header_out.length() > 0 && barcode_header_out.indexOf("|") > 0) {

                String[] str_out = barcode_header_out.split("\\|");
                Log.i(TAG, "-----------: " + str_out.length);
                for (int b = 0; b < str_out.length; b++) {
                    Log.i(TAG, "---------------- " + str_out[b]);

                    Log.i(TAG, "---------------- " + strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                            Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())));
                    if (strBarcode.length() >= str_out[b].length() && strBarcode.length() >= Integer.valueOf(minLen) && str_out[b].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                            Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())))) {
                        System.out.println("-----外机匹配正确");
                        shipList.get(i).setIsheaderin(0);
                        shipList.get(i).setIsShowId(i);
                        list.add(shipList.get(i));
                    }
                }
            }
            if (barcode_header_in != null && barcode_header_in.length() > 0 && strBarcode.length() >= barcode_header_in.length() && strBarcode.length() >= Integer.valueOf(minLen) && barcode_header_in.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                    Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_in.length())))) {

                System.out.println("内机匹配正确");
                shipList.get(i).setIsheaderin(1);
                shipList.get(i).setIsShowId(i);
                list.add(shipList.get(i));
            }
            if (barcode_header_out != null && barcode_header_out.length() > 0 && strBarcode.length() >= barcode_header_out.length() && strBarcode.length() >= Integer.valueOf(minLen) && barcode_header_out.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                    Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_out.length())))) {
                System.out.println("外机匹配成功");
                shipList.get(i).setIsheaderin(0);
                shipList.get(i).setIsShowId(i);
                list.add(shipList.get(i));
            }


        }
        return list;


    }


    /**
     * 添加扫描后的条码数据
     */
    private void addScanlistService(String Barcodes, String carid, String StoreId, String billno, String billtype, String GoodsId, String Goodsname, String isin, String inoutFlag,String shipid) {
        ScanRec scanlist = new ScanRec();

        TelephonyManager tm = (TelephonyManager) getSystemService(
                TELEPHONY_SERVICE);//获取设备信息

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        Log.i(TAG, "仓库id: " + StoreId);
        scanlist.setStoreId(StoreId);// 1
        if (tm.getDeviceId() != null) {
            scanlist.setShipId(tm.getDeviceId());
        } else {
            scanlist.setShipId("0000");
        }
        scanlist.setShipId(shipid);
        scanlist.setCarId(carid);
        Log.i(TAG, "添加carid: " + carid);
        scanlist.setUserId(SharedPreferencesUtil.getData(mContext, "userid", "000").toString());
        scanlist.setCarname(carname);// 4车名称
        scanlist.setBillno(billno);// 5单据编号
        scanlist.setBilltype(billtype);// 6单据类别
        scanlist.setGoodsId(GoodsId);// 7产品id
        scanlist.setGoodsname(Goodsname);// 8产品名
        scanlist.setBar69("");// 9 69码
        scanlist.setBarcodes(Barcodes);// 10
        scanlist.setQty(String.valueOf(Integer.valueOf(inoutFlag) * 1));// 11 数量
        scanlist.setTime(str);// 12 扫码时间
        scanlist.setIsInCode(isin);// 13
        scanlist.setGoodsno("车");
        Error.contentToTxt(DeliveryByCarDetailsActivity.this, scanlist.getStoreId() + ">>>>>>" +//1
                scanlist.getShipId() + ">>>>>>" +//2
                scanlist.getCarId() + ">>>>>>" +//3
                scanlist.getUserId() + ">>>>>>" +
                scanlist.getCarname() + ">>>>>>" +//4
                scanlist.getBilltype() + ">>>>>>" +//5
                scanlist.getBillno() + ">>>>>>" + //6
                scanlist.getGoodsId() + ">>>>>>" +//7
                scanlist.getGoodsname() + ">>>>>>" + //8
                scanlist.getBar69() + ">>>>>>" +//9
                scanlist.getBarcodes() + ">>>>>>" +//10
                scanlist.getQty() + ">>>>>>" +//11
                scanlist.getTime() + ">>>>>>" +//12
                scanlist.getIsInCode() + ">>>>>>" +//13
                scanlist.getGoodsno());
        scanlistService.addScanlist(scanlist);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_deliveryByCarDetails_back:
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
        showContext(-1);//展示信息
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final String[] cities = new String[scanlist.size()];
            for (int i = 0; i < scanlist.size(); i++) {
                cities[i]= scanlist.get(i).getGoodsname();
                Log.i(TAG, "------------: "+scanlist.get(i).getGoodsname());
            }
            //   Toast.makeText(ScanOnceActivity.this, "大于1个数据", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryByCarDetailsActivity.this);
            //  builder.setIcon(R.drawable.transparent_info);
            builder.setTitle("当前匹配大于2个，请选择正确的品名");
            //    指定下拉列表的显示数据


            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface,  int i) {

                    if (scanlist.get(i).getIsheaderin() == 1) {
                        if ((Integer.valueOf(scanlist.get(i).getIn_qty().toString()) - Integer.valueOf(scanlist.get(i).getCin_qty().toString())) <= 0) {
//            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "该型号内机已经扫描完了");
                            stopMusic();
                            shake();
                            return;
                        } else {
                            int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(scanlist.get(i).get_id().toString())) + 1;
                            shiplistservice.upshiplistinqtydata(String.valueOf(cin), scanlist.get(i).get_id().toString());//修改清单数据库个数
                            addScanlistService(barcodeStr, scanlist.get(i).getCarId().toString(), scanlist.get(i).getStoreid(), scanlist.get(i).getBillno().toString(), scanlist.get(i).getBilltype().toString(), scanlist.get(i).getGoodsId().toString(), scanlist.get(i).getGoodsname().toString(), "1", scanlist.get(i).getInoutFlag().toString(),scanlist.get(i).get_id());
                            playMusic();
                            Toast.makeText(DeliveryByCarDetailsActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                            showContext(scanlist.get(i).getIsShowId());//重新加载数据并显示
                            return;
                        }
                    } else if (scanlist.get(i).getIsheaderin() == 0) {
                        if ((Integer.valueOf(scanlist.get(i).getOut_qty().toString()) - Integer.valueOf(scanlist.get(i).getCout_qty().toString())) <= 0) {

                            // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsActivity.this, "该型号外机已经扫描完了");
                            stopMusic();
                            shake();
                            return;
                        } else {
                            int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty(scanlist.get(i).get_id().toString())) + 1;

                            shiplistservice.upshiplistoutqtydata(String.valueOf(cout), scanlist.get(i).get_id().toString());//修改清单数据库个数
                            addScanlistService(barcodeStr, scanlist.get(i).getCarId().toString(), scanlist.get(i).getStoreid(), scanlist.get(i).getBillno().toString(), scanlist.get(i).getBilltype().toString(), scanlist.get(i).getGoodsId().toString(), scanlist.get(i).getGoodsname().toString(), "0", scanlist.get(i).getInoutFlag().toString(),scanlist.get(i).get_id());

                            playMusic();
                            Toast.makeText(DeliveryByCarDetailsActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                            showContext(scanlist.get(i).getIsShowId());//重新加载数据并显示
                            return;

                        }

                    }
                }
            });
            builder.show();
        }
    };
}
