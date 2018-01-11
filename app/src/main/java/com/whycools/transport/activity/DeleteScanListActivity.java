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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maimengmami.waveswiperefreshlayout.WaveSwipeRefreshLayout;
import com.whycools.transport.R;
import com.whycools.transport.adapter.DeleteScanListAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.Scanlistbak;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ScanlistbakService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.ui.SimplePaddingDecoration;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 删除已经扫过的条形码数据
 * Created by Zero on 2016-12-12.
 */
public class DeleteScanListActivity extends BaseActivity implements View.OnClickListener {//Details DeleteScanListDetailsActivity
    private LinearLayout delete_scanlist_layout_back;//返回按钮
    private RecyclerView delete_scanlist_lv;//listview
    private ScanlistService scanlistservice;//扫描数据
    private ScanlistbakService scanlistbakService;//扫描数据临时表
    private ShipListService shiplistservice;
    private List<ScanRec> list = new ArrayList<>();
    private List<ScanRec> newlist = new ArrayList<>();

    private DeleteScanListAdapter adapter;
    private Button delete_scanlist_updata_bt;
    private TextView delete_scanlist_tv_number;

    private WaveSwipeRefreshLayout wave_layout;

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_delete_scanlist);


    }

    @Override
    public void initViews() {

        delete_scanlist_layout_back = findViewById(R.id.delete_scanlist_layout_back);

        wave_layout = findViewById(R.id.wave_layout);
        delete_scanlist_lv = findViewById(R.id.delete_scanlist_lv);
        delete_scanlist_updata_bt = findViewById(R.id.delete_scanlist_updata_bt);
        delete_scanlist_tv_number = findViewById(R.id.delete_scanlist_tv_number);


    }
    private int refresh=0;
    @Override
    public void initListeners() {


        delete_scanlist_layout_back.setOnClickListener(this);
        delete_scanlist_updata_bt.setOnClickListener(this);
        int homepage_refresh_spacing = 40;
        wave_layout.setProgressViewOffset(false, -homepage_refresh_spacing * 2, homepage_refresh_spacing);
        wave_layout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                wave_layout.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        showScanlistData();
                        wave_layout.setRefreshing(false);

                    }
                },2000);
            }

            @Override
            public void onLoad() {

                wave_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addMore();
                        adapter.notifyDataSetChanged();
                        wave_layout.setLoading(false);
                    }
                },2000);

            }

            @Override
            public boolean canLoadMore() {
                return true;
            }

            @Override
            public boolean canRefresh() {
                return true;
            }
        });


    }
    private void addMore(){
        if (newlist.size()<list.size()&&list.size()>5){
            if (list.size()-newlist.size()>=5){
                for (int i = refresh; i < refresh+5; i++) {
                    newlist.add(list.get(i));
                }
                Message msg=new Message();
                msg.what=3;
                handler.sendMessage(msg);
                refresh=refresh+5;
            }else{
                for (int i = newlist.size(); i < list.size(); i++) {
                    newlist.add(list.get(i));
                }
                Message msg=new Message();
                msg.what=3;
                handler.sendMessage(msg);
            }

        }else {
            Message msg=new Message();
            msg.what=2;
            handler.sendMessage(msg);
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showScanlistData();
                    break;
                case 2:
                    Toast.makeText(DeleteScanListActivity.this, "没有了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(DeleteScanListActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    String result = msg.getData().getString("result");
                    String number = msg.getData().getString("number");
                    Log.i(TAG, "返回结果: " + result);
                    if (result.equals("-1")) {
                        SharedPreferencesUtil.saveData(DeleteScanListActivity.this, DateUtile.getdata(0) + "number", (Integer.valueOf(SharedPreferencesUtil.getData(DeleteScanListActivity.this, DateUtile.getdata(0) + "number", "0").toString()) + Integer.valueOf(number)) + "");
                        TransparentDialogUtil.showSuccessMessage(DeleteScanListActivity.this, "上传成功");
                        List<Scanlistbak> scanlistbakdata = scanlistbakService.getAllScanlistbakList();
                        Log.i(TAG, "添加到临时表中的总个数: " + scanlistbakdata.size());
                        for (int j = 0; j < scanlistbakdata.size(); j++) {
                            scanlistservice.deleteScanlist(scanlistbakdata.get(j).getBarcode());
                            Log.i(TAG, "getBarcode存储的值: <<" + j + ">>" + scanlistbakdata.get(j).getBarcode());
                            Log.i(TAG, "删除的个数: " + j);
                            Log.i(TAG, "删除后的结果: " + scanlistservice.findbarcodes(scanlistbakdata.get(j).getBarcode()));
                            Error.contentToTxt(DeleteScanListActivity.this, "扫描条码成功后被删除的条码:" + scanlistbakdata.get(j).getBarcode() + "--------第" + j + "个");
                        }
                        showScanlistData();
                    } else {
                        //上传失败的原因一般就是上传字段错误(仔细检查字段)
//						Toast.makeText(TheMenuOfActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(DeleteScanListActivity.this, "上传失败");
                    }
                    break;
                case 7:
                    TransparentDialogUtil.showSuccessMessage(DeleteScanListActivity.this, "上传成功");
                    showScanlistData();
                    break;
                case 8:
                    TransparentDialogUtil.showSuccessMessage(DeleteScanListActivity.this, "上传失败");
                    shake();
                    showScanlistData();
                    break;
            }
        }
    };

    @Override
    public void initData() {


        String scan = SharedPreferencesUtil.getData(DeleteScanListActivity.this, "scan", "1").toString();
        if (scan.equals("1")) {
            sm = new ScanDevice();
        }


    }

    private void showScanlistData() {
        scanlistservice = new ScanlistService(mContext);
        scanlistbakService = new ScanlistbakService(mContext);
        shiplistservice = new ShipListService(mContext);
        list.clear();
        newlist.clear();
        list = scanlistservice.getAllScanlistList();
        if (list.size() == 0) {
            TransparentDialogUtil.showErrorMessage(mContext, "当前没有扫描数据");
        }
        delete_scanlist_tv_number.setText("共" + list.size() + "条");
        if (list.size() > 5) {
            for (int i = 0; i < 5; i++) {
                newlist.add(list.get(i));
            }
            adapter = new DeleteScanListAdapter(mContext, newlist);
        } else {
            adapter = new DeleteScanListAdapter(mContext, list);
        }
        Log.i(TAG, "大于newlist个数----------------: " + newlist.size());

        adapter.setOnItemClickListener(new DeleteScanListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(DeleteScanListActivity.this, DeleteScanListDetailsActivity.class).putExtra("_id", list.get(position).get_id().toString()));
            }
        });
        adapter.setOnItemLongClickListener(new DeleteScanListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteScanListActivity.this);
                builder.setMessage("你确定要删除该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanlistservice.deleteScanlist(list.get(position).getBarcodes());
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);

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


        delete_scanlist_lv.setLayoutManager(new LinearLayoutManager(delete_scanlist_lv.getContext()));
        //添加Android自带的分割线
        delete_scanlist_lv.addItemDecoration(new SimplePaddingDecoration(this));
        delete_scanlist_lv.setAdapter(adapter);
        delete_scanlist_lv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_scanlist_layout_back:
                finish();
                break;

            case R.id.delete_scanlist_updata_bt:
                TransparentDialogUtil.showLoadingMessage(DeleteScanListActivity.this, "正在上传，请稍后...", false);

                new Thread(scanListUpload).start();

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            showScanlistData();
            // 注册了一个动态广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(SCAN_ACTION);
            registerReceiver(mScanReceiver, filter);
        } catch (Exception e) {
            Error.contentToTxt(DeleteScanListActivity.this, "删除页面数据加载异常" + e.getMessage());//异常写入文档

        }
    }

    //条码上传线程，一条一条上传
    private Runnable scanListUpload = new Runnable() {

        @Override
        public void run() {
            List<ScanRec> listSan = scanlistservice.getAllScanlistList();
            if (listSan.size() == 0) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }
            Error.contentToTxt(DeleteScanListActivity.this, "应该上传扫描数据的个数:" + listSan.size());
            for (int i = 0; i < listSan.size(); i++) {
                String str = listSan.get(i).getShipId() + "~"//枪id：
                        + listSan.get(i).getUserId() + "~"//用户id：817
                        + listSan.get(i).getStoreId() + "~"//仓库id	214
                        + listSan.get(i).getCarId() + "~"//车辆id： 21
                        + listSan.get(i).getBilltype() + "~"//订单类别： 出，入
                        + listSan.get(i).getBillno() + "~"//订单号8位 98742121
                        + listSan.get(i).getGoodsId() + "~"//产品id  64321
                        + listSan.get(i).getQty() + "~"//数量（出库负数，入库正数）-1,1
                        + listSan.get(i).getBarcodes() + "~"//条码：
                        + listSan.get(i).getBar69() + "~"//69码
                        + listSan.get(i).getIsInCode() + "~"//是否内机：0,1
                        + listSan.get(i).getTime();//2016-08-26 11:27:54
                Error.contentToTxt(DeleteScanListActivity.this, "条码上传的数据:" + i + ">>>>>>>>>>>>>>>>>" + str);
                Log.i("条码上传的数据---", "run: " + "exec UploadBarcodeQty '" + str + "!'");
                String url = SharedPreferencesUtil.getData(mContext, "ServerAddress", SERVERDDRESS).toString();
                String url_synchronization = url + "rent/rProxyDsetExec.jsp?deviceid=&s=" + Zip.compress("exec UploadBarcodeQty '" + str + "!'");
                Error.contentToTxt(DeleteScanListActivity.this, "条码上传的数据url:" + i + ">>>>>>>>>>>>>>>>>" + url_synchronization);
                Log.i(TAG, ": " + url_synchronization);
                String result = RequestData.getResult(url_synchronization);
                Log.i(TAG, "返回结果: " + result);
                if (result.equals("-1")) {
                    scanlistservice.deleteScanlist(listSan.get(i).getBarcodes());
                } else {
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);
                    return;
                }
            }
            Message msg = new Message();
            msg.what = 7;
            handler.sendMessage(msg);
        }
    };


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
            if (scanlistservice.findbarcodes(barcodeStr)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteScanListActivity.this);
                builder.setMessage("你确定要删除<" + barcodeStr + ">该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScanRec scanRec = scanlistservice.findShipid(barcodeStr);
                        if (scanRec.getIsInCode().equals("1")) {
                            int cin = Integer.valueOf(shiplistservice.findshiplistCinqty(scanRec.getShipId())) - 1;
                            shiplistservice.upshiplistinqtydata(String.valueOf(cin), scanRec.getShipId());//修改清单数据库个数
                        } else if (scanRec.getIsInCode().equals("0")) {
                            int cout = Integer.valueOf(shiplistservice.findshiplistCoutqty(scanRec.getShipId())) - 1;

                            shiplistservice.upshiplistoutqtydata(String.valueOf(cout), scanRec.getShipId());//修改清单数据库个数
                        }
                        scanlistservice.deleteScanlist(barcodeStr);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.create().show();


            } else {
                TransparentDialogUtil.showErrorMessage(DeleteScanListActivity.this, "没有该条码");
                stopMusic();
                shake();
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


}
