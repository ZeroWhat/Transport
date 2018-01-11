/*
 * @(#)CrashHandler.java		       Project: crash
 * Date:2014-5-26
 *
 * Copyright (c) 2014 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whycools.transport.crash.log;

import android.content.Context;
import android.os.Looper;


import com.whycools.transport.crash.util.AssertUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 崩溃处理者。
 */
public class CrashCatcher implements Thread.UncaughtExceptionHandler {
    private static final String LOG_TAG = CrashCatcher.class.getSimpleName();

    private static final CrashCatcher sHandler = new CrashCatcher();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashListener mListener;
    private String logFileDir;
    private File mLogFile;
    private Context mContext;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CrashCatcher getInstance() {
        return sHandler;
    }

    /**
     * 初始化日志文件及CrashListener对象
     *
     * @param logFileDir 保存日志的文件
     * @param listener   回调接口
     */
    public void init(Context context, String logFileDir, CrashListener listener,Thread.UncaughtExceptionHandler mDefaultHandler) {
        mContext = context;
        AssertUtil.assertNotNull("logFile", logFileDir);
        this.logFileDir = logFileDir;
        mListener = listener;
        this.mDefaultHandler=mDefaultHandler;
    }




    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        // 如果用户没有处理则让系统默认的异常处理器来处理
        if (!sHandler.handleException(thread, ex, this)||mListener==null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    private File createLogFile() {
        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "crash-" + time+ ".log";
        File mLogFile = new File(logFileDir, fileName);
        return mLogFile;
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     *
     * @param thread
     * @param ex
     * @param crashCatcher
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Thread thread, final Throwable ex,final CrashCatcher crashCatcher) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        try {
            crashCatcher.mLogFile = crashCatcher.createLogFile();
            LogWriter.writeLog(crashCatcher.mContext, crashCatcher.mLogFile, ex.getMessage(), ex);
        } catch (Exception e) {
            return false;
        }
        if (crashCatcher.mListener != null) {
            crashCatcher.mListener.sendFile(crashCatcher.mLogFile,ex);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        crashCatcher.mListener.closeApp(thread, ex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Looper.loop();
                }
            }).start();
        }
        return true;
    }
}
