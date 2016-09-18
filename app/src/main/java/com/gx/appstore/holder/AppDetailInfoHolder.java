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

public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {
	@ViewInject(R.id.app_detail_info_iv_icon)
	ImageView	mIvIcon;

	@ViewInject(R.id.app_detail_info_rb_star)
	RatingBar	mRbStar;

	@ViewInject(R.id.app_detail_info_tv_time)
	TextView	mTvTime;

	@ViewInject(R.id.app_detail_info_tv_downloadnum)
	TextView	mTvDownloadNum;

	@ViewInject(R.id.app_detail_info_tv_name)
	TextView	mTvName;

	@ViewInject(R.id.app_detail_info_tv_size)
	TextView	mTvSize;

	@ViewInject(R.id.app_detail_info_tv_version)
	TextView	mTvVersion;

	@Override
	protected View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_info, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void rerefreshHolderView(AppInfoBean data) {
		String date = UIUtils.getString(R.string.appdetail_date, data.date);
		String downLoadNum = UIUtils.getString(R.string.appdetail_downLoadnum, data.downloadNum);
		String size = UIUtils.getString(R.string.appdetail_size, StringUtils.formatFileSize(data.size));
		String version = UIUtils.getString(R.string.appdetail_version, data.version);
		mTvName.setText(data.name);

		mTvDownloadNum.setText(downLoadNum);
		mTvSize.setText(size);
		mTvTime.setText(date);
		mTvVersion.setText(version);

		mIvIcon.setImageResource(R.drawable.ic_default);
		BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL + data.iconUrl);

		mRbStar.setRating(data.stars);
	}

}
