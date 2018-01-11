package com.whycools.transport.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.whycools.transport.R;
import com.whycools.transport.adapter.FragmentAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.fragment.FinishShipListFragment;
import com.whycools.transport.fragment.NoFinishShipListFragment;
import com.whycools.transport.fragment.NoScrollViewPager;
import com.whycools.transport.utils.Error;

import java.util.ArrayList;
import java.util.List;

/**
 * 按单发送
 * Created by Zero on 2016-12-06.
 */

public class DeliveryByListActivity extends BaseActivity implements View.OnClickListener{
    private FinishShipListFragment finishShipListFragment;//完成
    private NoFinishShipListFragment noFinishShipListFragment;//未完成

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();//fragment集合
    private FragmentAdapter mFragmentAdapter;//fragment是适配器
    private NoScrollViewPager delivery_by_list_vp;//控件vp
    private Button delicery_by_list_top_bt_nofinish;//未完成按钮
    private Button delicery_by_list_top_bt_finish;//完成按钮
    private LinearLayout delivery_by_list_layout_back;//返回按钮

    @Override
    public void setContentView() {

        try {
            setContentView(R.layout.activity_delivery_by_list);
        }catch (Exception e){
            Error.contentToTxt(DeliveryByListActivity.this,"按单发货页面启动异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initViews() {
        try {
            delivery_by_list_vp= findViewById(R.id.delivery_by_list_vp);
            delicery_by_list_top_bt_nofinish= findViewById(R.id.delicery_by_list_top_bt_nofinish);
            delicery_by_list_top_bt_finish= findViewById(R.id.delicery_by_list_top_bt_finish);
            delivery_by_list_layout_back= findViewById(R.id.delivery_by_list_layout_back);
        }catch (Exception e){
            Error.contentToTxt(DeliveryByListActivity.this,"按单发货页面控件实例化异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initListeners() {
        try {
            delicery_by_list_top_bt_nofinish.setOnClickListener(this);
            delicery_by_list_top_bt_finish.setOnClickListener(this);
            delivery_by_list_layout_back.setOnClickListener(this);
        }catch (Exception e){
            Error.contentToTxt(DeliveryByListActivity.this,"按单发货页面控件按钮监听异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initData() {

        try {
            finishShipListFragment=new FinishShipListFragment();
            noFinishShipListFragment=new NoFinishShipListFragment();
            mFragmentList.add(noFinishShipListFragment);
            mFragmentList.add(finishShipListFragment);
            mFragmentAdapter = new FragmentAdapter(
                    this.getSupportFragmentManager(), mFragmentList);
            delivery_by_list_vp.setAdapter(mFragmentAdapter);
            delivery_by_list_vp.setCurrentItem(0);
            delicery_by_list_top_bt_nofinish.setTextColor(Color.parseColor("#303F9F"));
            delicery_by_list_top_bt_finish.setTextColor(Color.parseColor("#000000"));
        }catch (Exception e){
            Error.contentToTxt(DeliveryByListActivity.this,"按单发货页面数据加载异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delicery_by_list_top_bt_nofinish:
                delicery_by_list_top_bt_nofinish.setTextColor(Color.parseColor("#303F9F"));
                delicery_by_list_top_bt_finish.setTextColor(Color.parseColor("#000000"));
                delivery_by_list_vp.setCurrentItem(0);
                break;
            case R.id.delicery_by_list_top_bt_finish:
                delicery_by_list_top_bt_finish.setTextColor(Color.parseColor("#303F9F"));
                delivery_by_list_vp.setCurrentItem(1);
                delicery_by_list_top_bt_nofinish.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.delivery_by_list_layout_back:
                finish();
                break;
        }
    }
}
