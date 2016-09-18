package com.gx.appstore.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.SubjectInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SubjectHolder extends BaseHolder<SubjectInfoBean> {
    @ViewInject(R.id.item_subject_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.item_subject_tv_title)
    TextView mTvTitle;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
        // 注入
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void rerefreshHolderView(SubjectInfoBean data) {
        mTvTitle.setText(data.des);
        mTvTitle.setTextColor(Color.BLACK);
        BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL + data.url);
    }

}
