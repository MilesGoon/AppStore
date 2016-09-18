package com.gx.appstore.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.manager.DownLoadInfo;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.CommonUtils;
import com.gx.appstore.utils.PrintDownLoadInfo;
import com.gx.appstore.utils.StringUtils;
import com.gx.appstore.utils.UIUtils;
import com.gx.appstore.view.ProgressView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

public class AppItemHolder extends BaseHolder<AppInfoBean> implements OnClickListener, DownLoadManager.DownLoadObserver {

    @ViewInject(R.id.item_appinfo_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.item_appinfo_rb_stars)
    RatingBar mRbStars;

    @ViewInject(R.id.item_appinfo_tv_des)
    TextView mTvDes;

    @ViewInject(R.id.item_appinfo_tv_size)
    TextView mTvSize;

    @ViewInject(R.id.item_appinfo_tv_title)
    TextView mTvTittle;

    @ViewInject(R.id.item_appinfo_progressView)
    ProgressView mProgressView;

    private AppInfoBean mData;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
        // 初始化孩子对象
        ViewUtils.inject(this, view);
        mProgressView.setOnClickListener(this);
        return view;
    }

    @Override
    protected void rerefreshHolderView(AppInfoBean data) {
        //每次显示之前我就置空进度
        mProgressView.setProgress(0);
        mData = data;
        mTvDes.setText(data.des);
        mTvSize.setText(StringUtils.formatFileSize(data.size));
        mTvTittle.setText(data.name);
        // 设置评分
        mRbStars.setRating(data.stars);
        // 设置图标
        mIvIcon.setImageResource(R.drawable.ic_default);
        BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL + data.iconUrl);
        DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mData);
        refreshProgressViewUI(info);
    }

    public void refreshProgressViewUI(DownLoadInfo info) {

        switch (info.state) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                mProgressView.setNote("下载");
                mProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中
                mProgressView.setProgressEnable(true);
                mProgressView.setMax(info.max);
                mProgressView.setProgress(info.progress);
                int progress = (int) (info.progress * 1.0f / info.max * 100 + .5f);
                mProgressView.setNote(progress + "%");
                mProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mProgressView.setNote("继续");
                mProgressView.setIcon(R.drawable.ic_resume);
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mProgressView.setNote("等待中...");
                mProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                mProgressView.setNote("重试");
                mProgressView.setIcon(R.drawable.ic_redownload);
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                mProgressView.setNote("安装");
                mProgressView.setIcon(R.drawable.ic_install);
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                mProgressView.setNote("打开");
                mProgressView.setIcon(R.drawable.ic_install);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_appinfo_progressView:
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
                refreshProgressViewUI(downLoadInfo);
            }
        });
    }
}
