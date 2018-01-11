package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.Error;

/**
 * 扫描选项
 * Created by Zero on 2016-12-05.
 */

public class ScanOptionsActivity extends BaseActivity implements View.OnClickListener{
    private ShipListService shipListService;
    private RelativeLayout scan_options_bt_delivery_by_car;//按车布局
    private RelativeLayout scan_options_bt_delivery_by_list;//按单布局
    private RelativeLayout scan_options_bt_dispatch;//调度布局
    private RelativeLayout scan_options_bt_achat;//采购布局
    private RelativeLayout scan_options_bt_free_scan;//自由布局
    private RelativeLayout scan_options_bt_back;//返回布局


    @Override
    public void setContentView() {

            setContentView(R.layout.activity_scan_options);

    }

    @Override
    public void initViews() {

            scan_options_bt_delivery_by_car= findViewById(R.id.scan_options_bt_delivery_by_car);
            scan_options_bt_delivery_by_list= findViewById(R.id.scan_options_bt_delivery_by_list);
            scan_options_bt_dispatch= findViewById(R.id.scan_options_bt_dispatch);
            scan_options_bt_achat= findViewById(R.id.scan_options_bt_achat);
            scan_options_bt_free_scan= findViewById(R.id.scan_options_bt_free_scan);
            scan_options_bt_back= findViewById(R.id.scan_options_bt_back);




    }

    @Override
    public void initListeners() {
            scan_options_bt_delivery_by_car.setOnClickListener(this);
            scan_options_bt_delivery_by_list.setOnClickListener(this);
            scan_options_bt_achat.setOnClickListener(this);
            scan_options_bt_free_scan.setOnClickListener(this);
            scan_options_bt_back.setOnClickListener(this);
            scan_options_bt_dispatch.setOnClickListener(this);



    }

    @Override
    public void initData() {
        shipListService=new ShipListService(ScanOptionsActivity.this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scan_options_bt_delivery_by_car://按车
                showByCar();
                break;
            case R.id.scan_options_bt_delivery_by_list://按单
                showByList();
                break;
            case R.id.scan_options_bt_dispatch://调度
                startActivity(new Intent(ScanOptionsActivity.this,DispatchActivity.class));
                break;
            case R.id.scan_options_bt_achat://采购
                startActivity(new Intent(ScanOptionsActivity.this,AchatActivity.class));
                break;
            case R.id.scan_options_bt_free_scan://自由扫
                startActivity(new Intent(ScanOptionsActivity.this,FreeScanActivity.class));
                break;
            case R.id.scan_options_bt_back://返回按钮
                finish();
                break;


        }
    }

    private void  showByCar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanOptionsActivity.this);
        builder.setTitle("按车出入");
        //    指定下拉列表的显示数据
        final String[] cities = {"按车发货→共"+shipListService.getByCar()+"条", "按车入库→共"+shipListService.getByCarIn()+"条"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,  int i) {
                switch (i){
                    case 0:
                        Intent intent_delicery_by_car=new Intent(ScanOptionsActivity.this,DeliveryByCarActivity.class);
                        intent_delicery_by_car.putExtra("billtype","出");
                        startActivity(intent_delicery_by_car);
                        break;
                    case 1:
                        Intent intent_delicery_in_by_car=new Intent(ScanOptionsActivity.this,DeliveryByCarActivity.class);
                        intent_delicery_in_by_car.putExtra("billtype","入");
                        startActivity(intent_delicery_in_by_car);
                        break;

                }
            }
        });
        builder.show();

    }
    //按单
    private void showByList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanOptionsActivity.this);
        builder.setTitle("按单→共"+shipListService.getByList()+"条");
        //    指定下拉列表的显示数据
        final String[] cities = {"以订单形式展示", "以列表形式展示","快递单扫描"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,  int i) {
                switch (i){
                    case 0:
                        Intent intent_delicery_by_list_order=new Intent(mContext,DeliveryByListOrderActivity.class);
                        startActivity(intent_delicery_by_list_order);
                        break;
                    case 1:
                        Intent intent_delicery_by_list=new Intent(mContext,DeliveryByListActivity.class);
                        startActivity(intent_delicery_by_list);
                        break;
                    case 2:
                        Intent intent_express_scan=new Intent(mContext,ExpressScanActivity.class);
                        startActivity(intent_express_scan);
                        break;
                }
            }
        });
        builder.show();
    }
}
