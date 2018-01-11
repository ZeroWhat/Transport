package com.whycools.transport.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whycools.transport.R;


/**
 * 透明对话框工具类
 * Created by user on 2016-11-18.
 */
public class TransparentDialogUtil {


    private static TransparentDialog dialog;
    private static Context context;
    //显示一般信息
    public static void showLoadingMessage(Context context, String msg, boolean cancelable) {
        dismiss();
        setDialog(context, msg, R.drawable.transparent_spinner, cancelable);
        if(dialog!=null) dialog.show();

    }
    //显示错误信息,显示时间为2秒
    public static void showErrorMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.transparent_error, true);
        if(dialog!=null) {
            dialog.show();
            dismissAfter2s();
        }
    }
    //显示正确信息，时间为2秒
    public static void showSuccessMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.transparent_success, true);
        if(dialog!=null) {
            dialog.show();
            dismissAfter2s();
        }
    }
    //显示警告信息，时间为2秒
    public static void showInfoMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.transparent_info, true);
        if(dialog!=null) {
            dialog.show();
            dismissAfter2s();
        }
    }



    private static void setDialog(Context ctx, String msg, int resId, boolean cancelable) {
        context = ctx;

        if(!isContextValid()){
            return;
        }
        dialog = TransparentDialog.createDialog(ctx);
        dialog.setMessage(msg);
        dialog.setImage(ctx, resId);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);		// back键是否可dimiss对话框


    }

    /**
     * 关闭对话框
     */
    public static void dismiss() {
        if(isContextValid() && dialog!=null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }


    /**
     * 计时关闭对话框
     *
     */
    private static void dismissAfter2s() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==0)
                dismiss();
        }
    };


    /**
     * 判断parent view是否还存在
     * 若不存在不能调用dismis，或setDialog等方法
     * @return
     */
    private static boolean isContextValid() {
        if(context==null)
            return false;
        if(context instanceof Activity) {
            Activity act = (Activity)context;
            if(act.isFinishing())
                return false;
        }
        return true;
    }
}
