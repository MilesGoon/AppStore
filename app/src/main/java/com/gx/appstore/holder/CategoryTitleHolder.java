package com.gx.appstore.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.CategoryBean;
import com.gx.appstore.utils.UIUtils;


public class CategoryTitleHolder extends BaseHolder<CategoryBean> {

    private TextView mTvTitle;

    @Override
    protected View initHolderView() {
        mTvTitle = new TextView(UIUtils.getContext());
        mTvTitle.setTextColor(Color.BLACK);
        mTvTitle.setBackgroundColor(Color.WHITE);
        int padding = UIUtils.dip2Px(3);
        mTvTitle.setPadding(padding, padding, padding, padding);
        return mTvTitle;
    }

    @Override
    protected void rerefreshHolderView(CategoryBean data) {
        mTvTitle.setText(data.title);
    }

}
