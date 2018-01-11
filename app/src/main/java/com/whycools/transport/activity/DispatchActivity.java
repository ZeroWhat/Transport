package com.whycools.transport.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.adapter.AchatListViewCursorAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.TransparentDialogUtil;

/**
 * 调度
 * Created by Zero on 2017-03-29.
 */

public class DispatchActivity extends BaseActivity implements View.OnClickListener{//
    private LinearLayout dispatch_layout_back;
    private ListView dispatch_lv;
    private TextView dispatch_et_search;
    private DBinventory dDBinventory;
    private AchatListViewCursorAdapter adapter;//适配器
    private Cursor cursor;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_dispatch);
    }

    @Override
    public void initViews() {
        dispatch_layout_back= findViewById(R.id.dispatch_layout_back);
        dispatch_lv= findViewById(R.id.dispatch_lv);
        dispatch_et_search= findViewById(R.id.dispatch_et_search);
    }

    @Override
    public void initListeners() {
        dispatch_layout_back.setOnClickListener(this);

    }

    @Override
    public void initData() {
        loadData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dispatch_layout_back:
                finish();
                break;
        }
    }
    private void showListView() {
        dDBinventory= new DBinventory(mContext);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        cursor = db.rawQuery("select * from shiplist where billno like '%" + dispatch_et_search.getText() + "%' and billtype=? and inoutFlag=? and billno<>''", new String[]{"调","-1"});
        Log.i(TAG, "cursor个数2: "+cursor.getCount());

        adapter=new AchatListViewCursorAdapter(mContext, cursor);
        dispatch_lv.setAdapter(adapter);
        dispatch_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                Intent intent=new Intent(mContext,DispatchDetailsActivity.class);
                intent.putExtra("_id",_id);
                startActivity(intent);
            }
        });

    }

    private void loadData(){
        dDBinventory=new DBinventory(DispatchActivity.this);
        SQLiteDatabase db = dDBinventory.getReadableDatabase();
        cursor = db.rawQuery("select * from shiplist where billtype=? and inoutFlag=? and billno<>''", new String[]{"调","-1"});
        Log.i(TAG, "cursor个数: "+cursor.getCount());
        if (cursor.getCount()>0){
            adapter=new AchatListViewCursorAdapter(mContext, cursor);
            dispatch_lv.setAdapter(adapter);
            dispatch_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);
                    String _id = cursor.getString(cursor.getColumnIndex("_id"));
                    Intent intent=new Intent(mContext,DispatchDetailsActivity.class);
                    intent.putExtra("_id",_id);
                    startActivity(intent);
                }
            });

        }else{
            TransparentDialogUtil.showErrorMessage(mContext,"当前没有数据，同步一下试试");
        }


        dispatch_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0){
                    showListView();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
