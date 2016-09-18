package com.gx.appstore.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gx.appstore.adapter.AppItemAdapter;
import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.bean.HomeBean;
import com.gx.appstore.factory.ListViewFactory;
import com.gx.appstore.holder.AppItemHolder;
import com.gx.appstore.holder.PictureHolder;
import com.gx.appstore.manager.DownLoadManager;
import com.gx.appstore.protocol.HomeProtocol;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private List<AppInfoBean> mDatas;
    private List<String> mPictures;
    private HomeProtocol mProtocol;
    private HomeAdapter mHomeAdapter;

    @Override
    protected LoadingController.LoadedResult initData() {
        // 线程池中
        mProtocol = new HomeProtocol();
        try {
            HomeBean homeBean = mProtocol.loadData(0);
            LoadingController.LoadedResult state = checkState(homeBean);
            if (state != LoadingController.LoadedResult.SUCCESS) {
                // 有问题
                return state;
            }
            // 检查home.list
            state = checkState(homeBean.list);

            if (state != LoadingController.LoadedResult.SUCCESS) {
                // 有问题
                return state;
            }
            // 走到这里.说明没有问题
            mDatas = homeBean.list;
            mPictures = homeBean.picture;

            return LoadingController.LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingController.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();

        // 给listView添加一个headerView
        PictureHolder pictureHolder = new PictureHolder();
        listView.addHeaderView(pictureHolder.mHolderView);
        // 触发加载数据
        pictureHolder.setDataAndRefreshHolderView(mPictures);
        mHomeAdapter = new HomeAdapter(listView, mDatas);
        listView.setAdapter(mHomeAdapter);
        return listView;
    }

    class HomeAdapter extends AppItemAdapter {
        public HomeAdapter(AbsListView absListView, List<AppInfoBean> datas) {
            super(absListView, datas);
        }

        @Override
        public List<AppInfoBean> onLoadMore() throws Exception {
            SystemClock.sleep(1000);
            List<AppInfoBean> loadMoreData = getMoreData();
            return loadMoreData;
        }
    }

    private List<AppInfoBean> getMoreData() throws Exception {
        // 线程池中
        HomeBean homeBean = mProtocol.loadData(mDatas.size());
        if (homeBean == null) {
            return null;
        }
        if (homeBean.list != null) {
            return homeBean.list;
        }
        return null;
    }


    @Override
    public void onPause() {
        if (mHomeAdapter != null) {
            List<AppItemHolder> appItemHolders = mHomeAdapter.getAppItemHolders();
            // 遍历集合,移除观察者
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownLoadManager.getInstance().deleteObserver(appItemHolder);
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mHomeAdapter != null) {
            List<AppItemHolder> appItemHolders = mHomeAdapter.getAppItemHolders();
            // 遍历集合,添加观察者
            for (AppItemHolder appItemHolder : appItemHolders) {
                DownLoadManager.getInstance().addObserver(appItemHolder);
            }
            //手动刷新
            mHomeAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
