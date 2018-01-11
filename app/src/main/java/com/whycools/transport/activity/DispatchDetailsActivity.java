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
import com.whycools.transport.utils.Goods;
import com.whycools.transport.utils.GoodsListText;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.view.MyListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 调度详情
 * Created by Zero on 2017-03-30.
 */

public class DispatchDetailsActivity extends BaseActivity implements View.OnClickListener{
    private ShipListService shipListService;//清单
    private ScanlistService scanlistService;//扫描
    private TextView dispatch_details_tv_billno,dispatch_details_tv_goodsid,dispatch_details_tv_goodsname,dispatch_details_tv_total,dispatch_details_tv_surplus,dispatch_details_tv_in,dispatch_details_tv_out,dispatch_details_tv_storeid;
    private MyListView dispatch_details_lv;
    private LinearLayout dispatch_details_layout_in,dispatch_details_layout_out,dispatch_details_layout_back;
    private Button dispatch_details_bt_manually_add;
    private CustomDialog dialog;
    private List<ScanRec> list;
    private ShipList shipList;
    private NoFinishShipListDetailsAdapter adapter;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_dispatch_details);
    }

    @Override
    public void initViews() {
        dispatch_details_tv_billno= findViewById(R.id.dispatch_details_tv_billno);
        dispatch_details_tv_goodsid= findViewById(R.id.dispatch_details_tv_goodsid);
        dispatch_details_tv_goodsname= findViewById(R.id.dispatch_details_tv_goodsname);
        dispatch_details_tv_total= findViewById(R.id.dispatch_details_tv_total);
        dispatch_details_tv_surplus= findViewById(R.id.dispatch_details_tv_surplus);
        dispatch_details_tv_in= findViewById(R.id.dispatch_details_tv_in);
        dispatch_details_tv_out= findViewById(R.id.dispatch_details_tv_out);
        dispatch_details_tv_storeid= findViewById(R.id.dispatch_details_tv_storeid);
        dispatch_details_layout_in= findViewById(R.id.dispatch_details_layout_in);
        dispatch_details_layout_out= findViewById(R.id.dispatch_details_layout_out);
        dispatch_details_layout_back= findViewById(R.id.dispatch_details_layout_back);
        dispatch_details_lv= findViewById(R.id.dispatch_details_lv);
        dispatch_details_bt_manually_add= findViewById(R.id.dispatch_details_bt_manually_add);

    }

    @Override
    public void initListeners() {
        dispatch_details_layout_back.setOnClickListener(this);
        dispatch_details_bt_manually_add.setOnClickListener(this);
        dispatch_details_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                dialog = new CustomDialog(DispatchDetailsActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                editText.setText(list.get(i).getBarcodes());
                editText.setSelection(editText.getText().length());
                final RadioButton rbin = (RadioButton) dialog.getrbin();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editText.getText().length()>0){
                            Log.i(TAG, "onClick: "+editText.getText().toString());
                            scanlistService.updateScanlistBarcodes(editText.getText().toString(),list.get(i).get_id());
                            Log.i(TAG, "onClick:id >>>>>>>>>>>>>>>>>"+list.get(i).get_id());
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
        dispatch_details_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("你确定要删除该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String isInCode=scanlistService.findisInCode(list.get(i).getBarcodes());
                        scanlistService.deleteScanlist(list.get(i).getBarcodes());
                        if(isInCode.equals("1")){
                            int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id()) )- 1;

                            shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id());//修改清单数据库个数
                        }else {
                            int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id()) )- 1;
                            shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id());//修改清单数据库个数
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

    @Override
    public void initData() {
        String scan= SharedPreferencesUtil.getData(DispatchDetailsActivity.this,"scan","1").toString();
        if(scan.equals("1")){
            sm = new ScanDevice();
        }
        loadListViewScanlist();

    }

    /**
     * 加载ListView
     */


    private void loadListViewScanlist() {
        shipListService=new ShipListService(DispatchDetailsActivity.this);
        scanlistService=new ScanlistService(DispatchDetailsActivity.this);
        Intent intent=getIntent();
        shipList=shipListService.findbilltype(intent.getStringExtra("_id"));

        dispatch_details_tv_billno.setText(shipList.getBillno());
        dispatch_details_tv_goodsid.setText(shipList.getGoodsId());
        dispatch_details_tv_goodsname.setText(shipList.getGoodsname());
        dispatch_details_tv_storeid.setText(shipList.getStoreid());
        dispatch_details_tv_total.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())+Integer.valueOf(shipList.getOut_qty())));
        dispatch_details_tv_surplus.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())+Integer.valueOf(shipList.getOut_qty())-Integer.valueOf(shipList.getCin_qty())-Integer.valueOf(shipList.getCout_qty())));
        dispatch_details_tv_in.setText(String.valueOf(Integer.valueOf(shipList.getIn_qty())-Integer.valueOf(shipList.getCin_qty())));
        dispatch_details_tv_out.setText(String.valueOf(Integer.valueOf(shipList.getOut_qty())-Integer.valueOf(shipList.getCout_qty())));
        if (!shipList.getGoodsname().contains("空调")){
//            achat_details_tv_in.setText(String.valueOf(Integer.valueOf(in_qty)+Integer.valueOf(out_qty)-Integer.valueOf(cin_qty)-Integer.valueOf(cout_qty)));
//            achat_details_tv_out.setText(String.valueOf(Integer.valueOf(in_qty)+Integer.valueOf(out_qty)-Integer.valueOf(cin_qty)-Integer.valueOf(cout_qty)));
            dispatch_details_layout_in.setVisibility(View.GONE);//内机布局控件隐藏
            dispatch_details_layout_out.setVisibility(View.GONE);//外机布局控件隐藏
        }
        // 从数据库读取listview数据
        list=scanlistService.getAllScanlistListDispatch(shipList.getBillno());
        List<ScanRec>  list2=new ArrayList<>();
        list2.clear();
        if (list.size()>5){

            for (int i = 0; i < 5; i++) {
                list2.add(list.get(i));
            }
        }else{
            list2=list;
        }
        adapter = new NoFinishShipListDetailsAdapter(this, list2);
        dispatch_details_lv.setAdapter(adapter);
        setListViewHeightBasedOnChildren(dispatch_details_lv);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dispatch_details_layout_back:
                finish();
            break;
            case R.id.dispatch_details_bt_manually_add:
                dialog = new CustomDialog(DispatchDetailsActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                final RadioButton rbin = (RadioButton) dialog.getrbin();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().length()==0){
                            TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "内容不能为空");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        if (scanlistService.findbarcodes(editText.getText().toString())){
                            TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该条码已经添加");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        int backMake = findbarcode(editText.getText().toString(), shipList);
                        if (backMake == 1) {
                            if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该型号内机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id().toString())) + 1;
                                shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "1");
                                playMusic();
                                Toast.makeText(DispatchDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                loadListViewScanlist();//重新加载数据并显示

                            }
                        } else if (backMake == 0) {
                            if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                                // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该型号外机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                                shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "0");

                                playMusic();
                                Toast.makeText(DispatchDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                loadListViewScanlist();//重新加载数据并显示


                            }

                        }else {
                            shake();
                            stopMusic();
                            TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "没有匹配到相应的条码");

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
//            barcodeStr="CBAGD4000"+"12364"+math;
            Log.i(TAG, "barcodeStr: "+barcodeStr);
            if (scanlistService.findbarcodes(barcodeStr)){
                TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该条码已经添加");
                stopMusic();
                shake();
            }else{
                int backMake = findbarcode(barcodeStr, shipList);
                if (backMake == 1) {
                    if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该型号内机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id().toString())) + 1;
                        shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "1");
                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        loadListViewScanlist();//重新加载数据并显示

                    }
                } else if (backMake == 0) {
                    if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                        // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "该型号外机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                        shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "0");

                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        loadListViewScanlist();//重新加载数据并显示


                    }

                }else {
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this, "没有匹配到相应的条码");
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




    Handler handler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 5:
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this,"该条码已扫过");
                    shake();
                    stopMusic();
                    break;
                case 6:
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this,"扫描成功");
                    loadListViewScanlist();
                    playMusic();
                    break;
                case 7:
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this,"内机扫描完了");
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "内机扫描完了", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this,"外机机扫描完了");
//                    Toast.makeText(NoFinishShipListDetailsActivity.this, "外机机扫描完了", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    stopMusic();
                    shake();
                    TransparentDialogUtil.showErrorMessage(DispatchDetailsActivity.this,"没有匹配到相应的条码");

                    break;
            }
        }
    };
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
        scanlist.setGoodsno("调");
        scanlistService.addScanlist(scanlist);
    }

}
