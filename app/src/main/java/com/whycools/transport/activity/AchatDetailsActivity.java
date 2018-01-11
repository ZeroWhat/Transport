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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.adapter.NoFinishShipListDetailsAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.BarcodeHeaderService;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.Goods;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;
import com.whycools.transport.view.MyListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import com.whycools.transport.utils.SendData;

/**
 * 采购单详情
 * Created by Zero on 2016-12-06.
 */
public class AchatDetailsActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout achat_details_layout_back;//返回按钮
    private TextView achat_details_tv_billno;//订单编号
    private TextView achat_details_tv_address;//地址
    private TextView achat_details_tv_phonenumber;//电话号码
    private Button achat_details_bt_gohere;//
    private Button achat_details_bt_call;
    private TextView achat_details_tv_goodsid;//产品id
    private TextView achat_details_tv_goodsname;//产品名称
    private TextView achat_details_tv_total;//总个数
    private TextView achat_details_tv_surplus;//剩余个数
    private TextView achat_details_tv_in;//内机个数
    private TextView achat_details_tv_out;//外机个数
    private MyListView achat_details_lv;
    private LinearLayout achat_details_layout_in;//内机布局
    private LinearLayout achat_details_layout_out;//外机布局
    private Button achat_details_bt_manually_add;//手动添加按钮
    private ShipListService shiplistservice;//清单
    private ScanlistService scanlistservice;//扫描
    private NoFinishShipListDetailsAdapter adapter;//适配器
   private   List<ScanRec>  list2=new ArrayList<>();
    private ShipList shipList;
    private CustomDialog dialog;

    @Override
    public void setContentView() {

            setContentView(R.layout.activity_achat_details);



    }

    @Override
    public void initViews() {


            achat_details_layout_back= findViewById(R.id.achat_details_layout_back);
            achat_details_tv_billno= findViewById(R.id.achat_details_tv_billno);
            achat_details_tv_address= findViewById(R.id.achat_details_tv_address);
            achat_details_tv_phonenumber= findViewById(R.id.achat_details_tv_phonenumber);
            achat_details_bt_gohere= findViewById(R.id.achat_details_bt_gohere);
            achat_details_bt_call= findViewById(R.id.achat_details_bt_call);
            achat_details_tv_goodsid= findViewById(R.id.achat_details_tv_goodsid);
            achat_details_tv_goodsname= findViewById(R.id.achat_details_tv_goodsname);
            achat_details_tv_total= findViewById(R.id.achat_details_tv_total);
            achat_details_tv_surplus= findViewById(R.id.achat_details_tv_surplus);
            achat_details_tv_in= findViewById(R.id.achat_details_tv_in);
            achat_details_tv_out= findViewById(R.id.achat_details_tv_out);
            achat_details_layout_in= findViewById(R.id.achat_details_layout_in);
            achat_details_layout_out= findViewById(R.id.achat_details_layout_out);
            achat_details_bt_manually_add= findViewById(R.id.achat_details_bt_manually_add);

            achat_details_lv= findViewById(R.id.achat_details_lv);


    }

    @Override
    public void initListeners() {

            achat_details_layout_back.setOnClickListener(this);
            achat_details_bt_manually_add.setOnClickListener(this);
            achat_details_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    dialog = new CustomDialog(AchatDetailsActivity.this);
                    final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                    editText.setText(list2.get(i).getBarcodes());
                    editText.setSelection(editText.getText().length());
                    final RadioButton rbin = (RadioButton) dialog.getrbin();//方法在CustomDialog中实现
                    dialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editText.getText().length()>0){
                                Log.i(TAG, "onClick: "+editText.getText().toString());
                                scanlistservice.updateScanlistBarcodes(editText.getText().toString(),list2.get(i).get_id());
                                Log.i(TAG, "onClick:id >>>>>>>>>>>>>>>>>"+list2.get(i).get_id());
                                loadListViewScanlist();
                                dialog.dismiss();
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
                }
            });
            achat_details_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("你确定要删除该条码吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String isInCode=scanlistservice.findisInCode(list2.get(i).getBarcodes());
                            scanlistservice.deleteScanlist(list2.get(i).getBarcodes());
                            if(isInCode.equals("1")){
                                int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(shipList.get_id()) )- 1;

                                shiplistservice.upshiplistinqtydata(String.valueOf(cin), shipList.get_id());//修改清单数据库个数
                            }else {
                                int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty( shipList.get_id()) )- 1;
                                shiplistservice.upshiplistoutqtydata(String.valueOf(cout),  shipList.get_id());//修改清单数据库个数
                            }
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
                    return false;
                }
            });


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    final String shipid=msg.getData().getString("shipid");
                    final String barcodes=msg.getData().getString("barcodes");
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString()+"rent/rProxyDsetExec.jsp?deviceid=&s="+ Zip.compress("delBarcode '"+shipid+"','"+barcodes+"'");
                            Log.i(TAG, "压缩字符串原始数据为: "+"delBarcode '"+shipid+"','"+barcodes+"'");
                            Log.i(TAG, "撤销的url: "+url);
                            String result= RequestData.HttpGet(url);
                            Message msg=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putString("result",result);
                            bundle.putString("barcodes",barcodes);
                            msg.setData(bundle);
                            msg.what=2;
                            handler.sendMessage(msg);
                            Log.i(TAG, "撤销返回的结果为: "+result);
                        }
                    }.start();

                    break;
                case 2:
                    String result=msg.getData().getString("result");
                    Log.i(TAG, "撤销返回的结果为: "+result);
                    if (isNumeric(result)&&Integer.valueOf(result)>0){
                        String barcodes2=msg.getData().getString("barcodes");
                        scanlistservice.deleteScanlist(barcodes2);
                        loadListViewScanlist();
                        Toast.makeText(mContext, "撤销成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "撤销失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 3:
                    String barcodeStr=msg.getData().getString("barcodeStr");
                    String isinorout=msg.getData().getString("isin");
                    addScanlistService(barcodeStr, isinorout);
//                    new SendData(mContext, barcodeStr);
                    loadListViewScanlist();
                    break;

                case 5:
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this,"该条码已扫过");
                    shake();
                    break;
                case 6:
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                    TransparentDialogUtil.showSuccessMessage(AchatDetailsActivity.this,"扫描成功");
                    loadListViewScanlist();
                    adapter.notifyDataSetChanged();
                    //mSoundPool.play(soundPoolMap.get(KEY_SOUND_A1), 1, 1, 0, 0, 1);
                    playMusic();
                    break;
                case 7:
                    shake();
                    TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this,"内机扫描完了");
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "内机扫描完了", Toast.LENGTH_SHORT).show();
                   // mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);
                    stopMusic();
                    break;
                case 8:
                    shake();
                    TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this,"外机机扫描完了");
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "外机机扫描完了", Toast.LENGTH_SHORT).show();
                  // mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);
                    stopMusic();
                    break;
                case 9:
                    shake();
                    TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this,"没有匹配到相应的条码");
                   // mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);
                    stopMusic();
                    break;
            }
        }
    };


    @Override
    public void initData() {


            String scan= SharedPreferencesUtil.getData(AchatDetailsActivity.this,"scan","1").toString();
            if(scan.equals("1")){
                sm = new ScanDevice();
            }
            loadListViewScanlist();



    }

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
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
//            int math=(int)((Math.random()*9+1)*100000);
//            barcodeStr="B70U2009E"+"12364"+math;
          //  barcodeStr="F70JC100000M4H3X0025";
            Log.i(TAG, "barcodeStr: "+barcodeStr);
            if (scanlistservice.findbarcodes(barcodeStr)){
                TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该条码已经添加");
                stopMusic();
                shake();
            }else{
                int backMake = findbarcode(barcodeStr, shipList);
                if (backMake == 1) {
                    if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该型号内机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(shipList.get_id().toString())) + 1;
                        shiplistservice.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "1");
                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        loadListViewScanlist();//重新加载数据并显示

                    }
                } else if (backMake == 0) {
                    if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                        // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该型号外机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                        shiplistservice.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "0");

                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        loadListViewScanlist();//重新加载数据并显示


                    }

                }else {
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "没有匹配到相应的条码");
                }
            }

            sm.stopScan();

        }
    };

    private int findbarcode(String strBarcode, ShipList shipList) {
        String barcodeLeftpos = shipList.getBarcodeLeftpos();
        String minLen = shipList.getMinLen();
        String barcode_header_in = shipList.getBarcode_header_in();
        String barcode_header_out = shipList.getBarcode_header_out();
        Log.i(TAG, "barcode_header_in: "+barcode_header_in);
        Log.i(TAG, "barcode_header_out: "+barcode_header_out);
        Log.i(TAG, "barcodeLeftpos: "+barcodeLeftpos);
        Log.i(TAG, "minLen: "+minLen);
        if (barcode_header_in != null &&barcode_header_in.length()>0&& barcode_header_in.indexOf("|") > 0) {
            String[] str_in = barcode_header_in.split("\\|");
            for (int a = 0; a < str_in.length; a++) {
                if (strBarcode.length() >= str_in[a].length() && strBarcode.length() >= Integer.valueOf(minLen) && str_in[a].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                        Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_in[a].length())))) {
                    System.out.println("----内机匹配正确");
                    return 1;

                }


            }

        }
        if (barcode_header_out != null && barcode_header_out.length()>0&& barcode_header_out.indexOf("|") > 0) {

            String[] str_out = barcode_header_out.split("\\|");
            Log.i(TAG, "-----------: "+str_out.length);
            for (int b = 0; b < str_out.length; b++) {
                Log.i(TAG, "---------------- "+str_out[b]);

                Log.i(TAG, "---------------- "+strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                        Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())));
                if (strBarcode.length() >= str_out[b].length() && strBarcode.length() >= Integer.valueOf(minLen) && str_out[b].equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                        Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(str_out[b].length())))) {
                    System.out.println("-----外机匹配正确");
                    return 0;

                }
            }
        }
        if (barcode_header_in != null && barcode_header_in.length()>0&& strBarcode.length() >= barcode_header_in.length() && strBarcode.length() >= Integer.valueOf(minLen) && barcode_header_in.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_in.length())))) {

            System.out.println("内机匹配正确");
            return 1;
        }
        if (barcode_header_out != null && barcode_header_out.length()>0 && strBarcode.length() >= barcode_header_out.length() && strBarcode.length() >= Integer.valueOf(minLen) && barcode_header_out.equals(strBarcode.substring(Integer.valueOf(barcodeLeftpos) - 1,
                Integer.valueOf(barcodeLeftpos) - 1 + Integer.valueOf(barcode_header_out.length())))) {
            System.out.println("外机匹配成功");
            return 0;

        }
        System.out.println("不正确");
        return 2;

    }



    /**
     * 添加扫描后的条码数据
     */
    private void addScanlistService(String barcodes,String IsInCode ) {
        ScanRec scanlist = new ScanRec();
        TelephonyManager tm = (TelephonyManager) getSystemService(NoFinishShipListDetailsActivity.TELEPHONY_SERVICE);//获取设备信息
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前扫描的时间
        String date = formatter.format(curDate);
        scanlist.setStoreId(shipList.getStoreid());// 1
        if (tm.getDeviceId()!=null){
            scanlist.setShipId(tm.getDeviceId());
        }else{
            scanlist.setShipId("0000");
        }
        scanlist.setCarId(shipList.getCarId());// 3车次号
        scanlist.setUserId(SharedPreferencesUtil.getData(mContext,"userid","000").toString());
        scanlist.setBillno(shipList.getBillno());// 5单据编号
        scanlist.setBilltype(shipList.getBilltype());// 6单据类别
        scanlist.setGoodsId(shipList.getGoodsId());// 7产品id
        scanlist.setGoodsname(shipList.getGoodsname());// 8产品名
        scanlist.setBarcodes(barcodes);// 10
        scanlist.setQty(String.valueOf(Integer.valueOf(shipList.getInoutFlag())*1));// 11 数量
        scanlist.setTime(date);// 12 扫码时间
        scanlist.setIsInCode(IsInCode);// 13
        scanlist.setGoodsno("采");
        scanlistservice.addScanlist(scanlist);

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
        finish();
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
     * 加载ListView
     */
    private void loadListViewScanlist() {
        shiplistservice=new ShipListService(mContext);
        scanlistservice=new ScanlistService(mContext);
        Intent intent=getIntent();
        //通过自增的_id查找数据
        shipList=shiplistservice.findbilltype(intent.getStringExtra("_id"));
        achat_details_tv_billno.setText(shipList.getBillno());
        achat_details_tv_address.setText(shipList.getAddress());
        achat_details_tv_phonenumber.setText(shipList.getPhonenumber());
        achat_details_tv_goodsid.setText(shipList.getGoodsId());
        achat_details_tv_goodsname.setText(shipList.getGoodsname());
        Log.i(TAG, "getGoodsname: "+shipList.getGoodsname());
        achat_details_tv_total.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())+Integer.valueOf(shipList.getOut_qty())));
        achat_details_tv_surplus.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())+Integer.valueOf(shipList.getOut_qty())-Integer.valueOf(shipList.getCin_qty())-Integer.valueOf(shipList.getCout_qty())));
        achat_details_tv_in.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())-Integer.valueOf(shipList.getCin_qty())));
        achat_details_tv_out.setText(String.valueOf(Integer.valueOf(shipList.getOut_qty())-Integer.valueOf(shipList.getCout_qty())));
        if (shipList.getGoodsname().contains("空调")||shipList.getGoodsname().contains("三菱重工海尔风管机RFUTD50WDV 2P")){
//            achat_details_tv_in.setText(String.valueOf(Integer.valueOf(in_qty)+Integer.valueOf(out_qty)-Integer.valueOf(cin_qty)-Integer.valueOf(cout_qty)));
//            achat_details_tv_out.setText(String.valueOf(Integer.valueOf(in_qty)+Integer.valueOf(out_qty)-Integer.valueOf(cin_qty)-Integer.valueOf(cout_qty)));
            achat_details_layout_in.setVisibility(View.VISIBLE);//内机布局控件隐藏
            achat_details_layout_out.setVisibility(View.VISIBLE);//外机布局控件隐藏
        }else{
            achat_details_layout_in.setVisibility(View.GONE);//内机布局控件隐藏
            achat_details_layout_out.setVisibility(View.GONE);//外机布局控件隐藏
        }
        // 从数据库读取listview数据
        List<ScanRec> list=scanlistservice.getAllScanlistListachat(shipList.getBillno());

        list2.clear();
        if (list.size()>5){

            for (int i = 0; i < 5; i++) {
                list2.add(list.get(i));
            }
        }else{
            list2=list;
        }

        adapter = new NoFinishShipListDetailsAdapter(this, list2);
        achat_details_lv.setAdapter(adapter);
        setListViewHeightBasedOnChildren(achat_details_lv);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.achat_details_layout_back:
                finish();//结束当前页面
                break;
            case R.id.achat_details_bt_manually_add:
                dialog = new CustomDialog(AchatDetailsActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                final RadioButton rbin = (RadioButton) dialog.getrbin();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editText.getText().toString().length()==0){
                            TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "内容不能为空");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        if (scanlistservice.findbarcodes(editText.getText().toString())){
                            TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该条码已经添加");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        int backMake = findbarcode(editText.getText().toString(), shipList);
                        if (backMake == 1) {
                            if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该型号内机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(shipList.get_id().toString())) + 1;
                                shiplistservice.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "1");
                                playMusic();
                                Toast.makeText(AchatDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                loadListViewScanlist();//重新加载数据并显示

                            }
                        } else if (backMake == 0) {
                            if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                                // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "该型号外机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                                shiplistservice.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "0");

                                playMusic();
                                Toast.makeText(AchatDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                loadListViewScanlist();//重新加载数据并显示


                            }

                        }else {
                            shake();
                            stopMusic();
                            TransparentDialogUtil.showErrorMessage(AchatDetailsActivity.this, "没有匹配到相应的条码");

                        }
                        dialog.dismiss();

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

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


}
