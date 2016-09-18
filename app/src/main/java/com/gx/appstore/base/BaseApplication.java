package com.gx.appstore.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 全局盒子,里面放置一些全局的变量或者方法,Application其实是一个单例
 */
public class BaseApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static Thread mMainThread;
    private static long mMainThreadId;

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    @Override
    public void onCreate() {
        // 1.上下文
        mContext = getApplicationContext();

        // 2.创建一个handler
        mHandler = new Handler();

        // 3.得到一个主线程id
        mMainThreadId = android.os.Process.myTid();

        // 4.得到主线程
        mMainThread = Thread.currentThread();

        super.onCreate();
    }

}
