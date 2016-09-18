package com.gx.appstore.factory;


import com.gx.appstore.manager.ThreadPoolProxy;

/**
 * 创建普通线程池,创建下载线程池
 */
public class ThreadFactory {
    static ThreadPoolProxy mNormalPool;    // 只需初始化一次就行了
    static ThreadPoolProxy mDownLoadPool;    // 只需初始化一次就行了

    /**
     * 创建了一个普通的线程池
     */
    public static ThreadPoolProxy getNormalPool() {
        if (mNormalPool == null) {
            synchronized (ThreadFactory.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * 创建了一个下载的线程池
     */
    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadFactory.class) {
                if (mDownLoadPool == null) {
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownLoadPool;
    }
}
