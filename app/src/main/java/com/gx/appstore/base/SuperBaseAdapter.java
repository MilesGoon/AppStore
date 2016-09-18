package com.gx.appstore.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gx.appstore.config.Constants;
import com.gx.appstore.factory.ThreadFactory;
import com.gx.appstore.holder.LoadMoreHolder;
import com.gx.appstore.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends BaseAdapter implements OnItemClickListener {

    private static final int VIEWTYPE_LOADMORE = 0;
    private static final int VIEWTYPE_NORMAL = 1;
    public List<ITEMBEANTYPE> mDataSource = new ArrayList<ITEMBEANTYPE>();
    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;
    private AbsListView mAbsListView;

    public SuperBaseAdapter(AbsListView absListView, List<ITEMBEANTYPE> datas) {
        super();
        absListView.setOnItemClickListener(this);
        mAbsListView = absListView;
        mDataSource = datas;
    }

    @Override
    public int getCount() {
        if (mDataSource != null) {
            // 加上了加载更多
            return mDataSource.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDataSource != null) {
            return mDataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // 默认是1现在为2(加上了加载更多的类型)
        return super.getViewTypeCount() + 1;
    }

    // 得到每一个item对应的viewType 必须从0开始到getViewTypeCount - 1
    // convertView更替时会检查convertView的类型 如果发现convertView的类型发生了改变 convertView被置为空
    @Override
    public int getItemViewType(int position) {

        if (position == getCount() - 1) {
            // 如果滑到最后一个item的时候->加载更多
            return VIEWTYPE_LOADMORE;// 0
        }
        // 常规item
        return getNormalItemType(position);
    }

    /**
     * 子类可以覆写此方法, 修改返回值
     */
    protected int getNormalItemType(int position) {
        return VIEWTYPE_NORMAL;// 1
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 视图部分
        BaseHolder<ITEMBEANTYPE> baseHolder = null;
        if (convertView == null) {
            // setTag()在BaseHolder()完成
            if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
                // 显示加载更多的holder
                baseHolder = (BaseHolder) getLoadMoreHolder();
            } else {
                // 其他holder
                baseHolder = getSpecialHolder(position);
            }
        } else {
            // convertView指向BaseHolder里的mHolderView
            baseHolder = (BaseHolder) convertView.getTag();
        }

        //模型部分
        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            if (hasLoadMore()) {
                // 有加载更多 把新数据全部添加到mDataSource中
                performLoadMore();
            } else {
                // 没有加载更多
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.STATE_EMPTY);
            }
        } else {
            // 其他holder加载新的常规Item
            ITEMBEANTYPE data = mDataSource.get(position);
            baseHolder.setDataAndRefreshHolderView(data);
        }

        return baseHolder.mHolderView;
    }

    /**
     * 判断是否有加载更多, 默认有加载更多
     * 如果子类没有加载更多, 重写它返回false
     */
    protected boolean hasLoadMore() {
        return true;
    }

    /**
     * 触发加载更多的数据
     */
    private void performLoadMore() {
        if (mLoadMoreTask == null) {
            // 加载之前这个状态修改为加载中
            int state = LoadMoreHolder.STATE_LOADING;
            mLoadMoreHolder.setDataAndRefreshHolderView(state);
            mLoadMoreTask = new LoadMoreTask();
            ThreadFactory.getNormalPool().execute(mLoadMoreTask);
        }
    }

    class LoadMoreTask implements Runnable {
        @Override
        public void run() {
            int state = LoadMoreHolder.STATE_LOADING;
            List<ITEMBEANTYPE> loadMoreData = null;

            try {
                loadMoreData = onLoadMore();
                if (loadMoreData == null) {// 没有加载更多
                    state = LoadMoreHolder.STATE_EMPTY;
                } else {
                    if (loadMoreData.size() < Constants.PAGERSIZE) {
                        // 少于规定条数
                        state = LoadMoreHolder.STATE_EMPTY;// 没有加载更多
                    } else {
                        // 还有数据
                        state = LoadMoreHolder.STATE_LOADING;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = LoadMoreHolder.STATE_RETRY;
            }

            final int tempState = state;
            final List<ITEMBEANTYPE> tempLoadMoreData = loadMoreData;
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    // 会去刷新loadmore视图-->
                    mLoadMoreHolder.setDataAndRefreshHolderView(tempState);
                    // 会去刷新adapter
                    // 修改数据源
                    if (tempLoadMoreData != null && tempLoadMoreData.size() != 0) {
                        mDataSource.addAll(tempLoadMoreData);
                        // 会暂时将ViewGroup parent清空再重新添加
                        notifyDataSetChanged();
                    }
                }
            });

            // 置空mLoadMoreTask 加载完成才可再次加载
            mLoadMoreTask = null;
        }
    }

    private LoadMoreHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    /**
     * 返回新数据
     */
    public List<ITEMBEANTYPE> onLoadMore() throws Exception {
        return null;
    }

    /**
     * convertView == null,也就是需要创建holder时候
     */
    protected abstract BaseHolder<ITEMBEANTYPE> getSpecialHolder(int position);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 如果是listview position需要做处理
        if (mAbsListView instanceof ListView) {
            ListView listView = (ListView) mAbsListView;
            position = position - listView.getHeaderViewsCount();
        }

        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            // 触发重新加载数据
            performLoadMore();
        } else {
            onNormalItemClick(parent, view, position, id);
        }
    }

    /**
     * 点击普通的itemView, 空方法
     */
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
