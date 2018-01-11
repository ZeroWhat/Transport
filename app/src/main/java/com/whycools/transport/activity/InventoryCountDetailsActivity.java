package com.whycools.transport.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Stkcksum;
import com.whycools.transport.entity.Upstkcksum;
import com.whycools.transport.service.NewStkcksumService;
import com.whycools.transport.service.UpstkcksumService;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 盘点表详情
 * Created by user on 2017-08-24.
 */

public class InventoryCountDetailsActivity extends BaseActivity {
    private LinearLayout inventorycountdetails_back;//返回按钮
    private TextView iinventorycountdetails_tv_goodsname;
    private TextView inventorycountdetails_tv_number;
    private TextView iinventorycountdetails_tv_sumdate;
    private EditText inventorycountdetails_ed_number;
    private Button iinventorycountdetails_bt_save;
    private Button inventorycountdetails_bt_same;
    private Button inventorycountdetails_bt_back;
    private NewStkcksumService newStkcksumService;
    private UpstkcksumService upstkcksumService;
    private String id;
    private String qmyereal;
    private String goodid;
    private TextView isgoodname;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_inventory_count_details);

    }

    @Override
    public void initViews() {
        newStkcksumService=new NewStkcksumService(this);
        upstkcksumService=new UpstkcksumService(this);
        inventorycountdetails_back= findViewById(R.id.inventorycountdetails_back);
        iinventorycountdetails_tv_goodsname= findViewById(R.id.iinventorycountdetails_tv_goodsname);
        iinventorycountdetails_tv_sumdate= findViewById(R.id.iinventorycountdetails_tv_sumdate);
        inventorycountdetails_tv_number= findViewById(R.id.inventorycountdetails_tv_number);
        isgoodname= findViewById(R.id.isgoodname);
        inventorycountdetails_ed_number= findViewById(R.id.inventorycountdetails_ed_number);
        iinventorycountdetails_bt_save= findViewById(R.id.iinventorycountdetails_bt_save);
        inventorycountdetails_bt_back= findViewById(R.id.inventorycountdetails_bt_back);
        inventorycountdetails_bt_same= findViewById(R.id.inventorycountdetails_bt_same);

    }

    @Override
    public void initListeners() {
        iinventorycountdetails_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newStkcksumService.updateStkcksum(inventorycountdetails_ed_number.getText().toString(),id);
                addUpstkcksum(inventorycountdetails_ed_number.getText().toString());
                Toast.makeText(InventoryCountDetailsActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        inventorycountdetails_bt_same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventorycountdetails_ed_number.setText(qmyereal);
                inventorycountdetails_ed_number.setSelection(inventorycountdetails_ed_number.getText().length());//将光标移至文字末尾
                addUpstkcksum(inventorycountdetails_ed_number.getText().toString());
                newStkcksumService.updateStkcksum(inventorycountdetails_ed_number.getText().toString(),id);
                Toast.makeText(InventoryCountDetailsActivity.this, "一致成功", Toast.LENGTH_SHORT).show();
            }
        });
        inventorycountdetails_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void initData() {

//        String scan= SharedPreferencesUtil.getData(InventoryCountDetailsActivity.this,"scan","1").toString();
//        if(scan.equals("1")){
//            sm = new ScanDevice();
//        }
        Intent intent=getIntent();
        id=intent.getStringExtra("_id");

        Stkcksum stkcksum=newStkcksumService.findByIDStkcksum(id);
        iinventorycountdetails_tv_goodsname.setText("机器名称:"+stkcksum.getGoodsname());
        iinventorycountdetails_tv_sumdate.setText("最后盘点时间:"+stkcksum.getSumdate());
        inventorycountdetails_tv_number.setText("库存个数:"+stkcksum.getQmyereal());
        qmyereal=stkcksum.getQmyereal();
        goodid=stkcksum.getAuto_id();
        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>initData: <<<<<<<<<<"+goodid);
        if(stkcksum.getQmyeCount().length()>0){
            inventorycountdetails_ed_number.setText(stkcksum.getQmyeCount());
        }else{
            inventorycountdetails_ed_number.setText(0+"");

        }
        inventorycountdetails_ed_number.setSelection(inventorycountdetails_ed_number.getText().length());//将光标移至文字末尾


    }

//    /**
//     * 条形码扫描
//     */
//    private ScanDevice sm;
//    private final static String SCAN_ACTION = "scan.rcv.message";
//    private String barcodeStr;
//    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            // 如果获取到该广播。
//            byte[] barocode = intent.getByteArrayExtra("barocode");
//            int barocodelen = intent.getIntExtra("length", 0);
//            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//            Log.i("debug", "----codetype--" + temp);
//            barcodeStr = new String(barocode, 0, barocodelen);
//            //卡萨帝洗衣机CBM1058W1----CB0B3000M----null----1----2016-07-22 06:35:42.82----6901570079431--------20
//            //海尔冰箱BCD-185TMPQ 185L----BK0YF00A1----null----1----2014-11-18 10:30:43.767----6901018052262--------20
//            //海尔电热水器ES40H-Q5(ZE) 40升----GA0SFD01C----null----1----2016-01-03 07:55:12.85----6932290358124--------20
////            barcodeStr ="1520181735203806AOQR12LHCE";//372004G|41FTXR272PC-W    152263190169202
//            Log.i(TAG, "barcodeStr: "+barcodeStr);
////            Toast.makeText(InventoryCountDetailsActivity.this, "----"+barcodeStr, Toast.LENGTH_SHORT).show();
////            barcodeStr="65703827123456789";
//
//
//                TransparentDialogUtil.showLoadingMessage(InventoryCountDetailsActivity.this,"正在匹配中...",true);
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//
//                        NewbarcodeheaderService newbarcodeheaderService=new NewbarcodeheaderService(InventoryCountDetailsActivity.this);
//                        Newbarcodeheader newbarcodeheader=newbarcodeheaderService.getAllNewbarcodeheader(barcodeStr);
//
//                        if (newbarcodeheader!=null&&newbarcodeheader.getGoodsId().equals(goodid)){
//                            Message msg=new Message();
//                            msg.what=1;
//
//                            handler.sendMessage(msg);
//                        }else {
//                            Message msg=new Message();
//                            msg.what=a1;
//
//                            handler.sendMessage(msg);
//
//                        }
//
//                    }
//
//                }.start();
//
//
//
//            sm.stopScan();
//        }
//    };
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        // 如果再活动不可见的时候，如果激光扫描还不为空的情况下，就将激光扫描关闭。
//        if (sm != null) {
//            sm.stopScan();
//        }
//        // 同时将广播撤销。
//        unregisterReceiver(mScanReceiver);
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        // 注册了一个动态广播
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SCAN_ACTION);
//        registerReceiver(mScanReceiver, filter);
//    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.dismiss();
                    Toast.makeText(InventoryCountDetailsActivity.this, "匹配成功", Toast.LENGTH_SHORT).show();
                    iinventorycountdetails_bt_save.setVisibility(View.VISIBLE);
                    inventorycountdetails_bt_same.setVisibility(View.VISIBLE);
                    isgoodname.setVisibility(View.GONE);
                    break;
                case 2:
                    TransparentDialogUtil.showErrorMessage(InventoryCountDetailsActivity.this,"没有匹配到,请确认后在扫描");
                    break;
            }
        }
    };

    private void addUpstkcksum(String qmyecount){
        Stkcksum stkcksum=newStkcksumService.findByIDStkcksum(id);
        Upstkcksum upstkcksum=new Upstkcksum();
        upstkcksum.setAuto_id(stkcksum.getAuto_id());
        upstkcksum.setSumDate(stkcksum.getSumdate());
        upstkcksum.setStoreid(stkcksum.getStoreid());
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String date = formatter.format(curDate);
        upstkcksum.setTime(date);
        upstkcksum.setQmyereal(stkcksum.getQmyereal());
        upstkcksum.setQmyeCount(qmyecount);
        upstkcksum.setGoodsname(stkcksum.getGoodsname());
        upstkcksumService.addUpStkcksum(upstkcksum);

    }
}
