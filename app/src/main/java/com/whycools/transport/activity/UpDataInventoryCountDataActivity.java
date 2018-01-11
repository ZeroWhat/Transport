package com.whycools.transport.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Upstkcksum;
import com.whycools.transport.service.NewStkcksumService;
import com.whycools.transport.service.UpstkcksumService;

/**
 * 修改盘点数据
 * Created by user on 2017-08-25.
 */

public class UpDataInventoryCountDataActivity extends BaseActivity {
    private LinearLayout updatainventorycountdata_back;
    private TextView updatainventorycountdata_tv_goodsname;
    private TextView updatainventorycountdata_tv_number;
    private EditText updatainventorycountdata_ed_number;
    private Button updatainventorycountdata_bt_save;
    private Button updatainventorycountdata_bt_same;
    private Button inventorycountdetails_bt_back;

    private NewStkcksumService newstkcksumservice;
    private UpstkcksumService upstkcksumService;

    private String id,auto_id;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_updatainventorycountdata);
    }

    @Override
    public void initViews() {
        newstkcksumservice=new NewStkcksumService(this);
        upstkcksumService=new UpstkcksumService(this);
        updatainventorycountdata_back= findViewById(R.id.updatainventorycountdata_back);
        updatainventorycountdata_tv_goodsname= findViewById(R.id.updatainventorycountdata_tv_goodsname);
        updatainventorycountdata_tv_number= findViewById(R.id.updatainventorycountdata_tv_number);
        updatainventorycountdata_ed_number= findViewById(R.id.updatainventorycountdata_ed_number);
        updatainventorycountdata_bt_save= findViewById(R.id.updatainventorycountdata_bt_save);
        updatainventorycountdata_bt_same= findViewById(R.id.updatainventorycountdata_bt_same);
        inventorycountdetails_bt_back= findViewById(R.id.inventorycountdetails_bt_back);

    }

    @Override
    public void initListeners() {
        updatainventorycountdata_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upstkcksumService.updateUpStkcksum(updatainventorycountdata_ed_number.getText().toString(),id);
                newstkcksumservice.updateStkcksum2(updatainventorycountdata_ed_number.getText().toString(),auto_id);
                Toast.makeText(UpDataInventoryCountDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
        updatainventorycountdata_bt_same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        inventorycountdetails_bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updatainventorycountdata_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void initData() {
        Intent intent=getIntent();
        id=intent.getStringExtra("_id");
        Upstkcksum upstkcksum=upstkcksumService.findUpStkcksum(id);
        auto_id=upstkcksum.getAuto_id();
        updatainventorycountdata_tv_goodsname.setText("产品名称:"+upstkcksum.getGoodsname());
        updatainventorycountdata_tv_number.setText("库存个数:"+upstkcksum.getQmyereal());
        updatainventorycountdata_ed_number.setText(upstkcksum.getQmyeCount());


    }
}
