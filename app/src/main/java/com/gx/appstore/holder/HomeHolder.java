package com.gx.appstore.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.StringUtils;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeHolder extends BaseHolder<AppInfoBean> {

	@ViewInject(R.id.item_appinfo_iv_icon)
	ImageView	mIvIcon;

	@ViewInject(R.id.item_appinfo_rb_stars)
	RatingBar	mRbStars;

	@ViewInject(R.id.item_appinfo_tv_des)
	TextView	mTvDes;

	@ViewInject(R.id.item_appinfo_tv_size)
	TextView	mTvSize;

	@ViewInject(R.id.item_appinfo_tv_title)
	TextView	mTvTittle;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
		// 初始化孩子对象
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void rerefreshHolderView(AppInfoBean data) {
		mTvDes.setText(data.des);
		mTvSize.setText(StringUtils.formatFileSize(data.size));
		mTvTittle.setText(data.name);
		// 设置评分
		mRbStars.setRating(data.stars);
		// 设置图标
		mIvIcon.setImageResource(R.drawable.ic_default);
		// 加载图片
		// BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());
		// http://localhost:8080/GooglePlayServer/image?
		// name=app/com.itheima.www/icon.jpg
		BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL + data.iconUrl);
	}
}
