package com.whycools.transport.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 上传查询
 * Created by Zero on 2017-03-15.
 */

public class UploadQueryActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout upload_query_layout_back;
    private TextView upload_query_tv_result;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_upload_query);

    }

    @Override
    public void initViews() {
        upload_query_layout_back= findViewById(R.id.upload_query_layout_back);
        upload_query_tv_result= findViewById(R.id.upload_query_tv_result);

    }

    @Override
    public void initListeners() {
        upload_query_layout_back.setOnClickListener(this);

    }

    @Override
    public void initData() {
        upload_query_tv_result.setText("请扫描条码进行查询");
        String scan= SharedPreferencesUtil.getData(UploadQueryActivity.this,"scan","1").toString();
        if(scan.equals("1")){
            sm = new ScanDevice();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload_query_layout_back:
                finish();
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
            // 如果获取到该广播。
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
            TransparentDialogUtil.showLoadingMessage(UploadQueryActivity.this,"正在查询请稍后...",true);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                    String  getBarcode_url= url + "rent/rProxy.jsp?deviceid=" + "&s=" + Zip.compress("getBarcode '"+barcodeStr+"'");//发货清单文件请求url
                    String getBarcode = RequestData.getResult(getBarcode_url);
                    Message msg=new Message();
                    msg.what=1;
                    Bundle bundle=new Bundle();
                    bundle.putString("getBarcode",getBarcode);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }.start();
            sm.stopScan();
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //{"results":[{"postdate":"2009-03-13 15:07:08.743","billno":"SJ98151777","carid":"","qty":"1","storeid":"1","barcode":"74702002305"}]}
                    String getBarcode= msg.getData().get("getBarcode").toString();
                    if(getBarcode.length()<10){
                        upload_query_tv_result.setText("抱歉没有查到");
                       

                    }
                    try {
                        JSONObject obj=new JSONObject(getBarcode);
                        String results=obj.getString("results");
                        JSONArray array=new JSONArray(results);
                        StringBuffer buffer=new StringBuffer();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj2=array.getJSONObject(i);
                            String postdate=obj2.getString("postdate");
                            String billno=obj2.getString("billno");
                            String carid=obj2.getString("carid");
                            String qty=obj2.getString("qty");
                            String storeid=obj2.getString("storeid");
                            String barcode=obj2.getString("barcode");
                            buffer.append("postdate:"+postdate+"\r\n");
                            buffer.append("billno:"+billno+"\r\n");
                            buffer.append("carid:"+carid+"\r\n");
                            buffer.append("qty:"+qty+"\r\n");
                            buffer.append("storeid:"+storeid+"\r\n");
                            buffer.append("barcode:"+barcode+"\r\n");
                        }
                        upload_query_tv_result.setText(buffer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    TransparentDialogUtil.dismiss();
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
}
