package com.gx.appstore.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.protocol.RecommendProtocol;
import com.gx.appstore.utils.UIUtils;
import com.gx.appstore.view.flyoutin.ShakeListener;
import com.gx.appstore.view.flyoutin.StellarMap;

import java.util.List;
import java.util.Random;


public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;
    private ShakeListener mShakeListener;

    @Override
    protected LoadingController.LoadedResult initData() {
        RecommendProtocol protocol = new RecommendProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingController.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        // 设置默认选中首页
        stellarMap.setGroup(0, true);

        // 拆分屏幕
        stellarMap.setRegularity(15, 20);

        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            // 检测到了手机的摇动
            @Override
            public void onShake() {
                int currentGroup = stellarMap.getCurrentGroup();

                if (currentGroup == adapter.getGroupCount() - 1) {
                    currentGroup = 0;
                } else {
                    // 切换到下一页
                    currentGroup++;
                }
                stellarMap.setGroup(currentGroup, true);
            }
        });
        return stellarMap;
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            // mShakeListener和Fragment生命周期绑定
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            // mShakeListener和Fragment生命周期绑定
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommendAdapter implements StellarMap.Adapter {

        private static final int PAGERSIZE = 15;

        @Override
        public int getGroupCount() {
            // 一共多少组
            int pagerSize = mDatas.size() / PAGERSIZE;
            // 不能整除的情况
            if (mDatas.size() % PAGERSIZE != 0) {
                return pagerSize + 1;
            }
            return pagerSize;
        }

        @Override
        public int getCount(int group) {
            // 如果是最后一页的时候,对应应该显示余数
            if (mDatas.size() % PAGERSIZE != 0) {
                // 有余数的情况下,最后一页需要特殊处理
                if (group == getGroupCount() - 1) {
                    // 最后一组
                    return mDatas.size() % PAGERSIZE;
                }
            }
            return PAGERSIZE;
        }

        @Override
        public View getView(int group, int position, View convertView) {
            // 具体的view
            TextView tv = new TextView(UIUtils.getContext());
            // group:第几组
            // position:第几组中的第几个
            int index = group * PAGERSIZE + position;
            tv.setText(mDatas.get(index));

            Random random = new Random();
            // 随机颜色
            int alpha = 255;
            int red = random.nextInt(190) + 30;// 30-220
            int green = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);
            tv.setTextColor(argb);
            // 随机大小
            tv.setTextSize(random.nextInt(10) + 12);// 12-16
            int padding = UIUtils.dip2Px(3);
            tv.setPadding(padding, padding, padding, padding);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}