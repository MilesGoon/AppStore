package com.gx.appstore.holder;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.UIUtils;
import com.gx.appstore.view.RatioLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {
	@ViewInject(R.id.app_detail_pic_iv_container)
	LinearLayout	mContainerPic;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_pic, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void rerefreshHolderView(AppInfoBean data) {
		List<String> screen = data.screen;

		for (int i = 0; i < screen.size(); i++) {
			String picUrl = screen.get(i);
			ImageView iv = new ImageView(UIUtils.getContext());

			iv.setImageResource(R.drawable.ic_default);
			BitmapHelper.display(iv, Constants.URLS.IMAGEBASEURL + picUrl);

			RatioLayout ratioLayout = new RatioLayout(UIUtils.getContext());
			ratioLayout.setPicRatio((float) 150 / 250);
			ratioLayout.setRelative(RatioLayout.RELATIVE_WITH);
			// 把imageView添加到RatioLayout
			ratioLayout.addView(iv);

			int widthPixels = UIUtils.getResources().getDisplayMetrics().widthPixels;
			widthPixels = widthPixels - UIUtils.dip2Px(5) - UIUtils.dip2Px(5);
			int width = widthPixels / 3;// 屏幕的1/3
			int height = LayoutParams.WRAP_CONTENT;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
			if (i != 0) {
				layoutParams.leftMargin = UIUtils.dip2Px(5);
			}
			// 把RatioLayout添加到mContainerPic
			mContainerPic.addView(ratioLayout, layoutParams);
		}
	}
}
