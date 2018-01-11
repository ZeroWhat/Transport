package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.whycools.transport.R;
import com.whycools.transport.adapter.ScanOnceAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.BarcodeHeader;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.BarcodeHeaderService;
import com.whycools.transport.service.NewbarcodeheaderService;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//import com.whycools.transport.utils.SendData;

/**
 * 扫一次保存一次
 * Created by Zero on 2016-12-08.
 */

public class ScanOnceActivity extends BaseActivity implements View.OnClickListener {
    private String billno;//编号
    private String stores;//仓库
    private String storesid;//仓库id
    private TextView scanonce_tv_context;//编号
    private ListView scanonce_lv;//listview
    private Button scanonce_bt_manually_add;//手动添加条形码
    private ScanlistService scanlistservice;//扫描数据库
    private LinearLayout scanonce_back;//返回按钮
    private ScanOnceAdapter adapter;//适配器
    private List<ScanRec> list = new ArrayList<>();//扫描数据集合
    private int in_or_out;
    private CustomDialog dialog;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_scanonce);
    }

    @Override
    public void initViews() {
        scanonce_tv_context = findViewById(R.id.scanonce_tv_context);
        scanonce_lv = findViewById(R.id.scanonce_lv);
        scanonce_back = findViewById(R.id.scanonce_back);
        scanonce_bt_manually_add = findViewById(R.id.scanonce_bt_manually_add);
    }

    @Override
    public void initListeners() {
        scanonce_back.setOnClickListener(this);
        scanonce_bt_manually_add.setOnClickListener(this);
        scanonce_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("你确定要删除该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        scanlistservice.deleteScanlist(list.get(i).getBarcodes());
                        loadListViewScanlist();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    mydialog.dismiss();
                    final String[] cities = new String[result_list.size()];
                    for (int i = 0; i < result_list.size(); i++) {
                        cities[i] = result_list.get(i).getGoodsname();
                        Log.i(TAG, "------------: " + result_list.get(i).getGoodsname());
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanOnceActivity.this);

                    builder.setTitle("当前匹配" + result_list.size() + "个，请选择正确的品名");
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            addScanlistService(barcodeStr, result_list.get(i).getGoodsId(), result_list.get(i).getGoodsname(), result_list.get(i).getIsin());
                            loadListViewScanlist();
                        }
                    });
                    builder.show();
                    break;
                case 7:
                    mydialog.setProgress(msg.getData().getInt("index"));
                    break;
                case 8:
                    mydialog.dismiss();
                    stopMusic();
                    shake();
                    TransparentDialogUtil.showErrorMessage(ScanOnceActivity.this, "没有匹配到该条码");
                    break;
                case 9:
                    mydialog.dismiss();
                    playMusic();
                    addScanlistService(barcodeStr, result_list.get(0).getGoodsId(), result_list.get(0).getGoodsname(), result_list.get(0).getIsin());
                    loadListViewScanlist();
                    break;
                case 10:
                    showDialog();
                    break;


            }
        }
    };


    @Override
    public void initData() {
        String scan = SharedPreferencesUtil.getData(ScanOnceActivity.this, "scan", "1").toString();
        if (scan.equals("1")) {
            sm = new ScanDevice();
        }
        Intent intent = getIntent();
        billno = intent.getStringExtra("billno");
        in_or_out = Integer.valueOf(intent.getStringExtra("in_or_out"));
        stores = intent.getStringExtra("stores");
        storesid = stores.substring(stores.indexOf("→") + 1, stores.length());
        Log.i("------------", "storesid-----------: " + storesid);
        if (in_or_out == 1) {
            scanonce_tv_context.setText("当前扫描编号：" + billno + "\r\n" + "当前仓库选择：" + stores + "\r\n" + "当前扫描类别：出");
        } else {
            scanonce_tv_context.setText("当前扫描编号：" + billno + "\r\n" + "当前仓库选择：" + stores + "\r\n" + "当前扫描类别：入");
        }
        scanlistservice = new ScanlistService(mContext);
        loadListViewScanlist();
    }

    private List<BarcodeHeader> result_list;
    /**
     * 条形码扫描
     */
    private ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private String barcodeStr;
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 如果获取到该广播。
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
            Log.i(TAG, "barcodeStr: " + barcodeStr);
            addBarcodeStr(barcodeStr);
            sm.stopScan();
        }
    };

    private void addBarcodeStr(final String barcode) {
        if (scanlistservice.findbarcodes(barcode)) {
            TransparentDialogUtil.showErrorMessage(mContext, "该条码已经扫描");
            shake();
            return;
        }
        showDialog();
        new Thread() {
            @Override
            public void run() {
                super.run();
                result_list = findbarcode(barcode);
                if (result_list.size() == 0) {
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);
                } else if (result_list.size() == 1) {
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 6;
                    handler.sendMessage(msg);
                }

            }
        }.start();

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

    /**
     * 添加扫描后的条码数据
     */
    private void addScanlistService(String Barcodes, String goodsId, String Goodsname, String isin) {
        ScanRec scanlist = new ScanRec();
        TelephonyManager tm = (TelephonyManager) getSystemService(NoFinishShipListDetailsActivity.TELEPHONY_SERVICE);//获取设备信息
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String date = formatter.format(curDate);
        scanlist.setStoreId(storesid);// 仓库id
        Log.i(TAG, "仓库id为: " + storesid);
        //如果设备序列号为null，则设备序列号为0000
        if (tm.getDeviceId() != null && !tm.getDeviceId().equals("0")) {
            Log.i(TAG, "枪id为: " + tm.getDeviceId());
            scanlist.setShipId(tm.getDeviceId());
        } else {
            scanlist.setShipId("ae01");
        }
        scanlist.setCarId("0");//车id
        scanlist.setUserId(SharedPreferencesUtil.getData(mContext, "userid", "0").toString());
        scanlist.setCarname("0");//车名称
        scanlist.setBillno(billno);//编号

        scanlist.setGoodsId(goodsId);// 产品id
        scanlist.setGoodsname(Goodsname);//产品名称
        scanlist.setBar69("0");//69码
        scanlist.setBarcodes(Barcodes);// 条形码
        if (in_or_out == 1) {
            scanlist.setQty("-1");//数量
            scanlist.setBilltype("出");//类别
        } else {
            scanlist.setQty("1");//数量
            scanlist.setBilltype("入");//类别
        }
        scanlist.setTime(date);//扫码时间
        scanlist.setIsInCode(isin);//是否为内机
        scanlist.setGoodsno("自由");//
        Error.contentToTxt(ScanOnceActivity.this, "扫一次保存一次上传数据" + scanlist.getStoreId() + "    " +//1
                scanlist.getShipId() + "    " +//a1
                scanlist.getCarId() + "    " +//3
                scanlist.getUserId() + "    " +
                scanlist.getCarname() + "    " +//4
                scanlist.getBilltype() + "    " +//5
                scanlist.getBillno() + "    " +//6
                scanlist.getGoodsId() + "    " +//7
                scanlist.getGoodsname() + "    " +//8
                scanlist.getBar69() + "    " +//9
                scanlist.getBarcodes() + "    " +//10
                scanlist.getQty() + "    " +//11
                scanlist.getTime() + "    " +//12
                scanlist.getIsInCode() + "    " +//13
                scanlist.getGoodsno());
        scanlistservice.addScanlist(scanlist);
    }

    /**
     * 加载ListView
     */
    private void loadListViewScanlist() {
        // 从数据库读取listview数据
        list.clear();
        list = scanlistservice.getAllScanlistListsanone();
        List<ScanRec> list2 = new ArrayList<>();
        list2.clear();
        if (list.size() > 5) {

            for (int i = 0; i < 5; i++) {
                list2.add(list.get(i));
            }
        } else {
            list2 = list;
        }
        adapter = new ScanOnceAdapter(mContext, list2);
        scanonce_lv.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanonce_back:
                finish();
                break;
            case R.id.scanonce_bt_manually_add:
                dialog = new CustomDialog(ScanOnceActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().length() > 0) {
                            addBarcodeStr(editText.getText().toString());
                            dialog.dismiss();

                        } else {
                            Toast.makeText(ScanOnceActivity.this, "条形码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

        }
    }

    private List<BarcodeHeader> findbarcode(String strBarcode) {


        List<BarcodeHeader> list = new ArrayList<>();
        BarcodeHeaderService barcodeHeaderService = new BarcodeHeaderService(ScanOnceActivity.this);
        List<BarcodeHeader> barlist = barcodeHeaderService.getAllBarcodeHeaderList();
        mydialog.setMaxProgress(barlist.size());
        for (int i = 0; i < barlist.size(); i++) {
            String barcodeLeftpos = barlist.get(i).getBarcodeLeftpos();
            String minLen = barlist.get(i).getMinLen();
            String barcode_header_in = barlist.get(i).getBarcode_header_in();
            String barcode_header_out = barlist.get(i).getBarcode_header_out();
            if (barcode_header_in != null && barcode_header_in.length() > 0 && barcode_header_in.indexOf("|") > 0) {
                String[] str_in = barcode_header_in.split("\\|");
                for (int a = 0; a < str_in.length; a++) {
                    if (strBarcode.length() >= str_in[a].length() && strBarcode.length() >= Integer.valueOf(minLen) && (Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_in[a].length())) <= strBarcode.length() && str_in[a].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                            Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_in[a].length())))) {
                        System.out.println("----内机匹配正确");
                        barlist.get(i).setIsin("1");
                        list.add(barlist.get(i));
                    }
                }

            }
            if (barcode_header_out != null && barcode_header_out.length() > 0 && barcode_header_out.indexOf("|") > 0) {

                String[] str_out = barcode_header_out.split("\\|");
                Log.i(TAG, "-----------: " + str_out.length);
                for (int b = 0; b < str_out.length; b++) {
                    if (strBarcode.length() >= str_out[b].length() && strBarcode.length() >= Integer.valueOf(minLen) && !barcodeLeftpos.equals("null") && (Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())) <= strBarcode.length() && str_out[b].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                            Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())))) {
                        System.out.println("-----外机匹配正确");
                        barlist.get(i).setIsin("0");
                        list.add(barlist.get(i));
                    }
                }
            }
            if (barcode_header_in != null && barcode_header_in.length() > 0 && strBarcode.length() >= barcode_header_in.length() && strBarcode.length() >= Integer.valueOf(minLen) && !barcodeLeftpos.equals("null") && (Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_in.length())) <= strBarcode.length() && barcode_header_in.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                    Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_in.length())))) {

                System.out.println("内机匹配正确");
                barlist.get(i).setIsin("1");
                list.add(barlist.get(i));
            }
            Log.i(TAG, "barcodeLeftpos: " + barcodeLeftpos);
            Log.i(TAG, "------------长度: " + (Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_out.length())));
            if (barcode_header_out != null && barcode_header_out.length() > 0 && strBarcode.length() >= barcode_header_out.length() && strBarcode.length() >= Integer.valueOf(minLen) && (Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_out.length())) <= strBarcode.length() && barcode_header_out.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                    Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_out.length())))) {
                System.out.println("外机匹配成功");
                barlist.get(i).setIsin("0");
                list.add(barlist.get(i));
            }
            sendMsg(i);


        }
        Log.i(TAG, "匹配成功后返回的个数为: " + list.size());
        return list;


    }

    private MaterialDialog mydialog;

    public void showDialog() {
        mydialog = new MaterialDialog.Builder(ScanOnceActivity.this)
                .title("数据匹配中")
                .content("正在匹配中，请稍后...")
                .progress(false, 100, true)
                .show();
        mydialog.setCanceledOnTouchOutside(false);

    }

    private void sendMsg(int index) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        msg.setData(bundle);
        msg.what = 7;
        handler.sendMessage(msg);
    }
}
