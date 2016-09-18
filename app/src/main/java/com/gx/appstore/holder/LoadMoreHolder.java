package com.gx.appstore.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class LoadMoreHolder extends BaseHolder<Integer> {
    @ViewInject(R.id.item_loadmore_container_loading)
    LinearLayout mContainerLoading;

    @ViewInject(R.id.item_loadmore_container_retry)
    LinearLayout mContainerRetry;

    public static final int STATE_LOADING = 1;    // 显示加载效果
    public static final int STATE_RETRY = 2;    // 显示重试效果
    public static final int STATE_EMPTY = 3;    // 没有加载更多,两个都不显示

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void rerefreshHolderView(Integer state) {
        // 根据传过来的状态去区分显示
        // 首先隐藏2个布局
        mContainerLoading.setVisibility(View.GONE);
        mContainerRetry.setVisibility(View.GONE);
        switch (state) {
            case STATE_LOADING:
                mContainerLoading.setVisibility(View.VISIBLE);
                break;
            case STATE_RETRY:
                mContainerRetry.setVisibility(View.VISIBLE);
                break;
            case STATE_EMPTY:
                break;
        }
    }

}
