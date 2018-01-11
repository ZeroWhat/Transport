package com.whycools.transport.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.R;
import com.whycools.transport.activity.NoFinishShipListDetailsActivity;
import com.whycools.transport.base.BaseFragment;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;
import com.whycools.transport.utils.DateUtile;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.whycools.transport.activity.PhotoActivity;
/**
 * 没有完成的清单
 * Created by Zero on 2016-12-06.
 */

public class NoFinishShipListFragment extends BaseFragment{
    private static final String TAG="没有完成的清单";
    private ShipListService shiplistservice;
    private String url="";
    private ListView  nofinishshiplist_lv;
    private List<ShipList> shipLists=new ArrayList<>();
    private NoFinishShipListAdapter adapter;
    private Spinner nofinishshiplist_sp;

    private List<String > shipdate=new ArrayList<>();
    private ArrayAdapter<String> shipdateadapter;//日期适配器
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nofinishshiplist,container,false);
        shiplistservice=new ShipListService(getActivity());
        this.context=getContext();
        init(view);
        listener();
        shipdate.add(DateUtile.getdata(0));
        shipdate.add(DateUtile.getdata(1));
        shipdate.add(DateUtile.getdata(2));
        shipdateadapter= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, shipdate);
        //仓库适配器设置样式
        shipdateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //仓库下拉框加载适配器
        nofinishshiplist_sp.setAdapter(shipdateadapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shipLists.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                shipLists=getshiplistbillno(DateUtile.getdata(0));

                String result="";
                if (shipLists.size()>0){
                    result="数据加载成功";
                }else{
                    result="当前没有数据，同步一下试试";
                }
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }.start();
    }

    private void init(View view){
        nofinishshiplist_lv= view.findViewById(R.id.nofinishshiplist_lv);
        nofinishshiplist_sp= view.findViewById(R.id.nofinishshiplist_sp);

    }
    private void listener(){
//        nofinishshiplist_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(getContext(), NoFinishShipListDetailsActivity.class);
//                intent.putExtra("Billno",shipLists.get(i).getBillno());
//                intent.putExtra("Address",shipLists.get(i).getAddress());
//                intent.putExtra("Phonenumber",shipLists.get(i).getPhonenumber());
//                intent.putExtra("GoodsId",shipLists.get(i).getGoodsId());
//                intent.putExtra("Goodsname",shipLists.get(i).getGoodsname());
//                intent.putExtra("Out_qty",shipLists.get(i).getOut_qty());
//                intent.putExtra("In_qty",shipLists.get(i).getIn_qty());
//                intent.putExtra("Cin_qty",shipLists.get(i).getCin_qty());
//                intent.putExtra("Cout_qty",shipLists.get(i).getCout_qty());
//                intent.putExtra("Storeid",shipLists.get(i).getStoreid());
//                intent.putExtra("CarId",shipLists.get(i).getCarId());
//                intent.putExtra("Carname",shipLists.get(i).getCarname());
//                intent.putExtra("Billtype",shipLists.get(i).getBilltype());
//                intent.putExtra("InoutFlag",shipLists.get(i).getInoutFlag());
//                intent.putExtra("_id",shipLists.get(i).get_id());
//                startActivity(intent);
//            }
//        });

        nofinishshiplist_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                shipLists.clear();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        shipLists=getshiplistbillno(nofinishshiplist_sp.getSelectedItem().toString());

                        String result="";
                        if (shipLists.size()>0){
                            result="数据加载成功";
                        }else{
                            result="当前没有数据，同步一下试试";
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("result",result);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }

                }.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void lazyLoad() {
        Log.i(TAG, "lazyLoad: shiplistservice实例化");




//        TransparentDialogUtil.showLoadingMessage(getContext(),LOADATA,true);
        shipLists.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                shipLists=getshiplistbillno(DateUtile.getdata(0));

                String result="";
                if (shipLists.size()>0){
                    result="数据加载成功";
                }else{
                    result="当前没有数据，同步一下试试";
                }
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result",result);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }.start();

    }

    // 查询shiplistcarid等于0
    public List<ShipList> getshiplistbillno(String shipdat) {
        List<ShipList> data = new ArrayList<ShipList>();
        try{

            DBinventory dDBinventory= new DBinventory(context);
            SQLiteDatabase db = dDBinventory.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from shiplist where carId=0 and billtype=? and isfinish=? and shipdate=?", new String[]{"出","完成",shipdat});
            Log.i("查询carid=0", "cursor个数为: "+cursor.getCount());
            while (cursor.moveToNext()) {
                String _id = cursor.getString(cursor.getColumnIndex("_id")); //
                Log.i("查询carid>0", "_id"+_id);
                String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // a1
                String shipdate = cursor.getString(cursor.getColumnIndex("shipdate")); // a1
                String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
                String inoutFlag = cursor.getString(cursor
                        .getColumnIndex("inoutFlag")); // 4
                String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
                String seqno = cursor.getString(cursor.getColumnIndex("seqno")); // 6
                String billtype = cursor.getString(cursor
                        .getColumnIndex("billtype")); // 7
                String billno = cursor.getString(cursor.getColumnIndex("billno")); // 8
                String company_address = cursor.getString(cursor
                        .getColumnIndex("company_address")); // 9
                String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 10
                String goodsname = cursor.getString(cursor
                        .getColumnIndex("goodsname")); // 11
                String in_qty = cursor.getString(cursor.getColumnIndex("in_qty")); // 12
                String cin_qty = cursor.getString(cursor.getColumnIndex("cin_qty"));// 13
                String out_qty = cursor.getString(cursor.getColumnIndex("out_qty"));// 14

                String cout_qty = cursor.getString(cursor
                        .getColumnIndex("cout_qty")); // 15
                String innerno = cursor.getString(cursor.getColumnIndex("innerno")); // 16
                String outerno = cursor.getString(cursor.getColumnIndex("outerno")); // 17

                String address = cursor.getString(cursor.getColumnIndex("address")); // 17
                String Phonenumber = cursor.getString(cursor.getColumnIndex("Phonenumber")); // 17
                String isfinish = cursor.getString(cursor.getColumnIndex("isfinish")); // 17
                String distance = cursor.getString(cursor.getColumnIndex("distance")); // 17
                String isStart = cursor.getString(cursor.getColumnIndex("isStart")); // 17

                String cm = cursor.getString(cursor.getColumnIndex("cm")); // 18
                ShipList shiplist = new ShipList();
                shiplist.set_id(_id);
                shiplist.setStoreid(storeid);// a1
                shiplist.setShipdate(shipdate);// a1
                shiplist.setCarId(carId);// 3
                shiplist.setInoutFlag(inoutFlag);// 4
                shiplist.setCarname(carname);// 5
                shiplist.setSeqno(seqno);// 6
                shiplist.setBilltype(billtype);// 7
                shiplist.setBillno(billno);// 8
                Log.i("查询carid>0", "billno: "+billno);
                shiplist.setCompany_address(company_address);// 9
                shiplist.setGoodsId(goodsId);// 10
                shiplist.setGoodsname(goodsname);// 11
                shiplist.setIn_qty(in_qty);// 12
                shiplist.setCin_qty(cin_qty);// 13
                shiplist.setOut_qty(out_qty);// 14
                shiplist.setCout_qty(cout_qty);// 15
                shiplist.setInnerno(innerno);// 16
                shiplist.setOuterno(outerno);// 17
                shiplist.setAddress(address);
                shiplist.setPhonenumber(Phonenumber);
                shiplist.setIsfinish(isfinish);
                shiplist.setDistance(distance);
                shiplist.setIsStart(isStart);
                shiplist.setCm(cm);// 18
                data.add(shiplist);
            }
            return data;
        }catch (Exception e){
            Log.i(TAG, "错误: "+e.getMessage());
            return data;
        }


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TransparentDialogUtil.dismiss();
                    String result = msg.getData().getString("result");
                    Log.i(TAG, "shiplists个数为: "+ shipLists.size());
                    TransparentDialogUtil.showInfoMessage(getContext(),result);
                    adapter=new NoFinishShipListAdapter(getContext(),shipLists);
                    nofinishshiplist_lv.setAdapter(adapter);
                    if (result.equals("数据加载成功")){
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                String result=RequestData.HttpGet(LOCATION_URL);
                                if (result.length()>5){
                                    try {
                                        JSONObject obj;
                                        Log.i(TAG, "result: "+result);
                                        obj=new JSONObject(result);
                                        String content=obj.has("content")?obj.getString("content"):"";
                                        Log.i(TAG, "content: "+content);
                                        obj=new JSONObject(content);
                                        String point=obj.has("point")?obj.getString("point"):"";
                                        Log.i(TAG, "point: "+point);
                                        obj=new JSONObject(point);
                                        String x=obj.has("x")?obj.getString("x"):"";
                                        String y=obj.has("y")?obj.getString("y"):"";
                                        Log.i(TAG, "x "+x);
                                        Log.i(TAG, "y "+y);

                                        for (int i = 0; i <shipLists.size() ; i++) {
                                            String getLoglat=RequestData.getLoglat(BAIDU1,"上海",shipLists.get(i).getAddress());
                                            Log.i(TAG, "getLoglat: "+getLoglat);
                                            if (getLoglat.length() > 27) {
                                                String resultLog = getLoglat.substring(27, getLoglat.length() - 1);
                                                Log.i(TAG, "resultLog: "+resultLog);
                                                JSONObject obj_lat_lng = new JSONObject(resultLog);
                                                JSONObject obj1_lat_lng = obj_lat_lng.getJSONObject("result");
                                                Log.i(TAG, "run: ");
                                                JSONObject obj2_lat_lng = obj1_lat_lng.getJSONObject("location");
                                                String lng = obj2_lat_lng.getString("lng");//x
                                                Log.i(TAG, "lng: +lng");
                                                String lat = obj2_lat_lng.getString("lat");//y
                                                Log.i(TAG, "lat: "+lat);
                                                ShipList shiplist=new ShipList();
                                                shiplist.set_id(shipLists.get(i).get_id());
                                                shiplist.setStoreid(shipLists.get(i).getStoreid());
                                                shiplist.setShipdate(shipLists.get(i).getShipdate());
                                                shiplist.setSeqno(shipLists.get(i).getSeqno());
                                                shiplist.setCarId(shipLists.get(i).getCarId());
                                                shiplist.setBilltype(shipLists.get(i).getBilltype());
                                                shiplist.setBillno(shipLists.get(i).getBillno());
                                                shiplist.setCompany_address(shipLists.get(i).getCompany_address());
                                                shiplist.setGoodsId(shipLists.get(i).getGoodsId());
                                                shiplist.setGoodsname(shipLists.get(i).getGoodsname());
                                                shiplist.setInnerno(shipLists.get(i).getInnerno());
                                                shiplist.setOuterno(shipLists.get(i).getOuterno());
                                                shiplist.setInoutFlag(shipLists.get(i).getInoutFlag());
                                                shiplist.setAddress(shipLists.get(i).getAddress());
                                                shiplist.setPhonenumber(shipLists.get(i).getPhonenumber());
                                                shiplist.setX(lng);
                                                shiplist.setY(lat);
                                                shiplist.setIn_qty(shipLists.get(i).getIn_qty());
                                                shiplist.setOut_qty(shipLists.get(i).getOut_qty());
                                                shiplist.setCin_qty(shipLists.get(i).getCin_qty());
                                                shiplist.setCout_qty(shipLists.get(i).getCout_qty());
                                                shiplist.setIsStart(shipLists.get(i).getIsStart());
                                                shiplist.setIsfinish(shipLists.get(i).getIsfinish());
                                                shiplist.setDistance(shipLists.get(i).getDistance());
                                                shipLists.set(i,shiplist);
                                            }
                                        }
                                        Message msg=new Message();
                                        msg.what=1;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("x",x);
                                        bundle.putString("y",y);
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        };
                    }
                    break;
                case 9:
                    final String x = msg.getData().getString("x");
                    final String y = msg.getData().getString("y");
                    Log.i(TAG, "x: "+x);
                    Log.i(TAG, "y: "+y);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            for (int i = 0; i < shipLists.size(); i++) {
                                Log.i(TAG, "x: "+x);
                                Log.i(TAG, "y: "+y);
                                Log.i(TAG, "y: "+shipLists.get(i).getX());
                                Log.i(TAG, "y: "+shipLists.get(i).getY());
                                String distance_result=RequestData.getDistanceData(BAIDU2,y,x,shipLists.get(i).getY(),shipLists.get(i).getX());
                                Log.i(TAG, "distance_result: "+distance_result);
                                try {
                                    JSONObject obj_distance = new JSONObject(distance_result);
                                    JSONArray array = obj_distance.getJSONArray("result");
                                    String distance = array.getJSONObject(0).getJSONObject("distance").getString("text");
                                    ShipList shiplist=new ShipList();
                                    shiplist.set_id(shipLists.get(i).get_id());
                                    shiplist.setStoreid(shipLists.get(i).getStoreid());
                                    shiplist.setShipdate(shipLists.get(i).getShipdate());
                                    shiplist.setSeqno(shipLists.get(i).getSeqno());
                                    shiplist.setCarId(shipLists.get(i).getCarId());
                                    shiplist.setBilltype(shipLists.get(i).getBilltype());
                                    shiplist.setBillno(shipLists.get(i).getBillno());
                                    shiplist.setCompany_address(shipLists.get(i).getCompany_address());
                                    shiplist.setGoodsId(shipLists.get(i).getGoodsId());
                                    shiplist.setGoodsname(shipLists.get(i).getGoodsname());
                                    shiplist.setInnerno(shipLists.get(i).getInnerno());
                                    shiplist.setOuterno(shipLists.get(i).getOuterno());
                                    shiplist.setInoutFlag(shipLists.get(i).getInoutFlag());
                                    shiplist.setAddress(shipLists.get(i).getAddress());
                                    shiplist.setPhonenumber(shipLists.get(i).getPhonenumber());
                                    shiplist.setDistance(distance);
                                    shiplist.setIn_qty(shipLists.get(i).getIn_qty());
                                    shiplist.setOut_qty(shipLists.get(i).getOut_qty());
                                    shiplist.setCin_qty(shipLists.get(i).getCin_qty());
                                    shiplist.setCout_qty(shipLists.get(i).getCout_qty());
                                    shiplist.setIsStart(shipLists.get(i).getIsStart());
                                    shiplist.setIsfinish(shipLists.get(i).getIsfinish());
                                    shipLists.set(i,shiplist);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Message msg=new Message();
                            msg.what=3;
                            handler.sendMessage(msg);
                        }
                    }.start();
                    break;
                case 3:
                    adapter=new NoFinishShipListAdapter(getContext(),shipLists);
                    nofinishshiplist_lv.requestLayout();
                    adapter.notifyDataSetInvalidated();
                    nofinishshiplist_lv.setAdapter(adapter); // 重新设置ListView的数据适配器

                    break;
                case 4:
                    adapter=new NoFinishShipListAdapter(getContext(),shipLists);
                    nofinishshiplist_lv.requestLayout();
                    adapter.notifyDataSetInvalidated();
                    nofinishshiplist_lv.setAdapter(adapter); // 重新设置ListView的数据适配器
                    final String Billno=msg.getData().getString("Billno").toString();
                    Log.i(TAG, "Billno: "+Billno);
                    final String Shipdate4=msg.getData().getString("Shipdate");
                    Log.i(TAG, "Shipdate: "+Shipdate4);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            String url=SharedPreferencesUtil.getData(getContext(),"ServerAddress",SERVERDDRESS).toString();
                            String userid=SharedPreferencesUtil.getData(getContext(),"userid","000").toString();
//                            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                            String strdate = formatter.format(curDate);
                            Log.i(TAG, "压缩字符串: "+"shipDoneConfirm "+userid+", "+Billno+", '"+Shipdate4+"', 0, '已发车'");
                            String url_=url+"rent/rProxy.jsp?deviceid=&s="+Zip.compress("shipDoneConfirm "+userid+", "+Billno+", '"+Shipdate4+"', 0, '已发车'");
                            Log.i(TAG, "完成发车的url: "+url_);
                            String resu = RequestData.getResult(url_);
                            Log.i(TAG, "resu: "+resu);
                        }
                    }.start();


                    break;
                case 5:
                    final String _id=msg.getData().getString("_id");
                    Log.i(TAG, "_id: "+_id);
                    final String billno=msg.getData().getString("Billno");
                    Log.i(TAG, "billno: "+billno);
                    final String Shipdate5=msg.getData().getString("Shipdate");
                    Log.i(TAG, "Shipdate: "+Shipdate5);
//                    Toast.makeText(getContext(), "疑难按钮"+billno, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.transparent_info);
                    builder.setTitle("疑难选择");
                    //    指定下拉列表的显示数据
                    final String[] cities = {"地址错", "用户不在", "用户拒收","图片上传","其他"};
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {
//                            Toast.makeText(getContext(), ""+i, Toast.LENGTH_SHORT).show();
                            if(i==3){
//                                startActivity(new Intent(getContext(), PhotoActivity.class));

                            }else if(i==4){
                                inputTitleDialog(billno,Shipdate5,_id);
                            }else{
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        String url=SharedPreferencesUtil.getData(getContext(),"ServerAddress",SERVERDDRESS).toString();
                                        String userid=SharedPreferencesUtil.getData(getContext(),"userid","000").toString();
//                                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                                    String strdate = formatter.format(curDate);
                                        Log.i(TAG, "压缩字符串: "+"shipDoneConfirm "+userid+", "+billno+", '"+Shipdate5+"', a1, '"+cities[i]+"'");
                                        String url_=url+"rent/rProxy.jsp?deviceid=&s="+Zip.compress("shipDoneConfirm "+userid+", "+billno+", '"+Shipdate5+"', a1, '"+cities[i]+"'");
                                        Log.i(TAG, "完成发车的url: "+url_);
                                        String resu = RequestData.getResult(url_);
                                        Log.i(TAG, "resu: "+resu);
                                        shiplistservice.upshiplistisfinishdate("已完成",_id);
                                    }
                                }.start();

                            }

//                            Toast.makeText(getContext(), "当前状态为：" + cities[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();


                    break;
                case 6:
                    Intent intent=new Intent(getContext(),NoFinishShipListDetailsActivity.class);
                    intent.putExtra("_id",msg.getData().getString("_id"));
                    startActivity(intent);
                    break;
            }

        }
    };

    class NoFinishShipListAdapter extends BaseAdapter {

        private List<ShipList> list = null;
        private LayoutInflater layoutInflater = null;
        private Context context;
        private ViewHolder viewHolder = null;

        public NoFinishShipListAdapter(Context context, List<ShipList> list) {
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
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.fragment_nofinishshiplist_lv_item,parent,false);
                viewHolder.nofinishshiplist_lv_item_tv_address = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_address);
                viewHolder.nofinishshiplist_lv_item_tv_billno = convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_billno);
                viewHolder.nofinishshiplist_lv_item_bt_start= convertView.findViewById(R.id.nofinishshiplist_lv_item_bt_start);
                viewHolder.nofinishshiplist_lv_item_tv_finish= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_finish);
                viewHolder.nofinishshiplist_lv_item_tv_distance= convertView.findViewById(R.id.nofinishshiplist_lv_item_tv_distance);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Error.contentToTxt(context,"按单发货"+list.get(position).getAddress()+">>>>"+list.get(position).getBillno()+">>第"+position+"个");
            viewHolder.nofinishshiplist_lv_item_tv_address.setText(list.get(position).getAddress());
            viewHolder.nofinishshiplist_lv_item_tv_billno.setText(list.get(position).getBillno());
            viewHolder.nofinishshiplist_lv_item_bt_start.setText(list.get(position).getIsStart());

            viewHolder.nofinishshiplist_lv_item_tv_finish.setText(list.get(position).getIsfinish());
            viewHolder.nofinishshiplist_lv_item_tv_distance.setText(list.get(position).getDistance());

//            if (list.get(position).getIsStart().equals("发车")){
//                viewHolder.nofinishshiplist_lv_item_tv_finish.setVisibility(View.GONE);
//            }
//            if (list.get(position).getIsStart().equals("送达")){
//                viewHolder.nofinishshiplist_lv_item_tv_finish.setVisibility(View.VISIBLE);
//                viewHolder.nofinishshiplist_lv_item_tv_finish.setText("疑难");
//            }
            viewHolder.nofinishshiplist_lv_item_bt_start.setOnClickListener(new btClick(position));//发车按钮监听
            viewHolder.nofinishshiplist_lv_item_tv_finish.setOnClickListener(new btClick(position));//完成按钮监听
            return convertView;
        }

        private class ViewHolder {
            TextView nofinishshiplist_lv_item_tv_address = null;
            TextView nofinishshiplist_lv_item_tv_billno = null;
            Button nofinishshiplist_lv_item_bt_start=null;
            Button nofinishshiplist_lv_item_tv_finish=null;
            TextView nofinishshiplist_lv_item_tv_distance=null;
        }
        /*
* 此为listview条目中的按钮点击事件的写法
*/
        class btClick implements View.OnClickListener {
            private int position;
            public btClick(int pos){  // 在构造时将position传给它，这样就知道点击的是哪个条目的按钮
                this.position = pos;
            }
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.nofinishshiplist_lv_item_bt_start:
                        shiplistservice=new ShipListService(context);
                        if( list.get(position).getIsStart().equals("送达")){//list.get(position).getIsStart().equals("完成")&&list.get(position).getIn_qty().equals(list.get(position).getCin_qty())&&list.get(position).getOut_qty().equals(list.get(position).getCout_qty()
                            Log.i("完成按钮", "getIn_qty: "+list.get(position).getIn_qty()+">>>>>>>>>>>>>>getCin_qty"+list.get(position).getCin_qty()+">>>>>>getOut_qty"+list.get(position).getOut_qty()+">>>>>>>>>>getCout_qty"+list.get(position).getCout_qty());
//                        if (list.get(position).getIn_qty().equals(list.get(position).getCin_qty())&&list.get(position).getOut_qty().equals(list.get(position).getCout_qty())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("你确定已经发车了吗？");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    list.get(position).set_id(list.get(position).get_id());
                                    list.get(position).setShipdate(list.get(position).getShipdate());
                                    list.get(position).setAddress(list.get(position).getAddress());
                                    list.get(position).setBillno(list.get(position).getBillno());
                                    list.get(position).setPhonenumber(list.get(position).getPhonenumber());
                                    list.get(position).setDistance(list.get(position).getDistance());
                                    list.get(position).setStoreid(list.get(position).getStoreid());
                                    list.get(position).setGoodsname(list.get(position).getGoodsname());

                                    list.get(position).setCarId(list.get(position).getCarId());
                                    list.get(position).setCarname(list.get(position).getCarname());
                                    list.get(position).setBilltype(list.get(position).getBilltype());
                                    list.get(position).setGoodsId(list.get(position).getGoodsId());
                                    list.get(position).setInnerno(list.get(position).getInnerno());
                                    list.get(position).setOuterno(list.get(position).getOuterno());
                                    list.get(position).setInoutFlag(list.get(position).getInoutFlag());
                                    list.get(position).setIn_qty(list.get(position).getIn_qty());
                                    list.get(position).setOut_qty(list.get(position).getOut_qty());
                                    list.get(position).setCin_qty(list.get(position).getCin_qty());
                                    list.get(position).setCout_qty(list.get(position).getCout_qty());
                                    list.get(position).setIsStart("送达");
                                    shiplistservice.upshiplistIsStartdate("送达",list.get(position).get_id());

                                    Message msg = new Message();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("Billno",list.get(position).getBillno());
                                    bundle.putString("Shipdate",list.get(position).getShipdate());
                                    msg.setData(bundle);
                                    msg.what = 4;
                                    list.add(0,list.get(position));
                                    list.remove(position+1);
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
//                        }else {
//                            Toast.makeText(context, "扫码还没有完成", Toast.LENGTH_SHORT).show();
//                        }
                        }else if(list.get(position).getIsStart().equals("发车")){
                            Message msg=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putString("_id",list.get(position).get_id());
                            msg.setData(bundle);
                            msg.what=6;
                            handler.sendMessage(msg);
                        }
                        break;
                    case R.id.nofinishshiplist_lv_item_tv_finish:
                        Message msg = new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("_id",list.get(position).get_id());
                        bundle.putString("Billno",list.get(position).getBillno());
                        bundle.putString("Shipdate",list.get(position).getShipdate());
                        msg.setData(bundle);
                        msg.what = 5;
                        handler.sendMessage(msg);
                        break;

                }

            }

        }


    }


    private void inputTitleDialog(final String billno,final String Shipdate,final String _id) {

        final EditText inputServer = new EditText(getActivity());
        inputServer.setHint("请输入其他原因");
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("其他").setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("发送",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                        if (inputServer.getText().length()==0){
                            Toast.makeText(getActivity(), "请输入其他原因", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                String url=SharedPreferencesUtil.getData(getContext(),"ServerAddress",SERVERDDRESS).toString();
                                String userid=SharedPreferencesUtil.getData(getContext(),"userid","000").toString();
//                                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//                                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                                    String strdate = formatter.format(curDate);
                                Log.i(TAG, "压缩字符串: "+"shipDoneConfirm "+userid+", "+billno+", '"+Shipdate+"', a1, '"+inputServer.getText().toString()+"'");
                                String url_=url+"rent/rProxy.jsp?deviceid=&s="+Zip.compress("shipDoneConfirm "+userid+", "+billno+", '"+Shipdate+"', a1, '"+inputServer.getText().toString()+"'");
                                Log.i(TAG, "完成发车的url: "+url_);
                                String resu = RequestData.getResult(url_);
                                Log.i(TAG, "resu: "+resu);
                                shiplistservice.upshiplistisfinishdate("已完成",_id);
                            }
                        }.start();
                    }
                });
        builder.show();
    }






}
