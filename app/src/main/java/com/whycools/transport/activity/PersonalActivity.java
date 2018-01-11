package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whycools.transport.MainActivity;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.VersionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 个人中心页面
 * Created by Administrator on 2017-12-29.
 */

public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout personal_back;
    private TextView personal_username;//当前登录的用户名称
    private Button personal_cancell;//注销按钮
    private RelativeLayout personal_item_setting;//设置按钮
    private RelativeLayout personal_item_chake_updata;//版本更新检查按钮
    private RelativeLayout personal_item_about;//关于软件按钮
    private TextView personal_item_tv_updata;//版本号
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_personal);
    }

    @Override
    public void initViews() {
        personal_back= findViewById(R.id.personal_back);
        personal_username= findViewById(R.id.personal_username);
        personal_cancell= findViewById(R.id.personal_cancell);
        personal_item_setting= findViewById(R.id.personal_item_setting);
        personal_item_chake_updata= findViewById(R.id.personal_item_chake_updata);
        personal_item_about= findViewById(R.id.personal_item_about);
        personal_item_tv_updata= findViewById(R.id.personal_item_tv_updata);
    }

    @Override
    public void initListeners() {
        personal_back.setOnClickListener(this);
        personal_cancell.setOnClickListener(this);
        personal_item_setting.setOnClickListener(this);
        personal_item_chake_updata.setOnClickListener(this);
        personal_item_about.setOnClickListener(this);

    }

    @Override
    public void initData() {
        String username=SharedPreferencesUtil.getData(PersonalActivity.this,"username","").toString();
        personal_username.setText(username);
        personal_item_tv_updata.setText(getVersionName()+getVersionCode());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personal_back:
                finish();
                break;
            case R.id.personal_cancell:
                //清空储存userid 跳转到登录页面
                SharedPreferencesUtil.saveData(mContext,"userid","");
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.personal_item_setting:
                startActivity(new Intent(PersonalActivity.this, SettingActivity.class));
                break;
            case R.id.personal_item_chake_updata:
                updata();
                break;
            case R.id.personal_item_about:
                startActivity(new Intent(PersonalActivity.this, AboutActivity.class));
                break;
        }
    }

    /**
     * 版本更新
     */
    private void updata(){
        if (!isNetworkAvailable(mContext)){
            TransparentDialogUtil.showErrorMessage(mContext,NET_NOUSE);
            return;
        }
        TransparentDialogUtil.showLoadingMessage(PersonalActivity.this,"正在连接中...",false);
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString()+"whycools/getFileVersion.jsp?filename=whycools.apk";
                Log.i(TAG, "版本号的接口: "+url);
                String code= RequestData.HttpGet(url);
                Log.i(TAG, "版本号code: "+replaceBlank(code));
                if (replaceBlank(code).length()>0&&isNumeric(replaceBlank(code))){
                    Message msg=new Message();
                    msg.what=1;
                    Bundle bundle=new Bundle();
                    bundle.putString("code",replaceBlank(code));
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }else{
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                }

            }
        }.start();

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int code=Integer.valueOf(msg.getData().getString("code"));
                    if(code<=getVersionCode( )){
                        TransparentDialogUtil.showInfoMessage(PersonalActivity.this,"已经是最新版本了");
                    }else{

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString()+"whycools/getFileVersionDetailJson.jsp?filename=whycools.apk";
                                Log.i(TAG, "更新接口run: "+url);
                                String updateresult=RequestData.HttpGet(url);
                                Log.i(TAG, "updateresult: "+updateresult);
                                String reviseText="";
                                try {
                                    JSONObject obj=new JSONObject(updateresult);
                                    JSONArray array=obj.getJSONArray("results");
                                    Log.i(TAG, "array个数: "+array.length());
                                    for (int i = 0; i <1 ; i++) {
                                        JSONObject obj2=array.getJSONObject(i);
                                        reviseText=obj2.getString("reviseText");
                                        Log.i(TAG, "run: "+reviseText);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Message msg=new Message();
                                msg.what=3;
                                Bundle bundle=new Bundle();
                                bundle.putString("reviseText",reviseText);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }.start();

                    }
                    break;
                case 2:
                    TransparentDialogUtil.showErrorMessage(PersonalActivity.this,"更新失败");
                    break;
                case 3:
                    TransparentDialogUtil.dismiss();
                    String reviseText=msg.getData().getString("reviseText");
                    showUpdataDialog(reviseText);
                    break;
            }
        }
    };
    //更新对话框
    protected void showUpdataDialog(String reviseText) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("发现新版本");
        builer.setMessage(reviseText);
        // 当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Log.i(TAG,"下载apk,更新");
                        downLoadApk();
                    }
                });
        // 当点取消按钮时进行登录
        builer.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        LoginMain();
                    }
                });
        AlertDialog  dialog = builer.create();
        dialog.show();
    }
    //下载app文件
    public void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    String url_serveraddress= SharedPreferencesUtil.getData(mContext,"ServerAddress", SERVERDDRESS)+"";
                    String url=url_serveraddress+"update/whycools.apk";
                    Log.i(TAG, "版本更新url: "+url);
                    File file = VersionUtil.getFileFromServer(url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = -1;
                }
            }
        }.start();
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),  "application/vnd.android.package-archive");// 注意：android不可改为大写,Android
        startActivity(intent);
    }
}
