package com.gx.appstore.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.UIUtils;

import java.util.List;

public class PictureHolder extends BaseHolder<List<String>> {
    private List<String> mPictureUrl;
    private ViewPager mViewPager;
    private LinearLayout mContainerIndicator;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
        mViewPager = (ViewPager) view.findViewById(R.id.item_home_picture_pager);
        mContainerIndicator = (LinearLayout) view.findViewById(R.id.item_home_picture_container_indicator);

        return view;
    }

    @Override
    protected void rerefreshHolderView(List<String> pictureUrl) {
        mPictureUrl = pictureUrl;
        mViewPager.setAdapter(new PictureAdapter());

        for (int i = 0; i < pictureUrl.size(); i++) {
            View indicator = new View(UIUtils.getContext());
            indicator.setBackgroundResource(R.drawable.indicator_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2Px(6), UIUtils.dip2Px(6));
            if (i != 0) {
                params.leftMargin = UIUtils.dip2Px(3);
            }
            params.bottomMargin = UIUtils.dip2Px(5);
            mContainerIndicator.addView(indicator, params);

            if (i == 0) {
                indicator.setBackgroundResource(R.drawable.indicator_selected);
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mPictureUrl.size();

                for (int i = 0; i < mPictureUrl.size(); i++) {
                    View indicator = mContainerIndicator.getChildAt(i);
                    indicator.setBackgroundResource(R.drawable.indicator_normal);
                }

                mContainerIndicator.getChildAt(position).setBackgroundResource(R.drawable.indicator_selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int item = Integer.MAX_VALUE / 2;
        int d = item % mPictureUrl.size();
        mViewPager.setCurrentItem(item - d);

        final AutoScrollTask autoScrollTask = new AutoScrollTask();
        autoScrollTask.start();

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        autoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        autoScrollTask.start();
                        break;
                }
                return false;
            }
        });
    }

    class AutoScrollTask implements Runnable {

        public void start() {
            UIUtils.postTaskDelay(this, 2000);
        }

        public void stop() {
            UIUtils.removeTask(this);
        }

        @Override
        public void run() {
            int preItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++preItem);

            start();
        }
    }


    class PictureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPictureUrl != null) {
                //return mPictureUrl.size();
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mPictureUrl.size();

            ImageView imageView = new ImageView(UIUtils.getContext());
            imageView.setImageResource(R.drawable.ic_default);
            String url = mPictureUrl.get(position);
            String picUrl = Constants.URLS.IMAGEBASEURL + url;
            BitmapHelper.display(imageView, picUrl);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
