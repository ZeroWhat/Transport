package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.adapter.DeliveryByCarDetailsSingleAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.Goods;
import com.whycools.transport.utils.GoodsListText;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 按车发货详细内容
 * Created by Zero on 2017-03-15.
 */

public class DeliveryByCarDetailsSingleActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG="按车发货详细内容";
    private LinearLayout delivery_by_car_details_single_layout_back;
    private TextView delivery_by_car_details_single_tv_context;
    private ListView delivery_by_car_details_single_lv;
    private Button delivery_by_car_details_single_bt;
    private DeliveryByCarDetailsSingleAdapter deliveryByCarDetailsSingleAdapter;
    private ScanlistService scanlistService;
    private ShipListService shipListService;
    private List<ScanRec> scanlist;
    private CustomDialog dialog;
    private ShipList shipList;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_car_details_single);
    }

    @Override
    public void initViews() {
        delivery_by_car_details_single_layout_back= findViewById(R.id.delivery_by_car_details_single_layout_back);
        delivery_by_car_details_single_tv_context= findViewById(R.id.delivery_by_car_details_single_tv_context);
        delivery_by_car_details_single_lv= findViewById(R.id.delivery_by_car_details_single_lv);
        delivery_by_car_details_single_bt= findViewById(R.id.delivery_by_car_details_single_bt);

    }

    @Override
    public void initListeners() {
        delivery_by_car_details_single_layout_back.setOnClickListener(this);
        delivery_by_car_details_single_bt.setOnClickListener(this);
        delivery_by_car_details_single_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("你确定要删除该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String isInCode=scanlistService.findisInCode(scanlist.get(i).getBarcodes());
                        scanlistService.deleteScanlist(scanlist.get(i).getBarcodes());
                        if(isInCode.equals("1")){
                            int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id()) )- 1;

                            shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id());//修改清单数据库个数
                        }else {
                            int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id()) )- 1;
                            shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id());//修改清单数据库个数
                        }

                        showData();

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
        delivery_by_car_details_single_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                dialog = new CustomDialog(DeliveryByCarDetailsSingleActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                editText.setText(scanlist.get(i).getBarcodes());
                editText.setSelection(editText.getText().length());
                final RadioButton rbin = (RadioButton) dialog.getrbin();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editText.getText().length()>0){
                            scanlistService.updateScanlistBarcodes(editText.getText().toString(),scanlist.get(i).get_id());
                            showData();
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
    }

    @Override
    public void initData() {


            String scan= SharedPreferencesUtil.getData(DeliveryByCarDetailsSingleActivity.this,"scan","1").toString();
            if(scan.equals("1")){
                sm = new ScanDevice();
            }
        showData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delivery_by_car_details_single_layout_back:
                finish();
                break;
            case R.id.delivery_by_car_details_single_bt:
                dialog = new CustomDialog(DeliveryByCarDetailsSingleActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().length()==0){
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "内容不能为空");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        if (scanlistService.findbarcodes(editText.getText().toString())){
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该条码已经添加");
                            stopMusic();
                            shake();
                            dialog.dismiss();
                            return;
                        }
                        int backMake = findbarcode(editText.getText().toString(), shipList);
                        if (backMake == 1) {
                            if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该型号内机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id().toString())) + 1;
                                shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "1");
                                playMusic();
                                Toast.makeText(DeliveryByCarDetailsSingleActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                showData();//重新加载数据并显示

                            }
                        } else if (backMake == 0) {
                            if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                                // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                                TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该型号外机已经扫描完了");
                                stopMusic();
                                shake();

                            } else {
                                int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                                shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                                addScanlistService(editText.getText().toString(), "0");

                                playMusic();
                                Toast.makeText(DeliveryByCarDetailsSingleActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                showData();//重新加载数据并显示


                            }

                        }else {
                            shake();
                            stopMusic();
                            TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "没有匹配到相应的条码");

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
     * 添加扫描后的条码数据
     */
    private void addScanlistService(String Barcodes,String isin) {
        ScanRec scanlist = new ScanRec();

        TelephonyManager tm = (TelephonyManager) getSystemService(
                TELEPHONY_SERVICE);//获取设备信息

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        scanlist.setStoreId(shipList.getStateId());// 1

        if (tm.getDeviceId()!=null){
            scanlist.setShipId(tm.getDeviceId());
        }else{
            scanlist.setShipId("0000");
        }
        scanlist.setShipId(shipList.get_id());
        scanlist.setCarId(shipList.getCarId());
        scanlist.setUserId(SharedPreferencesUtil.getData(mContext,"userid","000").toString());
        scanlist.setCarname(shipList.getCarname());// 4车名称
        scanlist.setBillno(shipList.getBillno());// 5单据编号
        scanlist.setBilltype(shipList.getBilltype());// 6单据类别
        scanlist.setGoodsId(shipList.getGoodsId());// 7产品id
        scanlist.setGoodsname(shipList.getGoodsname());// 8产品名
        scanlist.setBar69("");// 9 69码
        scanlist.setBarcodes(Barcodes);// 10
        scanlist.setQty(String.valueOf(Integer.valueOf(shipList.getInoutFlag())*1));// 11 数量
        scanlist.setTime(str);// 12 扫码时间
        scanlist.setIsInCode(isin);// 13
        scanlist.setGoodsno("车");
        Error.contentToTxt(DeliveryByCarDetailsSingleActivity.this,scanlist.getStoreId()+">>>>>>"+//1
                scanlist.getShipId()+">>>>>>"+//2
                scanlist.getCarId()+">>>>>>"+//3
                scanlist.getUserId()+">>>>>>"+
                scanlist.getCarname()+">>>>>>"+//4
                scanlist.getBilltype()+">>>>>>"+//5
                scanlist.getBillno()+">>>>>>"+ //6
                scanlist.getGoodsId()+">>>>>>"+//7
                scanlist.getGoodsname()+">>>>>>"+ //8
                scanlist.getBar69()+">>>>>>"+//9
                scanlist.getBarcodes()+">>>>>>"+//10
                scanlist.getQty()+">>>>>>"+//11
                scanlist.getTime()+">>>>>>"+//12
                scanlist.getIsInCode()+">>>>>>"+//13
                scanlist.getGoodsno());
        scanlistService.addScanlist(scanlist);

    }

    //数据展示
    private void showData(){

        scanlistService=new ScanlistService(DeliveryByCarDetailsSingleActivity.this);
        shipListService=new ShipListService(DeliveryByCarDetailsSingleActivity.this);

        Intent intent=getIntent();

        shipList=shipListService.findid(intent.getStringExtra("_id"));


        StringBuffer buffer=new StringBuffer();
        buffer.append("第"+intent.getStringExtra("index")+"条"+"\r\n");
        buffer.append(shipList.getGoodsname()+"\r\n");
        if (shipList.getGoodsname().toString().contains("空调")) {
            Log.i(TAG, "重新获取数据:总数 "+(Integer.valueOf(shipList.getIn_qty().toString()) +Integer.valueOf(shipList.getOut_qty().toString())));
            Log.i(TAG, "重新获取数据:内机 "+(Integer.valueOf(shipList.getIn_qty().toString()) -Integer.valueOf(shipList.getCin_qty().toString())));
            Log.i(TAG, "重新获取数据:外机 "+(Integer.valueOf(shipList.getOut_qty().toString()) -Integer.valueOf(shipList.getCout_qty().toString())));
            if(shipList.getGoodsname().toString().contains("内机")||shipList.getGoodsname().toString().contains("外机")||shipList.getGoodsname().toString().contains("遥控器")){
                buffer.append("总共：" + Integer.valueOf(shipList.getIn_qty().toString()) + "台\r\n");
                buffer.append("剩余台数为：" + (Integer.valueOf(shipList.getIn_qty().toString()) -Integer.valueOf(shipList.getCin_qty().toString())) + "台\r\n");
            }else{
                buffer.append("总共：" + (Integer.valueOf(shipList.getIn_qty().toString()) +Integer.valueOf(shipList.getOut_qty().toString()))+ "台\r\n");
                buffer.append("剩余内机：" + (Integer.valueOf(shipList.getIn_qty().toString()) -Integer.valueOf(shipList.getCin_qty().toString())) + "台\r\n");
                buffer.append("剩余外机：" + (Integer.valueOf(shipList.getOut_qty().toString()) -Integer.valueOf(shipList.getCout_qty().toString())) + "台\r\n");
            }

        } else {
            Log.i(TAG, "重新获取数据:总数 "+Integer.valueOf(shipList.getIn_qty().toString()));
            Log.i(TAG, "重新获取数据:内机 "+(Integer.valueOf(shipList.getIn_qty().toString()) -Integer.valueOf(shipList.getCin_qty().toString())));

            buffer.append("总共：" + Integer.valueOf(shipList.getIn_qty().toString()) + "台\r\n");
            buffer.append("剩余台数为：" + (Integer.valueOf(shipList.getIn_qty().toString()) -Integer.valueOf(shipList.getCin_qty().toString())) + "台\r\n");
        }
        buffer.append("-----------------------"+"\r\n");



        delivery_by_car_details_single_tv_context.setText(buffer);
        scanlist= scanlistService.getAllScanlistbycar(shipList.getCarname(),shipList.getGoodsname());
        deliveryByCarDetailsSingleAdapter=new DeliveryByCarDetailsSingleAdapter(DeliveryByCarDetailsSingleActivity.this,scanlist);
        delivery_by_car_details_single_lv.setAdapter(deliveryByCarDetailsSingleAdapter);

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
//             int math=(int)((Math.random()*9+1)*100000);
//             barcodeStr="BS01U000N11234"+""+math;
            Log.i(TAG, "barcodeStr: "+barcodeStr);
            if (scanlistService.findbarcodes(barcodeStr)){
                TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该条码已经添加");
                stopMusic();
                shake();
            }else{
                int backMake = findbarcode(barcodeStr, shipList);
                if (backMake == 1) {
                    if ((Integer.valueOf(shipList.getIn_qty().toString()) - Integer.valueOf(shipList.getCin_qty().toString())) <= 0) {
//                            Toast.makeText(context, "内机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该型号内机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cin = Integer.valueOf(shipListService.findshiplistCinqty(shipList.get_id().toString())) + 1;
                        shipListService.upshiplistinqtydata(String.valueOf(cin), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "1");
                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        showData();//重新加载数据并显示

                    }
                } else if (backMake == 0) {
                    if ((Integer.valueOf(shipList.getOut_qty().toString()) - Integer.valueOf(shipList.getCout_qty().toString())) <= 0) {

                        // Toast.makeText(context, "外机已经扫描完了", Toast.LENGTH_SHORT).show();
                        TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "该型号外机已经扫描完了");
                        stopMusic();
                        shake();

                    } else {
                        int cout = Integer.valueOf(shipListService.findshiplistCoutqty(shipList.get_id().toString())) + 1;

                        shipListService.upshiplistoutqtydata(String.valueOf(cout), shipList.get_id().toString());//修改清单数据库个数
                        addScanlistService(barcodeStr, "0");

                        playMusic();
                        Toast.makeText(context, "扫描成功", Toast.LENGTH_SHORT).show();
                        showData();//重新加载数据并显示


                    }

                }else {
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(DeliveryByCarDetailsSingleActivity.this, "没有匹配到相应的条码");
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
