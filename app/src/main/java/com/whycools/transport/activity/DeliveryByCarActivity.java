package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.adapter.DeliveryByCarAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.service.StoresService;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 按车发送
 * Created by Zero on 2016-12-05.
 */
public class DeliveryByCarActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG="按车发送页面";
    private LinearLayout delivery_by_car_layout_back;//返回按钮
    private ListView delivery_by_car_lv;//ListView
    private DeliveryByCarAdapter adapter;
    private List<ShipList> DeliveryByCarData=new ArrayList<>();
    private ShipListService shiplistservice;
    private Spinner delivery_by_car_sp_shipdate;
    private List<String> datelist=new ArrayList<>();
    private ArrayAdapter<String> shipdateadapter;//日期适配器

    private String intent_billtype="";
    private TextView delivery_by_car_title;

    @Override
    public void setContentView() {

        try {
            setContentView(R.layout.activity_delivery_by_car);
        }catch (Exception e){
            Error.contentToTxt(DeliveryByCarActivity.this,"按车发送页面启动异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initViews() {

        try {
            delivery_by_car_layout_back= findViewById(R.id.delivery_by_car_layout_back);
            delivery_by_car_lv= findViewById(R.id.delivery_by_car_lv);
            delivery_by_car_sp_shipdate= findViewById(R.id.delivery_by_car_sp_shipdate);
            delivery_by_car_title= findViewById(R.id.delivery_by_car_title);

            shiplistservice=new ShipListService(DeliveryByCarActivity.this);
        }catch (Exception e){
            Error.contentToTxt(DeliveryByCarActivity.this,"按车发送页面控件实例化异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initListeners() {

        try {
            delivery_by_car_layout_back.setOnClickListener(this);
            delivery_by_car_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    storeDialog(DeliveryByCarData.get(i).getCarname(),delivery_by_car_sp_shipdate.getSelectedItem().toString());
                }
            });
            delivery_by_car_sp_shipdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    loadData(delivery_by_car_sp_shipdate.getSelectedItem().toString(),intent_billtype);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
            Error.contentToTxt(DeliveryByCarActivity.this ,"按车发送页面控件按钮监听异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initData() {

        try {
            intent_billtype=getIntent().getStringExtra("billtype");
            if (intent_billtype.equals("出")){
                delivery_by_car_title.setText("按车发货");
            }else{
                delivery_by_car_title.setText("按车入库");
            }
            Log.i(TAG, "intent_billtype: "+intent_billtype);
            datelist.add(DateUtile.getdata(0));
            datelist.add(DateUtile.getdata(1));
            datelist.add(DateUtile.getdata(2));
            shipdateadapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, datelist);
            //仓库适配器设置样式
            shipdateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //仓库下拉框加载适配器
            delivery_by_car_sp_shipdate.setAdapter(shipdateadapter);
            delivery_by_car_sp_shipdate.setSelection(0);

            loadData(delivery_by_car_sp_shipdate.getSelectedItem().toString(),intent_billtype);
//            loadData("2017-02-21");


        }catch (Exception e){
            Error.contentToTxt(DeliveryByCarActivity.this,"按车发送页面数据加载异常"+e.getMessage());//异常写入文档

        }


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.dismiss();
                    String result = msg.getData().getString("result");
                    TransparentDialogUtil.showInfoMessage(mContext,result);
                    for (int i = 0; i < DeliveryByCarData.size(); i++) {
                        Log.i(TAG, "handleMessage: "+DeliveryByCarData.get(i).getCarname());
                    }
                    for ( int i = 0 ; i < DeliveryByCarData.size() - 1 ; i ++ ) {
                        for ( int j = DeliveryByCarData.size() - 1 ; j > i; j -- ) {
                            if (DeliveryByCarData.get(j).getCarname().equals(DeliveryByCarData.get(i).getCarname())) {
                                DeliveryByCarData.remove(j);
                            }
                        }
                    }
                    adapter=new DeliveryByCarAdapter(mContext,DeliveryByCarData);
                    delivery_by_car_lv.setAdapter(adapter);
                    break;
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delivery_by_car_layout_back:
                finish();
                break;
        }
    }
    private void loadData(final String shipdate,final String billtype){
        new Thread(){
            @Override
            public void run() {
                super.run();
                DeliveryByCarData.clear();
                Log.i(TAG, "shipdate: "+shipdate);
                DeliveryByCarData=shiplistservice.getshiplistcarid(shipdate,billtype);
                String result="";
                if (DeliveryByCarData.size()>0){
                    result="数据加载成功";
                }else{
                    result="当前没有数据，同步一下试试";
                }

                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }.start();
    }
    private int storeIndex=0;
    private void storeDialog(final String carname, final String shipdate){

        storeIndex=0;
        ShipListService shipListService=new ShipListService(this);
        StoresService storesService=new StoresService(this);
        List storeidList = new ArrayList(new HashSet(shipListService.allStoreid(carname,intent_billtype,shipdate)));
        List<String> storesList = new ArrayList();
        for (int i = 0; i < storeidList.size(); i++) {
            Log.i(TAG, "仓库id: "+storeidList.get(i).toString());
            if (!storeidList.get(i).equals("null")&&storeidList.get(i)!=null){
                storesList.add(storesService.findStkcksum(storeidList.get(i).toString()).getStorename()+"→"+storesService.findStkcksum(storeidList.get(i).toString()).getStoreId());
            }

        }
        final String items[]=storesList.toArray(new String[storesList.size()]);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请选择库区"); //设置标题
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setSingleChoiceItems(items,storeIndex,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                storeIndex=which;
//                Toast.makeText(DeliveryByCarActivity.this, items[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(DeliveryByCarActivity.this,items[storeIndex], Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(mContext,DeliveryByCarDetailsActivity.class);
                intent.putExtra("carname",carname);
                intent.putExtra("shipdate",shipdate);
//                intent.putExtra("shipdate","2017-02-21");
                intent.putExtra("storeId",items[storeIndex].substring(items[storeIndex].indexOf("→")+1,items[storeIndex].length()));
                intent.putExtra("billtype",intent_billtype);
                if (items[storeIndex].substring(items[storeIndex].indexOf("→")+1,items[storeIndex].length()).equals("null")){
                    Toast.makeText(DeliveryByCarActivity.this, "当前库区选择不正确，请同步一下试试", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(intent);
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(DeliveryByCarActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }
}
