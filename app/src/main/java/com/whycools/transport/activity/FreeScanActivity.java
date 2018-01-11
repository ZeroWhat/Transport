package com.whycools.transport.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Stores;
import com.whycools.transport.service.StoresService;
import com.whycools.transport.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自由扫页面
 * Created by Zero on 2016-12-07.
 */

public class FreeScanActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout freescan_back;//扫描返回
    private EditText freescan_et_billno;//发货编号输入框
    private Button freescan_bt_scan_once;//扫一次保存一次按钮
    private CheckBox freescan_cb_billtype_out;
    private CheckBox freescan_cb_billtype_in;
    private int in_or_out = 1;
    private int isin = 1;
    private Spinner freescan_sp;
    private StoresService storesservice;
    private ArrayAdapter<String> sp_freescan_adapter;//仓库适配器
    List<String> sp_freescan_data = new ArrayList<String>();

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_freescan);


    }

    @Override
    public void initViews() {


        freescan_back = findViewById(R.id.freescan_back);
        freescan_et_billno = findViewById(R.id.freescan_et_billno);
        freescan_bt_scan_once = findViewById(R.id.freescan_bt_scan_once);
        freescan_cb_billtype_out = findViewById(R.id.freescan_cb_billtype_out);
        freescan_cb_billtype_in = findViewById(R.id.freescan_cb_billtype_in);
        freescan_sp = findViewById(R.id.freescan_sp);


    }

    @Override
    public void initListeners() {

        freescan_back.setOnClickListener(this);
        freescan_bt_scan_once.setOnClickListener(this);
        freescan_cb_billtype_out.setChecked(true);
        freescan_cb_billtype_out.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    in_or_out = 1;
                    freescan_cb_billtype_in.setChecked(false);
                } else {
                    freescan_cb_billtype_in.setChecked(true);
                }
            }
        });
        freescan_cb_billtype_in.setChecked(false);
        freescan_cb_billtype_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    Toast.makeText(mContext, b?"选中了":"取消了选中"    , Toast.LENGTH_LONG).show();
                if (b) {
                    freescan_cb_billtype_out.setChecked(false);
                    in_or_out = 2;
                } else {
                    freescan_cb_billtype_out.setChecked(true);
                }
            }
        });


    }

    @Override
    public void initData() {

        storesservice = new StoresService(mContext);

        new Thread() {
            @Override
            public void run() {
                super.run();
                //数据库读取数据
                sp_freescan_data.clear();
                List<Stores> list = storesservice.getAllStkcksum();
                sp_freescan_data.add("张华浜整机库→214");
                for (int i = 0; i < list.size(); i++) {
                    Log.i("仓库数据库数据", ">>>>>>>>>>>: " + list.get(i).getStoreId());
                    sp_freescan_data.add(list.get(i).getStorename() + "→" + list.get(i).getStoreId());
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    sp_freescan_adapter = new ArrayAdapter<String>(mContext, R.layout.adapter_freescan_spinner, sp_freescan_data);
                    //仓库适配器设置样式
                    sp_freescan_adapter.setDropDownViewResource(R.layout.adapter_freescan_spinner_item);

                    //仓库下拉框加载适配器
                    freescan_sp.setAdapter(sp_freescan_adapter);


                    int equipmentposition = Integer.parseInt(SharedPreferencesUtil.getData(FreeScanActivity.this, "equipmentposition", 0).toString());
                    freescan_sp.setSelection(equipmentposition);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.freescan_back:
                finish();
                break;
            case R.id.freescan_bt_scan_once:
                if (freescan_et_billno.getText().length() <= 0) {
                    Toast.makeText(mContext, "请输入物品编号", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_scanone = new Intent(mContext, ScanOnceActivity.class);
                    intent_scanone.putExtra("billno", freescan_et_billno.getText().toString());
                    intent_scanone.putExtra("in_or_out", String.valueOf(in_or_out));
                    intent_scanone.putExtra("stores", freescan_sp.getSelectedItem().toString());
                    startActivity(intent_scanone);
                }
                break;
        }
    }
}
