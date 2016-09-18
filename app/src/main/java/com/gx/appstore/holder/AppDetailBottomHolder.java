package com.gx.appstore.holder;

import android.view.View;
import android.view.View.OnClickListener;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.manager.DownLoadInfo;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.utils.CommonUtils;
import com.gx.appstore.utils.PrintDownLoadInfo;
import com.gx.appstore.utils.UIUtils;
import com.gx.appstore.view.ProgressButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements OnClickListener, DownLoadManager.DownLoadObserver {
    @ViewInject(R.id.app_detail_download_btn_download)
    ProgressButton mProgressBtn;
    private AppInfoBean mData;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_bottom, null);
        ViewUtils.inject(this, view);
        mProgressBtn.setOnClickListener(this);
        return view;
    }

    @Override
    protected void rerefreshHolderView(AppInfoBean data) {
        mData = data;
        DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mData);
        refreshProgressBtnUI(info);
    }

    public void refreshProgressBtnUI(DownLoadInfo info) {

        mProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (info.state) {

            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                mProgressBtn.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中
                mProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                mProgressBtn.setProgressEnable(true);
                mProgressBtn.setMax(info.max);
                mProgressBtn.setProgress(info.progress);
                int progress = (int) (info.progress * 1.0f / info.max * 100 + .5f);
                mProgressBtn.setText(progress + "%");
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mProgressBtn.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mProgressBtn.setText("等待中...");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                mProgressBtn.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                mProgressBtn.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                mProgressBtn.setText("打开");
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.app_detail_download_btn_download:
                DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mData);

                switch (info.state) {
                    case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                        doDownLoad(info);
                        break;
                    case DownLoadManager.STATE_DOWNLOADING:// 下载中
                        pauseDownLoad(info);
                        break;
                    case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                        doDownLoad(info);
                        break;
                    case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                        cancelDownLoad(info);
                        break;
                    case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                        doDownLoad(info);
                        break;
                    case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                        installApk(info);
                        break;
                    case DownLoadManager.STATE_INSTALLED:// 已安装
                        openApk(info);
                        break;
                }

                break;
        }
    }

    /**
     * 安装apk
     */
    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 打开apk
     */
    private void openApk(DownLoadInfo info) {
        CommonUtils.openApp(UIUtils.getContext(), info.packageName);
    }

    /**
     * 开始下载
     */
    public void doDownLoad(DownLoadInfo info) {
        DownLoadManager.getInstance().downLoad(info);
    }

    /**
     * 暂停下载
     */
    private void pauseDownLoad(DownLoadInfo info) {
        DownLoadManager.getInstance().pauseDownLoad(info);
    }

    /**
     * 取消下载
     */
    private void cancelDownLoad(DownLoadInfo info) {
        DownLoadManager.getInstance().cancelDownLoad(info);
    }

    @Override
    public void onDownloadInfoChange(final DownLoadInfo downLoadInfo) {
        // 过滤
        if (!mData.packageName.equals(downLoadInfo.packageName)) {
            return;
        }

        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo);
        // 刷新ui
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                refreshProgressBtnUI(downLoadInfo);
            }
        });
    }

    public void addObserverAndRefresh() {
        // 添加观察者到观察者集合
        DownLoadManager.getInstance().addObserver(this);
        // 手动更新到最新的状态
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mData);
        DownLoadManager.getInstance().notifyObservers(downLoadInfo);
    }
}
