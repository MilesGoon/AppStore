package com.gx.appstore;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.gx.appstore.base.LoadingController;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.holder.AppDetailBottomHolder;
import com.gx.appstore.holder.AppDetailDesHolder;
import com.gx.appstore.holder.AppDetailInfoHolder;
import com.gx.appstore.holder.AppDetailPicHolder;
import com.gx.appstore.holder.AppDetailSafeHolder;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.protocol.AppDetailProtocol;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DetailActivity extends AppCompatActivity {
    private String mPackageName;
    private LoadingController mLoadingController;
    private AppInfoBean mAppInfoBean;


    @ViewInject(R.id.detailsssss_toolbar)
    Toolbar mDetailToolBar;

    @ViewInject(R.id.app_detail_bottom)
    FrameLayout mContainerBottom;

    @ViewInject(R.id.app_detail_des)
    FrameLayout mContainerDes;

    @ViewInject(R.id.app_detail_info)
    FrameLayout mContainerInfo;

    @ViewInject(R.id.app_detail_pic)
    FrameLayout mContainerPic;

    @ViewInject(R.id.app_detail_safe)
    FrameLayout mContainerSafe;
    private AppDetailBottomHolder mAppDetailBottomHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();// activity-->页面-->fragment-->4种页面(成功,失败,空,加载)-->loadingpager
        initData();
        initListener();

    }

    private void init() {
        mPackageName = getIntent().getStringExtra("packageName");
    }

    private void initView() {
        // 告诉DetailActivity视图显示
        mLoadingController = new LoadingController(UIUtils.getContext()) {
            @Override
            protected LoadedResult initData() {
                return DetailActivity.this.loadData();
            }

            @Override
            protected View initSuccessView() {
                return DetailActivity.this.initSuccessView();
            }
        };
        setContentView(mLoadingController);
    }

    /**
     * activtiy具体子线程中应该加载什么数据
     */
    protected LoadingController.LoadedResult loadData() {
        // protocol-->封装本地缓存
        AppDetailProtocol protocol = new AppDetailProtocol(mPackageName);
        try {
            mAppInfoBean = protocol.loadData(0);
            if (mAppInfoBean == null) {
                return LoadingController.LoadedResult.EMPTY;
            }
            return LoadingController.LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingController.LoadedResult.ERROR;
        }
    }

    /**
     * 返回Activity对应的成功视图
     */
    private View initSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.activity_detail, null);
        // 找出几个占位的FrameLayout
        ViewUtils.inject(this, view);


        // app 信息部分
        AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder();
        mContainerInfo.addView(appDetailInfoHolder.mHolderView);
        appDetailInfoHolder.setDataAndRefreshHolderView(mAppInfoBean);

        // app 安全部分
        AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder();
        mContainerSafe.addView(appDetailSafeHolder.mHolderView);
        appDetailSafeHolder.setDataAndRefreshHolderView(mAppInfoBean);

        // app 截图部分
        AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder();
        mContainerPic.addView(appDetailPicHolder.mHolderView);
        appDetailPicHolder.setDataAndRefreshHolderView(mAppInfoBean);

        // app 描述部分
        AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder();
        mContainerDes.addView(appDetailDesHolder.mHolderView);
        appDetailDesHolder.setDataAndRefreshHolderView(mAppInfoBean);

        // app 下载部分
        mAppDetailBottomHolder = new AppDetailBottomHolder();
        mContainerBottom.addView(mAppDetailBottomHolder.mHolderView);
        mAppDetailBottomHolder.setDataAndRefreshHolderView(mAppInfoBean);

        // 添加appDetailBottomHolder到观察者集合中
        DownLoadManager.getInstance().addObserver(mAppDetailBottomHolder);

        initToolBar();
        return view;
    }

    private void initToolBar() {
        setSupportActionBar(mDetailToolBar);
        mDetailToolBar.setLogo(R.drawable.ic_launcher);
        mDetailToolBar.setTitle("AppStore");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        mDetailToolBar.setNavigationIcon(R.drawable.myarrow1);
        mDetailToolBar.setContentInsetsAbsolute(0, 0);
    }


    @Override
    protected void onResume() {
        // 添加监听
        if (mAppDetailBottomHolder != null) {
            mAppDetailBottomHolder.addObserverAndRefresh();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 移除监听
        if (mAppDetailBottomHolder != null) {
            DownLoadManager.getInstance().deleteObserver(mAppDetailBottomHolder);
        }
        super.onPause();
    }

    protected void initData() {
        mLoadingController.LoadDataTrigger();
    }

    protected void initListener() {

    }
}
