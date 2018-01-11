package com.whycools.transport.activity;


import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;

import java.util.List;

/**
 * 按库扫描的详情页
 * Created by Zero on 2017-02-28.
 */

public class DeliveryByStoresDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout delivery_by_stores_details_layout_back;
    private TextView delivery_by_stores_details_tv_context;
    private String StoreId;
    private ShipListService shipListService;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_stores_details);

    }

    @Override
    public void initViews() {
        delivery_by_stores_details_layout_back= findViewById(R.id.delivery_by_stores_details_layout_back);
        delivery_by_stores_details_tv_context= findViewById(R.id.delivery_by_stores_details_tv_context);

    }

    @Override
    public void initListeners() {
        delivery_by_stores_details_layout_back.setOnClickListener(this);

    }

    @Override
    public void initData() {
        StoreId=getIntent().getStringExtra("StoreId");
        Log.i(TAG, "StoreId: "+StoreId);
        shipListService=new ShipListService(this);
        List<ShipList> shipLists=shipListService.findBystoresid(StoreId);
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i <shipLists.size() ; i++) {
            if(shipLists.get(i).getGoodsname().contains("空调")&&!shipLists.get(i).getGoodsname().contains("内机")){
                buffer.append("机器名称："+shipLists.get(i).getGoodsname()+"\r\n");
                buffer.append("出入类型："+shipLists.get(i).getBilltype()+"\r\n");
                buffer.append("日期："+shipLists.get(i).getShipdate()+"\r\n");
                buffer.append("总数:"+(Integer.valueOf(shipLists.get(i).getIn_qty())+Integer.valueOf(shipLists.get(i).getOut_qty()))+"\r\n");
                buffer.append("剩余内机:"+(Integer.valueOf(shipLists.get(i).getIn_qty())-Integer.valueOf(shipLists.get(i).getCin_qty()))+"\r\n");
                buffer.append("剩余外机:"+(Integer.valueOf(shipLists.get(i).getOut_qty())-Integer.valueOf(shipLists.get(i).getCout_qty()))+"\r\n");
                buffer.append("--------------\r\n");
            }else{
                buffer.append("机器名称："+shipLists.get(i).getGoodsname()+"\r\n");
                buffer.append("出入类型："+shipLists.get(i).getBilltype()+"\r\n");
                buffer.append("日期："+shipLists.get(i).getShipdate()+"\r\n");
                buffer.append("总数："+shipLists.get(i).getIn_qty()+"\r\n");
                buffer.append("剩余台数为："+(Integer.valueOf(shipLists.get(i).getIn_qty())-Integer.valueOf(shipLists.get(i).getCin_qty()))+"\r\n");
                buffer.append("--------------\r\n");

            }

        }
        delivery_by_stores_details_tv_context.setText(buffer.toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delivery_by_stores_details_layout_back:
                finish();
                break;
        }
    }
}
