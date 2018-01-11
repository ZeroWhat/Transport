package com.whycools.transport.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.crash.util.DateUtils;
import com.whycools.transport.entity.Stores;
import com.whycools.transport.service.StoresService;
import com.whycools.transport.text.ShiplistTextActivity;
import com.whycools.transport.utils.DataReset;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设置页面
 * Created by Zero on 2016-12-05.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private String url="";//服务器地址
    private LinearLayout layout_set_back;//返回按钮
    private EditText setting_et_server_address;//服务器地址输入框
    private Button setting_bt_server_address;//服务器地址设置按钮
    private Spinner setting_sp_equipment;//设备
    private Button setting_bt_uplog;//上传日志按钮
    private Button setting_bt_datareset;//数据重置按钮
    private Button setting_bt_finish;//完成按钮

    private Button setting_bt_text;
    private StoresService storesservice;
    private CheckBox setting_cb_scan;
    private Button bt_clerar;

    private ArrayAdapter<String> sp_warehouse_adapter;//仓库适配器

    List<String>  sp_warehouse_data= new ArrayList<String>();
    @Override
    public void setContentView() {
        try {
            setContentView(R.layout.activity_setting);
        }catch (Exception e){
            Error.contentToTxt(SettingActivity.this,"设置页面启动异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initViews() {
        try {
            layout_set_back= findViewById(R.id.layout_set_back);
            setting_et_server_address= findViewById(R.id.setting_et_server_address);
            setting_bt_server_address= findViewById(R.id.setting_bt_server_address);
            setting_sp_equipment= findViewById(R.id.setting_sp_equipment);
            setting_bt_uplog= findViewById(R.id.setting_bt_uplog);
            setting_bt_datareset= findViewById(R.id.setting_bt_datareset);
            setting_bt_finish= findViewById(R.id.setting_bt_finish);
            setting_cb_scan= findViewById(R.id.setting_cb_scan);


            setting_bt_text= findViewById(R.id.setting_bt_text);
            bt_clerar= findViewById(R.id.bt_clerar);


        }catch (Exception e){
            Error.contentToTxt(SettingActivity.this,"设置页面控件实例化异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initListeners() {
        try {
            layout_set_back.setOnClickListener(this);
            setting_bt_server_address.setOnClickListener(this);
            setting_bt_uplog.setOnClickListener(this);
            setting_bt_datareset.setOnClickListener(this);
            setting_bt_finish.setOnClickListener(this);
            setting_bt_text.setOnClickListener(this);
            bt_clerar.setOnClickListener(this);
            String isscan= SharedPreferencesUtil.getData(SettingActivity.this,"isscan","1").toString();
            if (isscan.equals("1")){
                setting_cb_scan.setChecked(true);
            }else {
                setting_cb_scan.setChecked(false);
            }
            setting_cb_scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){
                        SharedPreferencesUtil.saveData(SettingActivity.this,"scan","1");
                        SharedPreferencesUtil.saveData(SettingActivity.this,"isscan","1");
//                        Toast.makeText(SettingActivity.this, "我被选中了", Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferencesUtil.saveData(SettingActivity.this,"scan","0");
                        SharedPreferencesUtil.saveData(SettingActivity.this,"isscan","0");
//                        Toast.makeText(SettingActivity.this, "没有选中我", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //设备下拉框监听器
            setting_sp_equipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    //拿到被选择项的值
                    String str = (String) setting_sp_equipment.getSelectedItem();
                    //toast展示
//                Toast.makeText(SettingActivity.this, str, Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveData(SettingActivity.this,"equipmentposition",position);
                    Log.i(TAG, "设备存储第: " +position+"个");
                    //设置默认值
                    setting_sp_equipment.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });



        }catch (Exception e){
            Error.contentToTxt(SettingActivity.this,"设置页面控件按钮监听异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initData() {
        try {
            storesservice=new StoresService(mContext);
            setting_et_server_address.setText(SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString());
            Selection.setSelection(setting_et_server_address.getText(), setting_et_server_address.getText().length());//光标放在末尾
            url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //数据库读取数据
                    List<Stores> list=storesservice.getAllStkcksum();

                    for (int i = 0; i < list.size(); i++) {
                        Log.i("仓库数据库数据", ">>>>>>>>>>>: "+list.get(i).getStoreId());
                        sp_warehouse_data.add(list.get(i).getStoreId());
                    }
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);


                }
            }.start();

        }catch (Exception e){
            Error.contentToTxt(SettingActivity.this,"设置页面数据加载异常"+e.getMessage());//异常写入文档

        }

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    sp_warehouse_adapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, sp_warehouse_data);
                    //仓库适配器设置样式
                    sp_warehouse_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //仓库下拉框加载适配器
                    setting_sp_equipment.setAdapter(sp_warehouse_adapter);


                    int equipmentposition= Integer.parseInt(SharedPreferencesUtil.getData(SettingActivity.this,"equipmentposition",0).toString());
                    setting_sp_equipment.setSelection(equipmentposition);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_set_back://返回按钮
                finish();
                break;
            case R.id.setting_bt_server_address://服务器设置按钮
                String server_address=	setting_et_server_address.getText().toString();

                if (server_address.endsWith("/")){
                    SharedPreferencesUtil.saveData(mContext,"ServerAddress",server_address);
                    Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                }else{
                    TransparentDialogUtil.showErrorMessage(mContext,"格式不正确，最后一位必须为‘/'");
                }
                break;
            case R.id.setting_bt_uplog://日志上传按钮
                if (!isNetworkAvailable(mContext)){
                    TransparentDialogUtil.showErrorMessage(mContext,NET_NOUSE);
                    return;
                }
                sendjlCircle();
                break;
            case R.id.setting_bt_datareset://数据重置按钮
                if (!isNetworkAvailable(mContext)){
                    TransparentDialogUtil.showErrorMessage(mContext,NET_NOUSE);
                    return;
                }
                String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                new DataReset(mContext,url);
                break;
            case R.id.setting_bt_finish://完成按钮
                finish();

                break;
            case R.id.setting_bt_text:
                startActivity(new Intent(SettingActivity.this,ShiplistTextActivity.class));
                break;
            case R.id.bt_clerar:
                clear();
                break;
        }
    }

    /**
     * 上传错误的log日志
     */
    private void sendjlCircle( ) {

        String path =Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + getserialnumber()+ DateUtils
                .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
        Log.i(TAG, "路径path: "+path);
        File file= new File(path);
        String lid="1c827923-620b-47be-a8c7-7b34d87d705f";
        RequestParams params = new RequestParams();
        params.addBodyParameter("attach", file, "txt");
        String url_serveraddress= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
        //http://10.10.10.2:89/rent/simpleUpload
        String url=url_serveraddress+"update/simpleUpload.jsp?path=applog";
        uploadMethod(params,url);
    }
    private void uploadMethod(RequestParams params, String uploadHost) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        //开始上传
   					TransparentDialogUtil.showLoadingMessage(mContext,"开始上传",true);
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        //上传中
                        TransparentDialogUtil.showLoadingMessage(mContext,"上传中",true);
                    }

                    @Override
                    public void onSuccess(
                            ResponseInfo<String> responseInfo) {
                        //上传成功
                        TransparentDialogUtil.showSuccessMessage(mContext,"上传成功");
//                        try {
//                            String path =Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + DateUtils
//                                    .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
//                            File f = new File(path);
//                            FileWriter fw =  new FileWriter(f);
//                            fw.write("");
//
//                            fw.close();
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                           Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HttpException error,
                                          String msg) {
                        //上传失败
                        TransparentDialogUtil.showSuccessMessage(mContext,"上传失败");
                    }
                });

    }
    private void clear(){
        long preAvaiMemory = getAvailMemory(SettingActivity.this);//加速之前的系统内存

        Context context = SettingActivity.this;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取后台运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        //获取本项目的进程
        String currentProcess = context.getApplicationInfo().processName;

        /*也可以设置需要清理的后台程序的importance的程度
        * if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
        */

        //对系统中所有正在运行的进程进行迭代，如果进程名不是当前进程，则Kill掉
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {

            String processName = appProcessInfo.processName;
            //取得各个进程的包
            String[] pkgList = appProcessInfo.pkgList;
            if (!processName.equals(currentProcess)) {
                for (int i = 0; i < pkgList.length; ++i) {
                    activityManager.killBackgroundProcesses(pkgList[i]);
                }
            }
        }

        long newAvaiMemory = getAvailMemory(SettingActivity.this);//加速之后的系统内存
        Toast.makeText(getApplicationContext(), "为您节省了" + (newAvaiMemory - preAvaiMemory) + "M内存", Toast.LENGTH_SHORT).show();
        //用完即关闭，为下次打开能够顺利开启
        finish();
    }
    /*获取可用内存大小*/
    private long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / (1024 * 1024);//返回值以 M 为单位
    }
}
