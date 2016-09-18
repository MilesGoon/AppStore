package com.gx.appstore.manager;

import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.factory.ThreadFactory;
import com.gx.appstore.utils.CommonUtils;
import com.gx.appstore.utils.FileUtils;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 下载模块,单例,需要时刻记录当前的状态,根据AppInfoBean返回不同的状态
 */
public class DownLoadManager {
    private static DownLoadManager instance;

    public static final int STATE_UNDOWNLOAD = 0;            // 未下载
    public static final int STATE_DOWNLOADING = 1;           // 下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;         // 暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;       // 等待下载
    public static final int STATE_DOWNLOADFAILED = 4;        // 下载失败
    public static final int STATE_DOWNLOADED = 5;            // 下载完成
    public static final int STATE_INSTALLED = 6;             // 已安装
    // 记录用户点击了下载按钮对应的downLoadInfo
    public Map<String, DownLoadInfo> mDownLoadInfoMap = new HashMap<String, DownLoadInfo>();

    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 完成下载操作 用户点击了下载按钮
     */
    public void downLoad(DownLoadInfo info) {
        mDownLoadInfoMap.put(info.packageName, info);

        // 设置为未下载
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);

        // 预先设置为等待,如果能立刻加到工作线程,状态会修改为下载中,如果无法加入,对应就跑到缓存队列里面去了,状态还是等待中
        info.state = STATE_WAITINGDOWNLOAD;
        notifyObservers(info);

        DownLoadTask task = new DownLoadTask(info);
        //把下载任务绑定到DownLoadInfo身上
        info.task = task;
        ThreadFactory.getDownLoadPool().execute(task);
    }

    class DownLoadTask implements Runnable {
        private DownLoadInfo mInfo;

        public DownLoadTask(DownLoadInfo info) {
            mInfo = info;
        }

        @Override
        public void run() {
            try {
                // 设置为下载中
                mInfo.state = STATE_DOWNLOADING;
                notifyObservers(mInfo);

                // 取出已有的长度
                long initRange = 0;
                File tempFile = new File(mInfo.savePath);
                if (tempFile.exists()) {
                    initRange = tempFile.length();
                }
                // 初始progress
                mInfo.progress = initRange;

                // 正在开始请求网络,下载数据
                HttpUtils httpUtils = new HttpUtils();
                String url = Constants.URLS.BASEURL + "download";
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("name", mInfo.downLoadUrl);
                params.addQueryStringParameter("range", "" + initRange);// ①传递参数的时候传递已有的长度
                ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
                boolean isPause = false;
                if (responseStream.getStatusCode() == 200) {
                    // 保存的文件
                    File saveFile = new File(mInfo.savePath);
                    // 拿到输入流
                    InputStream in = responseStream.getBaseStream();
                    // 初始化输出流
                    FileOutputStream out = new FileOutputStream(saveFile, true);// ②以追加的形式写文件
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = in.read(buffer)) != -1) {
                        if (mInfo.state == STATE_PAUSEDOWNLOAD) {
                            isPause = true;
                            break;
                        }
                        mInfo.progress += len;
                        mInfo.state = STATE_DOWNLOADING;
                        notifyObservers(mInfo);
                        out.write(buffer, 0, len);
                    }
                    if (!isPause){
                        // 下载已经完成走到这里来 设置为已下载
                        mInfo.state = STATE_DOWNLOADED;
                        notifyObservers(mInfo);
                    }
                } else {
                    // 设置为下载失败
                    mInfo.state = STATE_DOWNLOADFAILED;
                    notifyObservers(mInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 设置为下载失败
                mInfo.state = STATE_DOWNLOADFAILED;
                notifyObservers(mInfo);
            }

        }
    }

    /**
     * 返回一个DownLoadInfo
     */
    public DownLoadInfo getDownLoadInfo(AppInfoBean appInfoBean) {

        // 已安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), appInfoBean.packageName)) {
            DownLoadInfo info = generateCommonDownLoadInfo(appInfoBean);
            // 状态的赋值
            info.state = STATE_INSTALLED;
            return info;
        }
        // 已下载
        DownLoadInfo info = generateCommonDownLoadInfo(appInfoBean);
        String savePath = info.savePath;
        File saveFile = new File(savePath);
        if (saveFile.exists() && saveFile.length() == appInfoBean.size) {
            // 状态的赋值
            info.state = STATE_DOWNLOADED;
            return info;
        }
        /**
         下载中
         暂停下载
         等待下载
         下载失败
         */
        if (mDownLoadInfoMap.containsKey(appInfoBean.packageName)) {
            return mDownLoadInfoMap.get(appInfoBean.packageName);
        }

        // 未下载
        DownLoadInfo tempDownLaodInfo = generateCommonDownLoadInfo(appInfoBean);
        tempDownLaodInfo.state = STATE_UNDOWNLOAD;
        return info;
    }

    /***
     * 根据AppInfoBean,返回一个downLoadInfo,并且设置一些常规的属性
     */
    public DownLoadInfo generateCommonDownLoadInfo(AppInfoBean appInfoBean) {
        DownLoadInfo info = new DownLoadInfo();
        // 常规的赋值
        info.downLoadUrl = appInfoBean.downloadUrl;

        // 保存的文件夹
        String dir = FileUtils.getDir("apk");// sdcard/android/data/包名/apk
        String saveName = appInfoBean.packageName + ".apk";
        File saveFile = new File(dir, saveName);
        info.savePath = saveFile.getAbsolutePath();
        info.packageName = appInfoBean.packageName;
        info.max = appInfoBean.size;
        info.progress = 0;
        return info;
    }

    public interface DownLoadObserver {
        void onDownloadInfoChange(DownLoadInfo downLoadInfo);
    }

    // 定义集合保存多个接口对象
    List<DownLoadObserver> observers = new LinkedList<DownLoadObserver>();

    /**
     * 添加观察者
     */
    public synchronized void addObserver(DownLoadObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    /**
     * 删除观察者
     */
    public synchronized void deleteObserver(DownLoadObserver o) {
        observers.remove(o);
    }

    /**
     * 通知所有观察者数据已经改变
     */
    public void notifyObservers(DownLoadInfo downLoadInfo) {
        for (DownLoadObserver o : observers) {
            o.onDownloadInfoChange(downLoadInfo);
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownLoad(DownLoadInfo info) {
        info.state = STATE_PAUSEDOWNLOAD;
        notifyObservers(info);
    }

    /**
     * 取消下载
     */
    public void cancelDownLoad(DownLoadInfo info) {
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);
        ThreadFactory.getDownLoadPool().remove(info.task);
    }
}
