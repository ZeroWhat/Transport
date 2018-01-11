package com.whycools.transport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.whycools.transport.R;
import com.whycools.transport.adapter.UpInventoryCountDataAdapter;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Upstkcksum;
import com.whycools.transport.service.UpstkcksumService;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-08-25.
 */

public class UpInventoryCountDataActivity extends BaseActivity{
    private LinearLayout upinventorycountdata_layout_back;
    private ListView upinventorycountdata_lv;
    private Button upinventorycountdata_updata_bt;
    private UpstkcksumService upstkcksumService;
    private UpInventoryCountDataAdapter adapter;
    private List<Upstkcksum> list=new ArrayList<Upstkcksum>();
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_upinventorycountdata);
    }

    @Override
    public void initViews() {
        upstkcksumService=new UpstkcksumService(this);
        upinventorycountdata_layout_back= findViewById(R.id.upinventorycountdata_layout_back);
        upinventorycountdata_lv= findViewById(R.id.upinventorycountdata_lv);
        upinventorycountdata_updata_bt= findViewById(R.id.upinventorycountdata_updata_bt);

    }

    @Override
    public void initListeners() {
        upinventorycountdata_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(UpInventoryCountDataActivity.this, "_id"+list.get(i).get_id()
                            , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpInventoryCountDataActivity.this,UpDataInventoryCountDataActivity.class).putExtra("_id",list.get(i).get_id().toString()));
            }
        });
        upinventorycountdata_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(UpInventoryCountDataActivity.this);
                builder.setMessage("你确定要删除该条码吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upstkcksumService.deleteUpStkcksum(list.get(i).get_id());
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.create().show();
                return false;
            }

        });
        upinventorycountdata_layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        upinventorycountdata_updata_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updata();

            }
        });

    }

    @Override
    public void initData() {
        showData();


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    showData();
                    break;
                case 2:
                    TransparentDialogUtil.showSuccessMessage(UpInventoryCountDataActivity.this,"没有数据");
                    showData();
                    break;
                case 3:
                    TransparentDialogUtil.showSuccessMessage(UpInventoryCountDataActivity.this,"上传成功");
                    showData();
                    break;
                case 4:
                    TransparentDialogUtil.showSuccessMessage(UpInventoryCountDataActivity.this,"上传失败");
                    shake();
                    showData();
                    break;
            }
        }
    };
    private void showData(){
        list.clear();
        list= upstkcksumService.getAllUpStkcksum();
        Log.i(TAG, "initData:--------- "+list.size());
        for (int i = 0; i <list.size() ; i++) {
            Log.i(TAG, "initData: "+list.get(i).getAuto_id());
            Log.i(TAG, "initData: "+list.get(i).getStoreid());
            Log.i(TAG, "initData: "+list.get(i).getTime());
            Log.i(TAG, "initData: "+list.get(i).getSumDate());
            Log.i(TAG, "initData: "+list.get(i).getQmyeCount());
            Log.i(TAG, "initData: "+list.get(i).getQmyereal());
            Log.i(TAG, "initData: "+list.get(i).getGoodsname());
        }
        adapter=new UpInventoryCountDataAdapter(this,list);
        upinventorycountdata_lv.setAdapter(adapter);

    }
    private void updata(){
        TransparentDialogUtil.showLoadingMessage(UpInventoryCountDataActivity.this,"正在上传中。。。",true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                list= upstkcksumService.getAllUpStkcksum();
                if(list.size()==0){
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                    return;
                }
                Error.contentToTxt(UpInventoryCountDataActivity.this,"应该上传扫描数据的个数:"+list.size());
                StringBuffer buff = new StringBuffer();


                for (int i = 0; i < list.size(); i++) {
                    // fields = new string[] { reader["storeId"].ToString(), reader["sumdate"].ToString(), reader["goodsId"].ToString(), reader["qmyecount"].ToString(), reader["qmyereal"].ToString() };
                    buff.append("exec dbo.UpdStoreCountDate'");
                    String str = list.get(i).getStoreid() + "~"
                            + list.get(i).getSumDate() +"~"
                            + list.get(i).getAuto_id()+"~"
                            +list.get(i).getQmyeCount() + "~"
                            + list.get(i).getQmyereal();
                    buff.append(str);
                    buff.append("!");
                    buff.append("'");
                    Error.contentToTxt(UpInventoryCountDataActivity.this,"盘点表数据上传的数据"+buff);
                    Zip.compress(buff.toString());

                    String url= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
                    String url_synchronization=url+"rent/rProxyDsetExec.jsp?deviceid=&s="+Zip.compress("exec dbo.UpdStoreCountDate'"+str+"!'");
                    Log.i(TAG, "run: <<<<<<<<<<<<<<<<<--------"+"exec dbo.UpdStoreCountDate'"+str+"!'");
                    Error.contentToTxt(UpInventoryCountDataActivity.this,"条码上传的数据url:"+i+">>>>>>>>>>>>>>>>>"+url_synchronization);
                    Log.i(TAG, ": "+url_synchronization);
                    String result= RequestData.getResult(url_synchronization);
                    Log.i(TAG, "run:<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+result);

                        upstkcksumService.deleteUpStkcksum(list.get(i).get_id());

                }




                Message msg=new Message();
                msg.what=3;
                handler.sendMessage(msg);


            }
        }.start();
    }
}
