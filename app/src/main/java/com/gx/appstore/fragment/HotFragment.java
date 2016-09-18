package com.gx.appstore.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.protocol.HotProtocol;
import com.gx.appstore.utils.UIUtils;
import com.gx.appstore.view.FlowLayout;

import java.util.List;
import java.util.Random;


public class HotFragment extends BaseFragment {

    private List<String> mDatas;

    @Override
    protected LoadingController.LoadedResult initData() {
        HotProtocol protocol = new HotProtocol();
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
        ScrollView scrollView = new ScrollView(UIUtils.getContext());

        FlowLayout flowlayout = new FlowLayout(UIUtils.getContext());
        // 往flowlayout添加数据
        for (final String data : mDatas) {
            // 创建一个textView
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText(data);

            // 默认的背景图片
            GradientDrawable normalDrawable = new GradientDrawable();
            // 设置填充色-->随机颜色
            Random random = new Random();

            int alpha = 255;
            int red = random.nextInt(190) + 30;// 30-220;
            int green = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);
            normalDrawable.setColor(argb);// selector
            normalDrawable.setCornerRadius(UIUtils.dip2Px(5));

            GradientDrawable pressDrawable = new GradientDrawable();
            pressDrawable.setColor(Color.DKGRAY);
            pressDrawable.setCornerRadius(UIUtils.dip2Px(5));

            // java代码状态选择器
            StateListDrawable selectorDrawable = new StateListDrawable();
            selectorDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
            selectorDrawable.addState(new int[]{}, normalDrawable);

            tv.setBackground(selectorDrawable);
            tv.setClickable(true);

            tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });

            int padding = UIUtils.dip2Px(5);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            flowlayout.addView(tv);
        }
        scrollView.addView(flowlayout);
        return scrollView;
    }

}