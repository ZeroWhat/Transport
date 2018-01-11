package com.whycools.transport.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.ScanRec;
import com.whycools.transport.service.ScanlistService;

/**
 *修改详情页
 * Created by Zero on 2017-03-14.
 */

public class DeleteScanListDetailsActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout delete_scanlist_details_layout_back;
    private EditText delete_scanlist_details_billno_ed;
    private EditText delete_scanlist_details_goodid_ed;
    private EditText delete_scanlist_details_barcodes_ed;
    private EditText delete_scanlist_details_isin_ed;
    private Button delete_scanlist_details_updata_bt;
    private ScanlistService scanlistService;
    private String _id;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_delete_scanlist_details);


    }

    @Override
    public void initViews() {
        delete_scanlist_details_layout_back= findViewById(R.id.delete_scanlist_details_layout_back);
        delete_scanlist_details_billno_ed= findViewById(R.id.delete_scanlist_details_billno_ed);
        delete_scanlist_details_goodid_ed= findViewById(R.id.delete_scanlist_details_goodid_ed);
        delete_scanlist_details_barcodes_ed= findViewById(R.id.delete_scanlist_details_barcodes_ed);
        delete_scanlist_details_isin_ed= findViewById(R.id.delete_scanlist_details_isin_ed);
        delete_scanlist_details_updata_bt= findViewById(R.id.delete_scanlist_details_updata_bt);

    }

    @Override
    public void initListeners() {
        delete_scanlist_details_layout_back.setOnClickListener(this);
        delete_scanlist_details_updata_bt.setOnClickListener(this);

    }

    @Override
    public void initData() {
        scanlistService=new ScanlistService(DeleteScanListDetailsActivity.this);
        _id=getIntent().getStringExtra("_id");
        ScanRec scanRec = scanlistService.findScanlist(_id);
        delete_scanlist_details_billno_ed.setText(scanRec.getBillno());
        delete_scanlist_details_goodid_ed.setText(scanRec.getGoodsId());
        delete_scanlist_details_barcodes_ed.setText(scanRec.getBarcodes());
        delete_scanlist_details_isin_ed.setText(scanRec.getIsInCode());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_scanlist_details_layout_back:
                finish();
                break;
            case R.id.delete_scanlist_details_updata_bt:
                ScanRec scanRec =new ScanRec();
                scanRec.set_id(_id);
                scanRec.setBillno(delete_scanlist_details_billno_ed.getText().toString());
                scanRec.setGoodsId(delete_scanlist_details_goodid_ed.getText().toString());
                scanRec.setBarcodes(delete_scanlist_details_barcodes_ed.getText().toString());
                scanRec.setIsInCode(delete_scanlist_details_isin_ed.getText().toString());
                scanlistService.updateScanlist(scanRec);
                finish();
                break;
        }
    }
}
