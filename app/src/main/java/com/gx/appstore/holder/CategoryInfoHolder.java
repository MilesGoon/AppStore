package com.gx.appstore.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gx.appstore.R;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.bean.CategoryBean;
import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.BitmapHelper;
import com.gx.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CategoryInfoHolder extends BaseHolder<CategoryBean> {
    @ViewInject(R.id.item_category_icon_1)
    ImageView mIvIcon1;

    @ViewInject(R.id.item_category_icon_2)
    ImageView mIvIcon2;

    @ViewInject(R.id.item_category_icon_3)
    ImageView mIvIcon3;

    @ViewInject(R.id.item_category_item_1)
    LinearLayout mContainerItem1;

    @ViewInject(R.id.item_category_item_2)
    LinearLayout mContainerItem2;

    @ViewInject(R.id.item_category_item_3)
    LinearLayout mContainerItem3;

    @ViewInject(R.id.item_category_name_1)
    TextView mTvName1;

    @ViewInject(R.id.item_category_name_2)
    TextView mTvName2;

    @ViewInject(R.id.item_category_name_3)
    TextView mTvName3;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_category_info, null);
        // 注入
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void rerefreshHolderView(CategoryBean data) {//
        setData(data.name1, data.url1, mTvName1, mIvIcon1);
        setData(data.name2, data.url2, mTvName2, mIvIcon2);
        setData(data.name3, data.url3, mTvName3, mIvIcon3);
    }

    private void setData(final String name, String url, TextView tvName, ImageView ivIcon) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)) {
            tvName.setText(name);
            ivIcon.setImageResource(R.drawable.ic_default);
            BitmapHelper.display(ivIcon, Constants.URLS.IMAGEBASEURL + url);
            ((ViewGroup) tvName.getParent()).setVisibility(View.VISIBLE);

            // 点击事件
            ((ViewGroup) tvName.getParent()).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ((ViewGroup) tvName.getParent()).setVisibility(View.INVISIBLE);
        }
    }

}
