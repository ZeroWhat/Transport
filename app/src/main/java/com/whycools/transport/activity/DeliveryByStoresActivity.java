package com.whycools.transport.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.whycools.transport.R;
import com.whycools.transport.adapter.DeliveryByStoresAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Stores;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.service.StoresService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 按库扫描
 * Created by Zero on 2017-02-28.
 */

public class DeliveryByStoresActivity extends BaseActivity implements View.OnClickListener{//
    private LinearLayout delivery_by_stores_layout_back;
    private StoresService storesService;
    private List<Stores> storesList=new ArrayList<>();
    private ListView delivery_by_stores_lv;
    private DeliveryByStoresAdapter adapter;
    private ShipListService shipListService;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delivery_by_stores);
    }

    @Override
    public void initViews() {
        delivery_by_stores_layout_back= findViewById(R.id.delivery_by_stores_layout_back);
        delivery_by_stores_lv= findViewById(R.id.delivery_by_stores_lv);

    }

    @Override
    public void initListeners() {
        delivery_by_stores_layout_back.setOnClickListener(this);
        delivery_by_stores_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DeliveryByStoresActivity.this,DeliveryByStoresDetailsActivity.class).putExtra("StoreId",storesList.get(i).getStoreId()));

            }
        });

    }

    @Override
    public void initData() {
        shipListService=new ShipListService(this);
        storesService=new StoresService(this);
        List storeidList = new ArrayList(new HashSet(shipListService.allStoreid("","","")));
        for (int i = 0; i < storeidList.size(); i++) {
            Log.i(TAG, "仓库id: "+storeidList.get(i).toString());

                storesList.add(storesService.findStkcksum(storeidList.get(i).toString()));


        }

//        storesList=storesService.getAllStkcksum();
        for (int i = 0; i <storesList.size() ; i++) {
            Log.i(TAG, "查询的仓库名称: "+storesList.get(i).getStorename());
        }
        adapter=new DeliveryByStoresAdapter(this,storesList);
        delivery_by_stores_lv.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.delivery_by_stores_layout_back:
                finish();
                break;
        }
    }
}
