package com.gx.appstore.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gx.appstore.R;


public class RatioLayout extends FrameLayout {
    private float mPicRatio = 0f;                      // 图片的宽高比
    public static final int RELATIVE_WITH = 0;         // 知道宽度-->动态计算高度
    public static final int RELATIVE_HEIGHT = 1;       // 知道高度-->动态计算宽度

    private int relative = RELATIVE_HEIGHT;

    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    public void setRelative(int relative) {
        this.relative = relative;
    }

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        // 取出属性
        mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0);
        relative = typedArray.getInt(R.styleable.RatioLayout_relative, 0);
        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);// match_prent 100dp

        if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && relative == RELATIVE_WITH) {// 宽度写死
            // 得到控件的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            // 计算控件的高度
            // 图片宽高比 = parentWidth/parentHeight
            int parentHeight = (int) (parentWidth / mPicRatio + .5f);

            // 得到图片控件宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();

            // 让孩子测绘
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 设置自身测绘的结果
            setMeasuredDimension(parentWidth, parentHeight);
        } else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && relative == RELATIVE_HEIGHT) {// 高度写死
            // 得到控件的高度
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            // 动态计算控件宽度
            // 图片的宽高比 = parentWidth/parentHeight
            int parentWidth = (int) (mPicRatio * parentHeight + .5f);

            // 计算孩子的宽度和高度
            // 得到图片控件宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();

            // 让孩子测绘
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 设置自身测绘的结果
            setMeasuredDimension(parentWidth, parentHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
