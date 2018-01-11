package com.whycools.transport.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.entity.Newbarcodeheader;
import com.whycools.transport.entity.Stkcksum;
import com.whycools.transport.service.NewStkcksumService;
import com.whycools.transport.service.NewbarcodeheaderService;
import com.whycools.transport.service.ScanlistService;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点表 InventoryCountDetailsActivity
 * Created by Zero on 2016-12-12.
 */

public class InventoryCountActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout inventorycount_back;
    private ListView inventorycount_lv;
    private EditText inventorycount_et;
//    private InventoryCountAdapter adapter;
    private DBinventory dDBinventory;
    private Cursor cursor;
    List<Stkcksum> list=new ArrayList<>();
    private Spinner inventorycount_sp;
    private Spinner inventorycount_sp2;
    private List<String> chosedatalist=new ArrayList<>();

    private ArrayAdapter<String> choseadapter;//选择适配器
    private ArrayAdapter<String> choseadapter2;//选择适配器


    private ScanlistService scanlistservice;//扫描数据库

    private NewStkcksumService newStkcksumService;
    private int make=0;
    private String name="全部";



    private EditText inventory_count_item_ed_number;
    private Button inventory_count_item_bt_save;
    private Button inventory_count_item_bt_same;


    private InventoryAdapter adapter;

    @Override
    public void setContentView() {

        try {
            setContentView(R.layout.activity_inventory_count);
        }catch (Exception e){
            Error.contentToTxt(InventoryCountActivity.this,"盘点表页面启动异常"+e.getMessage());//异常写入文档

        }


    }

    @Override
    public void initViews() {
        try {
            inventorycount_back= findViewById(R.id.inventorycount_back);
            inventorycount_lv= findViewById(R.id.inventorycount_lv);
            inventorycount_et= findViewById(R.id.inventorycount_et);
            inventorycount_sp= findViewById(R.id.inventorycount_sp);
            inventorycount_sp2= findViewById(R.id.inventorycount_sp2);



            inventory_count_item_ed_number= findViewById(R.id.inventory_count_item_ed_number);
            inventory_count_item_bt_save= findViewById(R.id.inventory_count_item_bt_save);
            inventory_count_item_bt_same= findViewById(R.id.inventory_count_item_bt_same);

        }catch (Exception e){
            Error.contentToTxt(InventoryCountActivity.this,"盘点表页面控件实例化异常"+e.getMessage());//异常写入文档

        }

    }

    @Override
    public void initListeners() {

//        try {
            inventorycount_back.setOnClickListener(this);
//            inventory_count_item_bt_save.setOnClickListener(this);
//            inventory_count_item_bt_same.setOnClickListener(this);

            inventorycount_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length()!=0){
//                        showListView();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            inventorycount_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(InventoryCountActivity.this, "第"+i+"个", Toast.LENGTH_SHORT).show();

//                    showListData(i,"  classname='"+inventorycount_sp2.getSelectedItem().toString()+"'");
                    make=i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            inventorycount_sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(InventoryCountActivity.this, "第"+i+"个", Toast.LENGTH_SHORT).show();
//                    name=inventorycount_sp2.getSelectedItem().toString();
//
//                    showListData(make,"  classname='"+inventorycount_sp2.getSelectedItem().toString()+"'");



                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
//        }catch (Exception e){
//            Error.contentToTxt(InventoryCountActivity.this,"盘点表页面控件按钮监听异常"+e.getMessage());//异常写入文档
//
//            Log.i(TAG, "盘点表页面控件按钮监听异常: "+e.getMessage());
//
//        }
        inventorycount_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(InventoryCountActivity.this, "-----"+list.get(i).get_id(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InventoryCountActivity.this,InventoryCountDetailsActivity.class).putExtra("_id",list.get(i).get_id()));
            }
        });

    }


    private void showListData(int index,String name){
        TransparentDialogUtil.showLoadingMessage(InventoryCountActivity.this,"数据加载中，请稍后",true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                list=newStkcksumService.getAllStkcksum();
                if(list.size()>0){
                    Message msg=new Message();
                    msg.what=3;
                    handler.sendMessage(msg);

                }else {
                    Message msg=new Message();
                    msg.what=4;
                    handler.sendMessage(msg);

                }

            }
        }.start();


    }



    @Override
    public void initData() {
        newStkcksumService=new NewStkcksumService(InventoryCountActivity.this);

        String scan= SharedPreferencesUtil.getData(InventoryCountActivity.this,"scan","1").toString();
        if(scan.equals("1")){
            sm = new ScanDevice();
        }

        chosedatalist.add("显示全部库存");
        chosedatalist.add("显示不一致库存");
        chosedatalist.add("仅显示有发生数");
        chosedatalist.add("仅显示发生未盘的");
        choseadapter= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, chosedatalist);
        //仓库适配器设置样式
        choseadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //仓库下拉框加载适配器
        inventorycount_sp.setAdapter(choseadapter);
        inventorycount_sp.setSelection(0);



        List<String> chosedatalist2=newStkcksumService.getClassName();
        for (int i = 0; i < chosedatalist2.size(); i++)  //外循环是循环的次数
        {
            for (int j = chosedatalist2.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
            {

                if (chosedatalist2.get(i) .equals(chosedatalist2.get(j))){
                    chosedatalist2.remove(j);
                }

            }
        }
//        复制代码
        choseadapter2= new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, chosedatalist2);
        //仓库适配器设置样式
        choseadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //仓库下拉框加载适配器
        inventorycount_sp2.setAdapter(choseadapter2);
        inventorycount_sp2.setSelection(0);

        showListData(0,"");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inventorycount_back:
                finish();
                break;
            case R.id.inventory_count_item_bt_save:
               /* if (inventory_count_item_ed_number.getText().toString().length()>0){
                    newStkcksumService.updateStkcksum(inventory_count_item_ed_number.getText().toString(),_id);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "请填写数字", Toast.LENGTH_SHORT).show();
                }*/

                break;
            case R.id.inventory_count_item_bt_same:
               /* Log.i(TAG, "qmyereal: "+qmyereal);
                newStkcksumService.updateStkcksum(qmyereal,_id);
                inventory_count_item_ed_number.setText(qmyereal);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();*/
                break;

        }
    }


    /**
     * 条形码扫描
     */
    private ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private String barcodeStr;
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 如果获取到该广播。
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
//            barcodeStr="AAABT30151234567899887";
            //卡萨帝洗衣机CBM1058W1----CB0B3000M----null----1----2016-07-22 06:35:42.82----6901570079431--------20
            //海尔冰箱BCD-185TMPQ 185L----BK0YF00A1----null----1----2014-11-18 10:30:43.767----6901018052262--------20
            //海尔电热水器ES40H-Q5(ZE) 40升----GA0SFD01C----null----1----2016-01-03 07:55:12.85----6932290358124--------20
//            barcodeStr ="63661234567890";//372004G|41FTXR272PC-W    152263190169202
            Log.i(TAG, "barcodeStr: "+barcodeStr);

                TransparentDialogUtil.showLoadingMessage(InventoryCountActivity.this,"正在匹配中...",true);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        NewbarcodeheaderService newbarcodeheaderService=new NewbarcodeheaderService(InventoryCountActivity.this);
                        List<Newbarcodeheader> list=newbarcodeheaderService.findsame();
                        Log.i(TAG, "run: ."+list.size());
                        for (int i = 0; i <list.size() ; i++) {
                          /*  Log.i(TAG, "run1: "+list.get(i).getMinLen().length());
                            Log.i(TAG, "run2: "+barcodeStr.length());
                            Log.i(TAG, "run3: "+barcodeStr.substring(Integer.valueOf(list.get(i).getBarcodeLeftpos())-1,Integer.valueOf(list.get(i).getMinLen())));
                            Log.i(TAG, "run4: "+list.get(i).getBarcode_header());
                            Log.i(TAG, "--------------------------------");*/
                            if(list.get(i).getMinLen().length()>0&&Integer.valueOf(list.get(i).getMinLen())<=barcodeStr.length()&&list.get(i).getBarcodeLeftpos().length()>0&&barcodeStr.substring(Integer.valueOf(list.get(i).getBarcodeLeftpos())-1,Integer.valueOf(list.get(i).getBarcode_header().length())).equals(list.get(i).getBarcode_header())){
                                Message msg=new Message();
                                Bundle bundle=new Bundle();
                                bundle.putString("_id",list.get(i).get_id()+"");
                                msg.setData(bundle);
                                msg.what=1;
                                handler.sendMessage(msg);
                                Log.i(TAG, "run: ---------------------------------------");
                                return;

                            }
                        }
                        Message msg=new Message();
                        msg.what=2;
                        handler.sendMessage(msg);



                    }

                }.start();



            sm.stopScan();
        }
    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // 如果再活动不可见的时候，如果激光扫描还不为空的情况下，就将激光扫描关闭。
        if (sm != null) {
            sm.stopScan();
        }
        // 同时将广播撤销。
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 注册了一个动态广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
        showListData(0,"");
    }
//    private String _id,qmyereal,qmyeCount;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    playMusic();
                    TransparentDialogUtil.dismiss();
                   String  _id=msg.getData().getString("_id");//388
                    startActivity(new Intent(InventoryCountActivity.this,InventoryCountDetailsActivity.class).putExtra("_id",_id));
                    break;
                case 2:
                    shake();
                    stopMusic();
                    TransparentDialogUtil.showErrorMessage(InventoryCountActivity.this,"没有匹配到该条码");
                    break;
                case 3:


                    adapter=new InventoryAdapter(InventoryCountActivity.this,list);
                    adapter.setListView(inventorycount_lv);
                    inventorycount_lv.setAdapter(adapter);
                    TransparentDialogUtil.dismiss();
                    break;
                case 4:
                    TransparentDialogUtil.showErrorMessage(InventoryCountActivity.this,"没有数据，同步一下看看");
                    break;

            }
        }
    };


    public class InventoryAdapter extends BaseAdapter  {
        private List<Stkcksum> list = null;
        private LayoutInflater layoutInflater = null;
        private Context context;
        private ListView listView;

        public InventoryAdapter(Context context, List<Stkcksum> list) {
            this.list = list;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            InventoryAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new InventoryAdapter.ViewHolder();

                convertView = layoutInflater.inflate(R.layout.activity_inventory_count_lv_item, parent, false);
                viewHolder.inventory_count_lv_item_tv_goodsname = convertView.findViewById(R.id.inventory_count_lv_item_tv_goodsname);
                viewHolder.inventory_ount_lv_item_tv_number = convertView.findViewById(R.id.inventory_ount_lv_item_tv_number);
                viewHolder.inventory_count_lv_item_tv_number = convertView.findViewById(R.id.inventory_count_lv_item_tv_number);
                viewHolder.inventory_count_item_ed_number = convertView.findViewById(R.id.inventory_count_item_ed_number);
                viewHolder.inventory_count_item_bt_save = convertView.findViewById(R.id.inventory_count_item_bt_save);
                viewHolder.inventory_count_item_bt_same = convertView.findViewById(R.id.inventory_count_item_bt_same);
                viewHolder.inventory_count_lv_item_layout = convertView.findViewById(R.id.inventory_count_lv_item_layout);




                convertView.setTag(viewHolder);
            } else {
                viewHolder = (InventoryAdapter.ViewHolder) convertView.getTag();
            }


            viewHolder.inventory_count_lv_item_tv_goodsname.setText(list.get(position).getGoodsname());
            Log.i(TAG, "getView: "+list.get(position).getGoodsname());

            viewHolder.inventory_ount_lv_item_tv_number.setText("库存个数："+list.get(position).getQmyereal());
            if(list.get(position).getQmyeCount().length()>0){
                viewHolder.inventory_count_item_ed_number.setText(list.get(position).getQmyeCount());
                if(list.get(position).getQmyeCount().equals(list.get(position).getQmyereal())){
                    viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.green));
                }else{
                    viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
            }else{
                viewHolder.inventory_count_item_ed_number.setText(0+"");
                viewHolder.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }




            viewHolder.inventory_count_item_bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSeme(position);
                    Toast.makeText(context, " 保存成功", Toast.LENGTH_SHORT).show();




                }
            });
            viewHolder.inventory_count_item_bt_same.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSave(position);
                    Toast.makeText(context, " 一致成功", Toast.LENGTH_SHORT).show();

                }
            });





            return convertView;
        }

        private class ViewHolder {
            TextView inventory_count_lv_item_tv_goodsname = null;
            TextView inventory_ount_lv_item_tv_number = null;
            TextView inventory_count_lv_item_tv_number = null;
            EditText inventory_count_item_ed_number=null;
            Button inventory_count_item_bt_save=null;
            Button inventory_count_item_bt_same=null;
            LinearLayout inventory_count_lv_item_layout=null;

        }

        public  void setSeme(int index){
            int visiblePosition = listView.getFirstVisiblePosition();

            View view = listView.getChildAt(index - visiblePosition);
            final InventoryAdapter.ViewHolder holderOne = new InventoryAdapter.ViewHolder();
            holderOne.inventory_count_lv_item_tv_goodsname = view.findViewById(R.id.inventory_count_lv_item_tv_goodsname);
            holderOne.inventory_ount_lv_item_tv_number = view.findViewById(R.id.inventory_ount_lv_item_tv_number);
            holderOne.inventory_count_lv_item_tv_number = view.findViewById(R.id.inventory_count_lv_item_tv_number);
            holderOne.inventory_count_item_ed_number = view.findViewById(R.id.inventory_count_item_ed_number);
            holderOne.inventory_count_item_bt_save = view.findViewById(R.id.inventory_count_item_bt_save);
            holderOne.inventory_count_item_bt_same = view.findViewById(R.id.inventory_count_item_bt_same);
            holderOne.inventory_count_lv_item_layout = view.findViewById(R.id.inventory_count_lv_item_layout);
            Log.i(TAG, "-------------holderOne.inventory_count_item_ed_number.getText().toString(): "+holderOne.inventory_count_item_ed_number.getText().toString());
            Log.i(TAG, "-------------list.get(index).get_id(): "+list.get(index).get_id());
            newStkcksumService.updateStkcksum(holderOne.inventory_count_item_ed_number.getText().toString(),list.get(index).get_id());
            holderOne.inventory_count_lv_item_layout.setBackgroundColor(context.getResources().getColor(R.color.red));
            list.get(index).setIsMove("1");
        }

        private void setSave(int index){
            int visiblePosition = listView.getFirstVisiblePosition();

            View view = listView.getChildAt(index - visiblePosition);
            final InventoryAdapter.ViewHolder holderOne = new InventoryAdapter.ViewHolder();
            holderOne.inventory_count_lv_item_tv_goodsname = view.findViewById(R.id.inventory_count_lv_item_tv_goodsname);
            holderOne.inventory_ount_lv_item_tv_number = view.findViewById(R.id.inventory_ount_lv_item_tv_number);
            holderOne.inventory_count_lv_item_tv_number = view.findViewById(R.id.inventory_count_lv_item_tv_number);
            holderOne.inventory_count_item_ed_number = view.findViewById(R.id.inventory_count_item_ed_number);
            holderOne.inventory_count_item_bt_save = view.findViewById(R.id.inventory_count_item_bt_save);
            holderOne.inventory_count_item_bt_same = view.findViewById(R.id.inventory_count_item_bt_same);
            holderOne.inventory_count_lv_item_layout = view.findViewById(R.id.inventory_count_lv_item_layout);
            Log.i(TAG, "-------------holderOne.inventory_count_item_ed_number.getText().toString(): "+holderOne.inventory_count_item_ed_number.getText().toString());
            Log.i(TAG, "-------------list.get(index).get_id(): "+list.get(index).get_id());
//            newStkcksumService.updateStkcksum(holderOne.inventory_count_item_ed_number.getText().toString(),list.get(index).get_id());
            holderOne.inventory_count_item_ed_number.setText(list.get(index).getQmyereal().toString());
            list.get(index).setQmyeCount(list.get(index).getQmyereal().toString());

        }

     /*   public void upView(int index) {
            int visiblePosition = listView.getFirstVisiblePosition();

            View view = listView.getChildAt(index - visiblePosition);
            final InstallListAdapter.ViewHolder holderOne = new InstallListAdapter.ViewHolder();
            holderOne.install_list_item_bt = (Button) view.findViewById(R.id.install_list_item_bt);
            holderOne.install_list_item_background = (LinearLayout) view.findViewById(R.id.install_list_item_background);
            holderOne.install_list_item_background.setBackgroundColor(context.getResources().getColor(R.color.green));
            list.get(index).setInstallstateId("3");
            InstallListService installListService = new InstallListService(context);
            installListService.updateInstallList("3", list.get(index).get_id());
            updata(list.get(index).getOrderno(), list.get(index).getInstalldate());
            holderOne.install_list_item_bt.setText("已完成");
        }*/

        public void setListView(ListView listView) {
            this.listView = listView;
        }

      /*  public void updata(final String orderno, final String date) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String url = SharedPreferencesUtil.getData(context, "ServerAddress", SERVERDDRESS).toString();
                    String userid = SharedPreferencesUtil.getData(context, "USERID", "").toString();
                    String url_ = url + "rent/rProxy.jsp?deviceid=&s=" + Zip.compress("InstallDoneConfirm  " + userid + ", " + orderno + ", '" + date + "', 3, '已完工'");
                    Log.i("安装", "run:没有压缩的数据 " + "InstallDoneConfirm  " + userid + ", " + orderno + ", '" + date + "', 3, '已完工'");
                    Log.i("安装", "run: -------------url" + url_);
                    String resu = RequestData.getResult(url_);
                }
            }.start();


        }*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        showListData(0,"");
    }
}
