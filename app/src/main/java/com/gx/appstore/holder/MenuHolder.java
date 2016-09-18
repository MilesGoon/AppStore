package com.gx.appstore.holder;

import android.view.View;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.utils.UIUtils;

public class MenuHolder extends BaseHolder<Object> {

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
        return view;
    }

    @Override
    protected void rerefreshHolderView(Object data) {

    }

}
