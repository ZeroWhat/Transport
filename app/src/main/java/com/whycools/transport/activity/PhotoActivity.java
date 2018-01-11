//package com.whycools.transport.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.whycools.transport.R;
//import com.whycools.transport.base.BaseActivity;
//import com.whycools.transport.utils.SharedPreferencesUtil;
//import com.whycools.transport.utils.TransparentDialogUtil;
////import com.yuyh.library.imgsel.ImageLoader;
////import com.yuyh.library.imgsel.ImgSelActivity;
////import com.yuyh.library.imgsel.ImgSelConfig;
//
//import java.io.File;
//import java.util.ArrayList;
//
///**
// * 拍照上传
// * Created by user on 2017-04-17.
// */
//
//public class PhotoActivity extends BaseActivity implements View.OnClickListener{
//    private static final int REQUEST_CODE = 0;
////    private TextView tvResult;
////    private SimpleDraweeView draweeView;
//    private Button bt;
//    private ArrayList<String> pathList;
//    private LinearLayout phonto_layout_back;
//    private GridView phonto_gv;
//    private GridAdapter gridAdapter;
//
//
//    @Override
//    public void setContentView() {
//        Fresco.initialize(this);
//        setContentView(R.layout.activity_photo);
//        bt=(Button)findViewById(R.id.phonto_bt_update);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TransparentDialogUtil.showLoadingMessage(mContext,"正在上传",true);
//                for (int i = 0; i < pathList.size(); i++) {
////                    Toast.makeText(PhotoActivity.this, ""+pathList.get(i), Toast.LENGTH_SHORT).show();
//                    sendjlCircle(pathList.get(i));
//                }
//                TransparentDialogUtil.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void initViews() {
//        phonto_layout_back=(LinearLayout)findViewById(R.id.phonto_layout_back);
//        phonto_gv=(GridView)findViewById(R.id.phonto_gv);
//
//    }
//
//    @Override
//    public void initListeners() {
//        phonto_layout_back.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void initData() {
//        gridAdapter = new GridAdapter(pathList);
//        phonto_gv.setAdapter(gridAdapter);
//
//    }
//
//    private ImageLoader loader = new ImageLoader() {
//        @Override
//        public void displayImage(Context context, String path, ImageView imageView) {
//            Glide.with(context).load(path).into(imageView);
//        }
//    };
//
//    public void Multiselect(View view) {
////        tvResult.setText("");
//        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
//                .multiSelect(true)
//                // 是否记住上次选中记录
//                .rememberSelected(false)
//                // 使用沉浸式状态栏
//                .statusBarColor(Color.parseColor("#35bbb0")).build();
//
//        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
//    }
//
//    public void Single(View view) {
////        tvResult.setText("");
//        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
//                // 是否多选
//                .multiSelect(false)
//                .btnText("Confirm")
//                // 确定按钮背景色
//                //.btnBgColor(Color.parseColor(""))
//                // 确定按钮文字颜色
//                .btnTextColor(Color.WHITE)
//                // 使用沉浸式状态栏
//                .statusBarColor(Color.parseColor("#3F51B5"))
//                // 返回图标ResId
////                .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha)
//                .title("Images")
//                .titleColor(Color.WHITE)
//                .titleBgColor(Color.parseColor("#3F51B5"))
//                .allImagesText("All Images")
//                .needCrop(true)
//                .cropSize(1, 1, 200, 200)
//                // 第一个是否显示相机
//                .needCamera(true)
//                // 最大选择图片数量
//                .maxNum(9)
//                .build();
//
//        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
//            gridAdapter = new GridAdapter(pathList);
//            phonto_gv.setAdapter(gridAdapter);
//
//            for (String path : pathList) {
////                tvResult.append(path + "\n");
//            }
//        }
//    }
//
//    /**
//     * 上传错误的log日志
//     */
//    private void sendjlCircle(String path ) {
////
////        String path = Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + getserialnumber()+DateUtils
////                .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
////        Log.i(TAG, "路径path: "+path);
//        File file= new File(path);
//        String lid="1c827923-620b-47be-a8c7-7b34d87d705f";
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("attach", file, "txt");
//        String url_serveraddress= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
//        //http://10.10.10.2:89/rent/simpleUpload
//        String url=url_serveraddress+"update/simpleUpload.jsp?path=applog";
//        uploadMethod(params,url);
//    }
//    private void uploadMethod(RequestParams params, String uploadHost) {
//
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                        //开始上传
////                        TransparentDialogUtil.showLoadingMessage(mContext,"开始上传",true);
//                    }
//
//                    @Override
//                    public void onLoading(long total, long current,
//                                          boolean isUploading) {
//                        //上传中
////                        TransparentDialogUtil.showLoadingMessage(mContext,"上传中",true);
//                    }
//
//                    @Override
//                    public void onSuccess(
//                            ResponseInfo<String> responseInfo) {
//                        //上传成功
////                        TransparentDialogUtil.showSuccessMessage(mContext,"上传成功");
////                        try {
////                            String path =Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + DateUtils
////                                    .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
////                            File f = new File(path);
////                            FileWriter fw =  new FileWriter(f);
////                            fw.write("");
////
////                            fw.close();
////                        } catch (IOException e) {
////                            // TODO Auto-generated catch block
////                            e.printStackTrace();
////                        }
////                           Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error,
//                                          String msg) {
//                        //上传失败
////                        TransparentDialogUtil.showSuccessMessage(mContext,"上传失败");
//                    }
//                });
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.phonto_layout_back:
//                finish();
//                break;
//        }
//    }
//
//    private class GridAdapter extends BaseAdapter{
//        private ArrayList<String> listUrls;
//        private LayoutInflater inflater;
//        public GridAdapter(ArrayList<String> listUrls) {
//            this.listUrls = listUrls;
//            if(listUrls.size() == 7){
//                listUrls.remove(listUrls.size()-1);
//            }
//            inflater = LayoutInflater.from(PhotoActivity.this);
//        }
//
//        public int getCount(){
//            return  listUrls.size();
//        }
//        @Override
//        public String getItem(int position) {
//            return listUrls.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = inflater.inflate(R.layout.item_image, parent,false);
//                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder)convertView.getTag();
//            }
//
//            final String path=listUrls.get(position);
//            if (path.equals("000000")){
//                holder.image.setImageResource(R.mipmap.ic_launcher);
//            }else {
//                Glide.with(PhotoActivity.this)
//                        .load(path)
////                        .placeholder(R.mipmap.default_error)
////                        .error(R.mipmap.default_error)
//                        .centerCrop()
//                        .crossFade()
//                        .into(holder.image);
//            }
//            return convertView;
//        }
//        class ViewHolder {
//            ImageView image;
//        }
//    }
//}
