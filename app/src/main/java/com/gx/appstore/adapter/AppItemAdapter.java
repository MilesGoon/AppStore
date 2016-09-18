package com.gx.appstore.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.gx.appstore.DetailActivity;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.base.SuperBaseAdapter;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.holder.AppItemHolder;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;


public class AppItemAdapter extends SuperBaseAdapter<AppInfoBean> {

    public List<AppItemHolder> mAppItemHolders = new ArrayList<AppItemHolder>();

    public List<AppItemHolder> getAppItemHolders() {
        return mAppItemHolders;
    }

    public AppItemAdapter(AbsListView absListView, List<AppInfoBean> datas) {
        super(absListView, datas);
    }

    @Override
    protected BaseHolder<AppInfoBean> getSpecialHolder(int position) {
        AppItemHolder appItemHolder = new AppItemHolder();
        mAppItemHolders.add(appItemHolder);
        // 添加到观察者集合中
        DownLoadManager.getInstance().addObserver(appItemHolder);

        return appItemHolder;
    }

    @Override
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
        gotoDetailActivity(mDataSource.get(position).packageName);
        super.onNormalItemClick(parent, view, position, id);
    }

    private void gotoDetailActivity(String packageName) {
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.putExtra("packageName", packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);
    }

}
