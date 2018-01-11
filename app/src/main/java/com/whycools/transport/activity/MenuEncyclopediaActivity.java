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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.whycools.transport.MainActivity;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.DataSync;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.VersionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 客运司机的主页界面
 * Created by Zero on 2016-12-05.
 */

public class MenuEncyclopediaActivity extends BaseActivity implements View.OnClickListener{
    private CircleTextImageView menu_encyclopedia_head;//个人中心头像
    private ImageView menu_encyclopedia_bt_scan;//扫描按钮
    private ImageView menu_encyclopedia_bt_sync;//同步按钮
    private ImageView menu_encyclopedia_bt_inventory;//盘点按钮
    private ImageView menu_encyclopedia_bt_updata;//数据上传按钮
    private  AlertDialog  dialog;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_menu_encyclopedia);
    }

    @Override
    public void initViews() {

            menu_encyclopedia_head= findViewById(R.id.menu_encyclopedia_head);
            menu_encyclopedia_bt_scan= findViewById(R.id.menu_encyclopedia_bt_scan);
            menu_encyclopedia_bt_sync= findViewById(R.id.menu_encyclopedia_bt_sync);
            menu_encyclopedia_bt_inventory= findViewById(R.id.menu_encyclopedia_bt_inventory);
            menu_encyclopedia_bt_updata= findViewById(R.id.menu_encyclopedia_bt_updata);



    }

    @Override
    public void initListeners() {
            menu_encyclopedia_head.setOnClickListener(this);
            menu_encyclopedia_bt_scan.setOnClickListener(this);
            menu_encyclopedia_bt_sync.setOnClickListener(this);
            menu_encyclopedia_bt_inventory.setOnClickListener(this);
            menu_encyclopedia_bt_updata.setOnClickListener(this);

    }

    @Override
    public void initData() {
        if (isLogin()){
            startActivity(new Intent(MenuEncyclopediaActivity.this, LoginActivity.class));
            finish();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_encyclopedia_head:
                startActivity(new Intent(MenuEncyclopediaActivity.this, PersonalActivity.class));
                break;
            case R.id.menu_encyclopedia_bt_scan://扫描按钮
                Intent intent_scanoptions=new Intent(MenuEncyclopediaActivity.this,ScanOptionsActivity.class);
                startActivity(intent_scanoptions);
                break;
            case R.id.menu_encyclopedia_bt_sync://同步按钮
                if (!isNetworkAvailable(mContext)){
                    TransparentDialogUtil.showErrorMessage(MenuEncyclopediaActivity.this,NET_NOUSE);
                    return;
                }
                String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                new DataSync(MenuEncyclopediaActivity.this,url,getserialnumber());
                break;
            case R.id.menu_encyclopedia_bt_updata://数据上传
                showUpdata();
                break;
            case R.id.menu_encyclopedia_bt_inventory://盘点按钮
                Intent intent_check=new Intent(mContext,InventoryCountActivity.class);
                startActivity(intent_check);
                break;

        }
    }

    // 记录退出时间时候使用
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                        Toast.LENGTH_SHORT).show();
                // 记录当前时间，如果是在两秒内
                exitTime = System.currentTimeMillis();
            } else {
                // 退出代码
                finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断是否登录了吗，如果没有登录直接跳到登录页面
     */
    private boolean   isLogin(){
        String userid=SharedPreferencesUtil.getData(mContext,"userid",SERVERDDRESS).toString();
        return !(userid != null && userid.length() > 0 && isNumeric(userid));

    }
    //数据上传选择框
    private void showUpdata(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuEncyclopediaActivity.this);
        builder.setTitle("数据上传");
        //    指定下拉列表的显示数据
        final String[] cities = {"扫描条码上传", "盘点数据上传"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,  int i) {
                switch (i){
                    case 0:
                        Intent intent_delicery_by_list_order=new Intent(MenuEncyclopediaActivity.this,DeleteScanListActivity.class);
                        startActivity(intent_delicery_by_list_order);
                        break;
                    case 1:
                        Intent intent_delicery_by_list=new Intent(MenuEncyclopediaActivity.this,UpInventoryCountDataActivity.class);
                        startActivity(intent_delicery_by_list);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updataApp();
        Log.i(TAG, "onResume: app版本更新----------------------");
    }

    //app更新
    public  void updataApp(){
        if (!isNetworkAvailable(mContext)){
            return;
        }
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
                                msg.what=2;
                                Bundle bundle=new Bundle();
                                bundle.putString("reviseText",reviseText);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }.start();

                    }
                    break;
                case 2:
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
        dialog = builer.create();
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

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog!=null){
            dialog.dismiss();
        }
    }
}
