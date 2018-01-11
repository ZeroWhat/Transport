package com.whycools.transport.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.whycools.transport.AppInterface;
import com.whycools.transport.R;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Zero on 2016-12-05.
 */

public abstract class BaseActivity extends FragmentActivity  implements AppInterface{
    /**标示*/
    protected static final String TAG = "BaseActivity";

    /** 所有已存在的Activity */
    protected static ConcurrentLinkedQueue<Activity> allActivity = new ConcurrentLinkedQueue<Activity>();
    /** 同时有效的界面数量 */
    protected static final int validActivityCount = 15;

    /**屏幕的宽高*/
    protected int mScreenWidth;
    protected int mScreenHeight;

    /** Context对象 */
    protected  static  Context mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        palyMuc();

        //Activity队列管理，如果超出指定个数，获取并移除此队列的头（队列中时间最长的）。
        if (allActivity.size() >= validActivityCount) {
            Activity act = allActivity.poll();
            act.finish();// 结束
        }
        allActivity.add(this);
        printAllActivityName();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {//设置布局文件、初始化布局文件中的控件、初始化控件的监听、进行数据初始化。（子类重写这些方法）
            setContentView();
            initViews();
            initListeners();
            initData();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
    /**
     * 设置布局文件
     */
    public abstract void setContentView();
    /**
     * 初始化布局文件中的控件
     */
    public abstract void initViews();
    /**
     * 初始化控件的监听
     */
    public abstract void initListeners();
    /** 进行数据初始化
     * initData
     */
    public abstract void initData();

    /**
     * 获取当前屏幕宽高
     */
    public void getScreenWidthAndHeight(){
        //获取当前屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
    }

    /**
     * 控制台上打印 {@link BaseActivity#allActivity}
     */
    private static void printAllActivityName() {
        for (Activity activity : allActivity) {
            Log.d(TAG, activity.getClass().getName());
        }
    }



    /**
     * 退出当前activity
     * @see android.app.Activity#finish()
     */
    public void finish() {
        try {
            super.finish();
            //软键盘隐藏
            if (null != this.getCurrentFocus() && null != this.getCurrentFocus().getWindowToken()) {
                InputMethodManager in = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                in.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            // 从Activity集合中清理出已结束的Activity
            if (allActivity != null && allActivity.size() > 0 && allActivity.contains(this)) {
                allActivity.remove(this);
            }
            for (Activity a : allActivity) {
                Log.d("finish", a.getClass().getName());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 结束所有activity
     */
    public static void finishAll() {
        // 结束Activity
        try {
            for (Activity act : allActivity) {
                allActivity.remove(act);
                act.finish();
                printAllActivityName();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 判断activity是否已经结束
     * @param act
     * @return
     */
    public static boolean contains(Class<?> act) {
        // 结束Activity
        try {
            for (Activity ele : allActivity) {
                if (ele.getClass().getName().equals(act.getName())) {
                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    /**
     * 取得版本名称
     *
     * @return
     */
    public String getVersionName() {
        if (null != getPackageInfo()) {
            return getPackageInfo().versionName;
        } else {
            return "";
        }
    }
    /**
     * 获取版本号
     */
    public  int getVersionCode( ) {
        return getPackageInfo().versionCode;
    }

    private PackageInfo getPackageInfo() {
        PackageManager manager = this.getPackageManager();
        try {
            return manager.getPackageInfo(this.getPackageName(), 0);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 判断当前手机是否有网络连接
     *
     * @param context
     * @return true,有可用的网络连接；false,没有可用的网络连接
     */
    public  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {

            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    //判断返回是否为数字
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    public  String getserialnumber(){
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (tm.getDeviceId()!=null&&!tm.getDeviceId().equals("0")){
            Log.i(TAG, "枪id为: "+tm.getDeviceId());
            return tm.getDeviceId();

        }else{
            return "ae01";
        }
    }

    //震动
    public void shake() {
		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
    }
    /**
     * 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static final int KEY_SOUND_A1 = 1;
    public static final int KEY_SOUND_A2 = 2;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    private void palyMuc(){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                //alert(" " + sampleId);
            }
        });
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(KEY_SOUND_A1, mSoundPool.load(this, R.raw.a1, 1));
        soundPoolMap.put(KEY_SOUND_A2, mSoundPool.load(this, R.raw.a2, 1));

    }
    public void playMusic(){
        mSoundPool.play(soundPoolMap.get(KEY_SOUND_A1), 1, 1, 0, 0, 1);
    }
    public void stopMusic(){
        mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);
    }

}

