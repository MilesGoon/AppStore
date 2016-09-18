package com.gx.appstore.holder;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.AppInfoBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import java.util.List;

public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
    @ViewInject(R.id.app_detail_safe_pic_container)
    LinearLayout mContainerPic;

    @ViewInject(R.id.app_detail_safe_des_container)
    LinearLayout mContainerDes;

    @ViewInject(R.id.app_detail_safe_iv_arrow)
    ImageView mIvArrow;
    private boolean isOpen = true;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_safe, null);
        ViewUtils.inject(this, view);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    protected void rerefreshHolderView(AppInfoBean data) {
        List<AppInfoBean.AppInfoSafeBean> appInfoSafeBeans = data.safe;
        for (AppInfoBean.AppInfoSafeBean appInfoSafeBean : appInfoSafeBeans) {
            ImageView iconImageView = new ImageView(UIUtils.getContext());
            BitmapHelper.display(iconImageView, Constants.URLS.IMAGEBASEURL + appInfoSafeBean.safeUrl);
            mContainerPic.addView(iconImageView);

            LinearLayout ll = new LinearLayout(UIUtils.getContext());

            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            BitmapHelper.display(ivDesIcon, Constants.URLS.IMAGEBASEURL + appInfoSafeBean.safeDesUrl);

            TextView tvDes = new TextView(UIUtils.getContext());
            tvDes.setText(appInfoSafeBean.safeDes);
            if (appInfoSafeBean.safeDesColor == 0) {// 默认
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));// 正确的方式
                // tvDes.setTextColor(R.color.app_detail_safe_normal);//错误的方式
            } else {// 警告色
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }

            ll.addView(ivDesIcon);
            ll.addView(tvDes);

            int padding = UIUtils.dip2Px(3);
            ll.setPadding(padding, padding, padding, padding);
            ll.setGravity(Gravity.CENTER_VERTICAL);

            mContainerDes.addView(ll);
        }

        // 默认折叠动画
        toggle(false);
    }

    @Override
    public void onClick(View v) {
        toggle(true);
    }

    public void toggle(boolean isAnimation) {
        // 折叠动画
        if (isOpen) {// 展开-->折叠
            // 折叠:就是 mContainerDes高度从 应有高度---->0 过程
            mContainerDes.measure(0, 0);
            int start = mContainerDes.getMeasuredHeight();
            int end = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = end;
                mContainerDes.setLayoutParams(params);
            }
        } else {// 折叠-->展开
            // 展开:就是 mContainerDes高度从 0 ---->应有高度 过程
            mContainerDes.measure(0, 0);
            int end = mContainerDes.getMeasuredHeight();
            int start = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = end;
                mContainerDes.setLayoutParams(params);
            }
        }
        isOpen = !isOpen;
    }

    public void doAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.start();
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueanimator) {
//                // 拿到渐变值
                int animatedValue = (Integer) valueanimator.getAnimatedValue();
                LayoutParams params = mContainerDes.getLayoutParams();
                params.height = animatedValue;
                mContainerDes.setLayoutParams(params);
            }
        });

        // 箭头旋转动画
        if (isOpen) {
            ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 360).start();
        } else {
            ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();

        }

    }
}
