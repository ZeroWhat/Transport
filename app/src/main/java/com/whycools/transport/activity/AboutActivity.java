package com.whycools.transport.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;

/**
 * 关于我们
 * Created by Administrator on 2018-01-10.
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout about_layout_back;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_about);


    }

    @Override
    public void initViews() {
        about_layout_back= findViewById(R.id.about_layout_back);

    }

    @Override
    public void initListeners() {
        about_layout_back.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_layout_back:
                finish();
                break;
        }
    }
}
