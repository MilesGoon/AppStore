package com.gx.appstore.manager;

public class DownLoadInfo {

    public String downLoadUrl;                                // 下载地址
    public String savePath;                                    // 文件保存地址
    public String packageName;
    public int state = DownLoadManager.STATE_UNDOWNLOAD; // 记录最新的状态.默认是未下载
    public long max;
    public long progress;
    public Runnable task;
}
