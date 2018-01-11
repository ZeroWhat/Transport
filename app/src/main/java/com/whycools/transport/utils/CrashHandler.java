package com.whycools.transport.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.whycools.transport.AppInterface;
import com.whycools.transport.crash.util.DateUtils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler ,AppInterface{

    private Logger logger;

    public static final String TAG = "CrashHandler";
  
    // CrashHandler 实例  
    private static CrashHandler INSTANCE = new CrashHandler();  
  
    // 程序的 Context 对象  
    private static Context mContext;

    private static  String mserialnumber;
  
    // 系统默认的 UncaughtException 处理类  
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
  
    // 用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();
  
    // 用于格式化日期,作为日志文件名的一部分  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
  
  
    /** 保证只有一个 CrashHandler 实例 */  
    private CrashHandler() {
    }  
  
    /** 获取 CrashHandler 实例 ,单例模式 */  
    public static CrashHandler getInstance(String serialnumber) {
        mserialnumber=serialnumber;
        return INSTANCE;  
    }  
  
    /** 
     * 初始化 
     * 
     * @param context 
     */  
    public void init(Context context) {

        logger = Logger.getLogger(CrashHandler.class);
        mContext = context;  
      
        // 获取系统默认的 UncaughtException 处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
      
        // 设置该 CrashHandler 为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    /** 
     * 当 UncaughtException 发生时会转入该函数来处理 
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            // 如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "error : ", e);
            }  
  
            // 退出程序  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);  
        }  
    }  
  
    /** 
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成 
     *  
     * @param ex 
     * @return true：如果处理了该异常信息；否则返回 false 
     */  
    private boolean handleException(final Throwable ex) {
        if (ex == null) {  
            return false;  
        }  
  
        // 使用 Toast 来显示异常信息  
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();
                logger.error("系统异常退出", ex);
                String path=saveCrashInfo2File(ex);
                Log.i(TAG, "日志文件在手机的: "+path);
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出...", Toast.LENGTH_LONG).show();
                sendjlCircle();
                Looper.loop();  
            }  
        }.start();  
  
        // 收集设备参数信息  
        collectDeviceInfo(mContext);
        // 保存日志文件  
        String path=saveCrashInfo2File(ex);
//        sendjlCircle(path);
        return true;  
    }  
  
    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }
  
    /** 
     * 保存错误信息到文件中 
    * 
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

        return null;
    }

    /**
     * 上传错误的log日志
     */
    public static void sendjlCircle( ) {
        String path =Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + mserialnumber+DateUtils
                .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
        File file= new File(path);
        String lid="1c827923-620b-47be-a8c7-7b34d87d705f";
        RequestParams params = new RequestParams();
        params.addBodyParameter("attach", file, "txt");
        String url_serveraddress= SharedPreferencesUtil.getData(mContext,"ServerAddress",SERVERDDRESS).toString();
        //http://10.10.10.2:89/rent/simpleUpload
        String url=url_serveraddress+"rent/AppUpload";
        uploadMethod(params,url);
    }
    public  static void uploadMethod(RequestParams params, String uploadHost) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        //开始上传
//   						tv.setText("开始上传");
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        //上传中
//   						tv.setText("上传中");
                    }

                    @Override
                    public void onSuccess(
                            ResponseInfo<String> responseInfo) {
                        //上传成功
//   						tv.setText("上传成功");
////                        try {
////                            String path =Environment.getExternalStorageDirectory() + "/LLogDemo/"+"log/" + DateUtils
////                                    .date2String(new Date(), "yyyyMMdd") + ".log";//文件绝对地址，sd卡根目录
////                            File f = new File(path);
////                            FileWriter fw =  new FileWriter(f);
////                            fw.write("");
////
////                            fw.close();
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
//   						tv.setText("上传失败");
//                           Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}  