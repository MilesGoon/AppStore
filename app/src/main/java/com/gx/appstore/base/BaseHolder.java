package com.gx.appstore.base;

import android.view.View;

public abstract class BaseHolder<HOLDERBEANTYPE> {

    // 根布局
    public View mHolderView;

    public BaseHolder() {
        // mHolderView会赋值给convertView
        mHolderView = initHolderView();
        mHolderView.setTag(this);
    }

    /**
     * 刷新itemView
     */
    public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
        rerefreshHolderView(data);
    }

    /**
     * BaesHolder具体子类被初始化的时候被调用 返回根布局
     */
    protected abstract View initHolderView();

    /**
     * 刷新itemView,必须实现
     */
    protected abstract void rerefreshHolderView(HOLDERBEANTYPE data);

}
