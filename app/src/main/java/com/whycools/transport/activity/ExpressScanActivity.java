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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.whycools.transport.utils.SendData;

/**
 * 快递扫描
 * Created by Zero on 2017-01-11.
 */

public class ExpressScanActivity extends BaseActivity implements View.OnClickListener{
    private final static String TAG="快递扫描";
    private LinearLayout express_scan_layout_back;
    private ListView express_scan_lv;
    private TextView express_scan_tv_context;
    private ShipListService shiplistservice;//发货清单
    private ShipListAdapter adapter;
    private List<ShipList> shiplist=new ArrayList<>();
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_express_scan);

    }

    @Override
    public void initViews() {
        express_scan_layout_back= findViewById(R.id.express_scan_layout_back);
        express_scan_lv= findViewById(R.id.express_scan_lv);
        express_scan_tv_context= findViewById(R.id.express_scan_tv_context);
    }

    @Override
    public void initListeners() {
        express_scan_layout_back.setOnClickListener(this);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.showLoadingMessage(mContext,"正在查询请稍后。。。",true);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                            String shiplist_url = url + "rent/rProxy.jsp?deviceid=" + "&s=" + Zip.compress("getShiplist4gunByDeliverNo  '"+barcodeStr+"'");
                            Log.i(TAG, "url run: "+shiplist_url);
                            String result = RequestData.getResult(shiplist_url);
                            Log.i(TAG, "结果run: "+result);
                            if (result.length()<15){
                                Log.i(TAG, "数据请求失败，请重新请求");
                            }else {
                                try {
                                    Log.i(TAG, "解析result" + result);
                                    JSONObject obj = new JSONObject(result);
                                    JSONArray array = obj.getJSONArray("results");

                                    Log.i(TAG, "array个数为: " + array.length());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject data = array.getJSONObject(i);
                                        String storeid = data.has("storeid") ? data.getString("storeid") : "";//仓库id  214
                                        String shipdate = data.has("shipdate") ? data.getString("shipdate") : "";//发货日期  2116-01-25
                                        String seqno = data.has("seqno") ? data.getString("seqno") : "";//0
                                        String carid = data.has("carid") ? data.getString("carid") : "";    //0
                                        Log.i(TAG, "carid: " + carid);
                                        String carname = data.has("carname") ? data.getString("carname") : "";//""
                                        Log.i(TAG, "carname: " + carname);
                                        String billtype = data.has("billtype") ? data.getString("billtype") : "";//入
                                        String orderno = data.has("orderno") ? data.getString("orderno") : "";//发货编号
                                        String address_company = data.has("address_company") ? data.getString("address_company") : "";//地址
                                        String goodsid = data.has("goodsid") ? data.getString("goodsid") : "";//产品编号33311
                                        String goodsname = data.has("goodsname") ? data.getString("goodsname") : "";//产品名称
                                        String innerno = data.has("innerno") ? data.getString("innerno") : "";//编码
                                        String outerno = data.has("outerno") ? data.getString("outerno") : "";//外机编码
                                        String inoutFlag = data.has("inoutFlag") ? data.getString("inoutFlag") : "";//-1
                                        Log.i(TAG, "inoutFlag: " + inoutFlag);
                                        String address = data.has("address") ? data.getString("address") : "";//地址
                                        String gsm = data.has("gsm") ? data.getString("gsm") : "";//电话号码
                                        String qty = data.has("qty") ? data.getString("qty") : "";//数量
                                        String stateId = data.has("stateId") ? data.getString("stateId") : "";//标识
                                        ShipList shiplist = new ShipList();
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
                                        if (goodsname.contains("空调")) {
                                            shiplist.setIn_qty(qty);
                                            shiplist.setOut_qty(qty);
                                        } else {
                                            shiplist.setIn_qty(qty);
                                            shiplist.setOut_qty("0");
                                        }
                                        shiplist.setCin_qty("0");
                                        shiplist.setCout_qty("0");
                                        if (stateId.equals("3")) {
                                            shiplist.setIsfinish("已完成");
                                        } else {
                                            shiplist.setIsfinish("完成");
                                        }

                                        if (stateId.equals("0")) {
                                            shiplist.setIsStart("送达");
                                        } else {
                                            shiplist.setIsStart("发车");
                                        }

                                        shiplist.setDistance("0.0公里");
                                        shiplist.setStateId(stateId);
                                        shiplist.setCm(barcodeStr);
                                        if (shiplistservice.findorderno(orderno) > 0) {
                                            shiplistservice.deleteorderno(orderno);
                                        }
                                        shiplistservice.addShipListData(shiplist);
                                        Log.i(TAG, "数据添加中。。。。。。 ");
                                    }
                                    Log.i(TAG, "数据请求成功添加数据");



                                } catch (Exception e) {
                                    Log.i(TAG, "错误: " + e.getMessage());
                                }

                            }
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }.start();


                    break;
                case 2:
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            shiplist.clear();
                            shiplist=shiplistservice.getAllExpressScan(barcodeStr);
                            Message msg=new Message();
                            msg.what=3;
                            handler.sendMessage(msg);
                        }
                    }.start();
                    break;
                case 3:
                    TransparentDialogUtil.dismiss();
                    if(shiplist.size()>0){
                        express_scan_tv_context.setVisibility(View.GONE);
                        adapter=new ShipListAdapter(mContext,shiplist);
                        express_scan_lv.setAdapter(adapter);
                    }else{

                        express_scan_tv_context.setText("该快递单号没有数据");
                        stopMusic();
                    }


                    break;
                case 5:
                    final String _id=msg.getData().getString("_id");
                    Log.i(TAG, "_id: "+_id);
                    final String billno=msg.getData().getString("Billno");
                    Log.i(TAG, "billno: "+billno);
                    final String Shipdate5=msg.getData().getString("Shipdate");
                    Log.i(TAG, "Shipdate: "+Shipdate5);
//                    Toast.makeText(getContext(), "疑难按钮"+billno, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(R.drawable.transparent_info);
                    builder.setTitle("疑难选择");
                    //    指定下拉列表的显示数据
                    final String[] cities = {"地址错", "用户不在", "用户拒收"};
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                                    String userid= SharedPreferencesUtil.getData(mContext,"userid","000").toString();
//                                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                                    String strdate = formatter.format(curDate);
                                    Log.i(TAG, "压缩字符串: "+"shipDoneConfirm "+userid+", "+billno+", '"+Shipdate5+"', 2, '"+cities[i]+"'");
                                    String url_=url+"rent/rProxy.jsp?deviceid=&s="+ Zip.compress("shipDoneConfirm "+userid+", "+billno+", '"+Shipdate5+"', 2, '"+cities[i]+"'");
                                    Log.i(TAG, "完成发车的url: "+url_);
                                    String resu = RequestData.getResult(url_);
                                    Log.i(TAG, "resu: "+resu);
                                    shiplistservice.upshiplistisfinishdate("已完成",_id);
                                }
                            }.start();
//                            Toast.makeText(getContext(), "当前状态为：" + cities[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                    break;

            }
        }
    };

    @Override
    public void initData() {
        String scan= SharedPreferencesUtil.getData(ExpressScanActivity.this,"scan","1").toString();
        if(scan.equals("1")){
            sm = new ScanDevice();
        }

        shiplistservice=new ShipListService(mContext);


//

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
//            barcodeStr="719575293236";
            Message msg=new Message();
            msg.what=1;
            handler.sendMessage(msg);
            sm.stopScan();
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.express_scan_layout_back:
                finish();
                break;
        }
    }

    class ShipListAdapter extends BaseAdapter {

        private List<ShipList> list = null;
        private LayoutInflater layoutInflater = null;
        private Context context;
        private ViewHolder viewHolder = null;

        public ShipListAdapter(Context context, List<ShipList> list) {
            this.list = list;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.fragment_nofinishshiplist_lv_item,parent,false);
                viewHolder.nofinishshiplist_lv_item_tv_address = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_address);
                viewHolder.nofinishshiplist_lv_item_tv_billno = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_billno);
                viewHolder.nofinishshiplist_lv_item_bt_start= convertView.findViewById(R.id.nofinishshiplist_lv_item_bt_start);
                viewHolder.nofinishshiplist_lv_item_tv_finish= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_finish);
                viewHolder.nofinishshiplist_lv_item_tv_distance= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_distance);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.nofinishshiplist_lv_item_tv_address.setText(list.get(position).getAddress());
            viewHolder.nofinishshiplist_lv_item_tv_billno.setText(list.get(position).getBillno());
            viewHolder.nofinishshiplist_lv_item_bt_start.setText(list.get(position).getIsStart());

            viewHolder.nofinishshiplist_lv_item_tv_finish.setText("送达");
            viewHolder.nofinishshiplist_lv_item_tv_finish.setVisibility(View.VISIBLE);
            viewHolder.nofinishshiplist_lv_item_tv_finish.setText("疑难");
            viewHolder.nofinishshiplist_lv_item_tv_distance.setVisibility(View.GONE);
//            if (list.get(position).getIsStart().equals("发车")){
//                viewHolder.nofinishshiplist_lv_item_tv_finish.setVisibility(View.GONE);
//            }
//            if (list.get(position).getIsStart().equals("送达")){
//
//            }
            viewHolder.nofinishshiplist_lv_item_bt_start.setOnClickListener(new btClick(position));//发车按钮监听
            viewHolder.nofinishshiplist_lv_item_tv_finish.setOnClickListener(new btClick(position));//完成按钮监听
            return convertView;
        }

        private class ViewHolder {
            TextView nofinishshiplist_lv_item_tv_address = null;
            TextView nofinishshiplist_lv_item_tv_billno = null;
            Button nofinishshiplist_lv_item_bt_start=null;
            Button nofinishshiplist_lv_item_tv_finish=null;
            TextView nofinishshiplist_lv_item_tv_distance=null;
        }
        /*
* 此为listview条目中的按钮点击事件的写法
*/
        class btClick implements View.OnClickListener {
            private int position;
            public btClick(int pos){  // 在构造时将position传给它，这样就知道点击的是哪个条目的按钮
                this.position = pos;
            }
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.nofinishshiplist_lv_item_bt_start:
                        Intent intent=new Intent(mContext,NoFinishShipListDetailsActivity.class);
                        intent.putExtra("_id",list.get(position).get_id());
                        startActivity(intent);
                        break;
                    case R.id.nofinishshiplist_lv_item_tv_finish:
                        Message msg = new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("Billno",list.get(position).getBillno());
                        bundle.putString("Shipdate",list.get(position).getShipdate());
                        msg.setData(bundle);
                        msg.what = 5;
                        handler.sendMessage(msg);
                        break;

                }

            }

        }


    }




}
