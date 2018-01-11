package com.whycools.transport;

import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.whycools.transport.crash.util.DateUtils;
import com.whycools.transport.utils.CrashHandler;
import com.whycools.transport.utils.RequestData;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.mindpipe.android.logging.log4j.LogConfigurator;


/**
 * Created by Administrator on 2016/9/22.
 */
public class AppApplication extends Application{
    private static final String TAG="application";

    public LogConfigurator logConfigurator;
    public Logger logger = null;

    /**
     * 存储卡根目录
     */
    public static String EXTERNALSTORAGEDIR = Environment.getExternalStorageDirectory().getPath();

    /**
     * 程序使用的目录
     */
    public final static String DIR_APP = "LLogDemo";

    /**
     * 日志保存目录
     */
    private final static String DIR_LOG = "log";

    private Timer timer = new Timer(true);

    @Override
    public void onCreate() {
        super.onCreate();

        initLogConfig();
        //友盟sdk
        umtt();

        //启动定时器用来定位使用者当前的位置 现在是1小时执行一次
//        timer.schedule(task, 0, 60*60*1000);//
    }


    public void initLogConfig() {
        //初始化日志信息
        initLoginConfig();
        //初始化系统配置
        createAppDir();
        //logger.info("程序启动");
//        AndroidCrash.getInstance().setLogFileDir(getLogDir()).setCrashReporter
//                (myCrashListener);
//        AndroidCrash.getInstance().init(getApplicationContext());

        // 新加方法
        CrashHandler catchHandler = CrashHandler.getInstance( getserialnumber());
        catchHandler.init(getApplicationContext());
    }



    /**
     * 创建程序目录文件夹
     */
    private  void createAppDir() {
        //检查日志文件目录是否存在
        File logDir = new File(getLogDir());
        if (!logDir.exists())
            logDir.mkdirs();
        File nomedia = new File(EXTERNALSTORAGEDIR + File.separator + DIR_APP, ".nomedia");
        try {
            nomedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取日志文件存取目录
     *
     * @return
     */
    public static String getLogDir() {
        return EXTERNALSTORAGEDIR + File.separator + DIR_APP + File.separator + DIR_LOG;
    }




    /**
     * 初始化日志信息配置
     */
    private void initLoginConfig() {
        //初始化日志设置
        this.logConfigurator = new LogConfigurator();
        //设置日志文件名称
        this.logConfigurator.setFileName(getLogDir() + File.separator + getserialnumber()+DateUtils
                .date2String(new Date(), "yyyyMMdd") + ".log");
        this.logConfigurator.setFilePattern("%d [%p]-[%c.%M(%L)] %m %n");
        //设置日志级别
        this.logConfigurator.setRootLevel(Level.DEBUG);
        this.logConfigurator.setLevel("org.apache", Level.ERROR);
        //设置最大文件大小
        this.logConfigurator.setMaxFileSize(1024 * 1024 * 5);
        this.logConfigurator.setImmediateFlush(true);
        //应用日志设置
        this.logConfigurator.configure();
        //初始化日志
        this.logger = Logger.getLogger(AppApplication.class);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //todo something....

                try {

                    String location = msg.getData().getString("location");
                    Log.i(TAG, "location: "+location);
                    String location2=location.substring(29,location.length()-1);
                    Log.i(TAG, "location2: "+location2);
                    JSONObject obj=new JSONObject(location2);
                    String result=obj.getString("result");
                    Log.i(TAG, "result: "+result);
                    JSONObject obj2=new JSONObject(result);
                    String formatted_address=obj2.getString("formatted_address");
                    Toast.makeText(getApplicationContext(), "我的当前地址" + formatted_address, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (msg.what==2){
                final String x=msg.getData().getString("x");
                final String y=msg.getData().getString("y");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.i(TAG, "handleMessage: "+x+y);
                        String url="http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+ y+","+ x+"&output=json&pois=0&ak=fWrY58aTZHQPhgOhqtCItxUiOsNfGf9P";
                        String result=RequestData.HttpGet(url);
                        Message msg1 = new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("location",result);
                        Log.i("定位返回的数据", "run: "+result);
                        msg1.setData(bundle);
                        msg1.what = 1;
                        handler.sendMessage(msg1);
                    }
                }.start();

//
            }
        }
    };



    //任务
    private TimerTask task = new TimerTask() {
        public void run() {

            try {
                String location= RequestData.HttpGet("http://api.map.baidu.com/location/ip?ak=fWrY58aTZHQPhgOhqtCItxUiOsNfGf9P&coor=bd09ll");
                JSONObject obj=new JSONObject(location);
                String content=obj.getString("content");
                Log.i(TAG, "content: "+content);
                JSONObject obj2=new JSONObject(content);
                String point=obj2.getString("point");
                Log.i(TAG, "point: "+point);
                JSONObject obj3=new JSONObject(point);
                String x=obj3.getString("x");
                Log.i(TAG, "x: "+x);
                String y=obj3.getString("y");
                Log.i(TAG, "y: "+y);
                Message msg=new Message();
                Bundle bundel=new Bundle();
                bundel.putString("x",x);
                bundel.putString("y",y);
                msg.setData(bundel);
                msg.what=2;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public  String getserialnumber(){
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (tm.getDeviceId()!=null&&!tm.getDeviceId().equals("0")){
            Log.i(TAG, "枪id为: "+tm.getDeviceId());
            return tm.getDeviceId();

        }else{
            return "ae01";
        }
    }

    public  void umtt(){
            PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
            mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("注册成功会返回device token", "------------------onSuccess: "+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }


}
