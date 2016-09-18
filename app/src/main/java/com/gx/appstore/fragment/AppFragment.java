package com.gx.appstore.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gx.appstore.adapter.AppItemAdapter;
import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.factory.ListViewFactory;
import com.gx.appstore.holder.AppItemHolder;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.protocol.AppProtocol;

import java.util.List;

public class AppFragment extends BaseFragment {

    private List<AppInfoBean> mDatas;
    private AppProtocol mProtocol;
    private AppAdapter mAdapter;

    @Override
    protected LoadingController.LoadedResult initData() {
        mProtocol = new AppProtocol();
        // 加载数据
        try {
            mDatas = mProtocol.loadData(0);
            // 检测数据
            LoadingController.LoadedResult checkState = checkState(mDatas);
            return checkState;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingController.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        mAdapter = new AppAdapter(listView, mDatas);
        listView.setAdapter(mAdapter);
        return listView;
    }

    class AppAdapter extends AppItemAdapter {

        public AppAdapter(AbsListView absListView, List<AppInfoBean> datas) {
            super(absListView, datas);
        }

        @Override
        public List<AppInfoBean> onLoadMore() throws Exception {
            return mProtocol.loadData(mDatas.size());
        }
    }

    @Override
    public void onPause() {
        if (mAdapter != null) {
            List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
            // 遍历集合,移除观察者
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownLoadManager.getInstance().deleteObserver(appItemHolder);
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mAdapter != null) {
            List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
            // 遍历集合,添加观察者
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownLoadManager.getInstance().addObserver(appItemHolder);
            }
            //手动刷新
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

}