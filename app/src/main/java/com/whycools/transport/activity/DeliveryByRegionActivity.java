package com.whycools.transport.activity;

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

import com.whycools.transport.R;
import com.whycools.transport.adapter.DeliveryByRegionAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 按区发货
 * Created by Zero on 2017-02-15.
 */

public class DeliveryByRegionActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout delivery_by_region_layout_back;
    private Spinner delivery_by_region_sp_shipdate;
    private ListView delivery_by_region_lv;
    private DeliveryByRegionAdapter adapter;
    private List<ShipList> DeliveryByRegionData=new ArrayList<>();

    private ShipListService shiplistservice;

    private List<String> district=new ArrayList<>();
    private ArrayAdapter<String> shipdateadapter;//日期适配器

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_region);

    }

    @Override
    public void initViews() {
        delivery_by_region_layout_back= findViewById(R.id.delivery_by_region_layout_back);
        delivery_by_region_sp_shipdate= findViewById(R.id.delivery_by_region_sp_shipdate);

        delivery_by_region_lv= findViewById(R.id.delivery_by_region_lv);
        shiplistservice=new ShipListService(DeliveryByRegionActivity.this);

    }

    @Override
    public void initListeners() {
        delivery_by_region_layout_back.setOnClickListener(this);
        delivery_by_region_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DeliveryByRegionActivity.this,DeliveryByRegionDetailsActivity.class).putExtra("district",DeliveryByRegionData.get(i).getDistrict()).putExtra("shipdate",delivery_by_region_sp_shipdate.getSelectedItem().toString()));
            }
        });

        delivery_by_region_sp_shipdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadData(delivery_by_region_sp_shipdate.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void initData() {
//        district.add("黄浦区");
//        district.add("徐汇区");
//        district.add("长宁区");
//        district.add("静安区");
//        district.add("普陀区");
//        district.add("虹口区");
//        district.add("杨浦区");
//        district.add("闵行区");
//        district.add("宝山区");
//        district.add("嘉定区");
//        district.add("浦东新区");
//        district.add("金山区");
//        district.add("松江区");
//        district.add("青浦区");
//        district.add("奉贤区");

        district.add(DateUtile.getdata(0));
        district.add(DateUtile.getdata(1));
        district.add(DateUtile.getdata(2));
        shipdateadapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, district);
        delivery_by_region_sp_shipdate.setAdapter(shipdateadapter);
        delivery_by_region_sp_shipdate.setSelection(0);
        loadData(delivery_by_region_sp_shipdate.getSelectedItem().toString());

//
//        DeliveryByRegionData=shiplistservice.getshiplistcarid("2017-02-16");
//        for ( int i = 0 ; i < DeliveryByRegionData.size() - 1 ; i ++ ) {
//            for ( int j = DeliveryByRegionData.size() - 1 ; j > i; j -- ) {
//                if (DeliveryByRegionData.get(j).getDistrict().equals(DeliveryByRegionData.get(i).getDistrict())) {
//                    DeliveryByRegionData.remove(j);
//                }
//            }
//        }
//        adapter=new DeliveryByRegionAdapter(mContext,DeliveryByRegionData);
//        delivery_by_region_lv.setAdapter(adapter);

    }

    private void loadData(final String shipdate){
        new Thread(){
            @Override
            public void run() {
                super.run();
                DeliveryByRegionData.clear();
                //DeliveryByRegionData=shiplistservice.getshiplistcarid(shipdate);
                String result="";
                if (DeliveryByRegionData.size()>0){
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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.dismiss();
                    String result = msg.getData().getString("result");
                    TransparentDialogUtil.showInfoMessage(DeliveryByRegionActivity.this,result);
                    for (int i = 0; i < DeliveryByRegionData.size(); i++) {
                        Log.i(TAG, "handleMessage: "+DeliveryByRegionData.get(i).getDistrict());
                    }
                    for ( int i = 0 ; i < DeliveryByRegionData.size() - 1 ; i ++ ) {
                        for ( int j = DeliveryByRegionData.size() - 1 ; j > i; j -- ) {
                            if (DeliveryByRegionData.get(j).getDistrict().equals(DeliveryByRegionData.get(i).getDistrict())) {
                                DeliveryByRegionData.remove(j);
                            }
                        }
                    }
                    adapter=new DeliveryByRegionAdapter(DeliveryByRegionActivity.this,DeliveryByRegionData);
                    delivery_by_region_lv.setAdapter(adapter);
                    break;
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delivery_by_region_layout_back:
                finish();
                break;
        }
    }
}
