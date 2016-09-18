package com.gx.appstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private List<Line> mLines = new ArrayList<Line>();  // 用来记录每行View的摆放情况
    private Line mCurrrenLine;                          // 用来记录当前已经添加到了哪一行
    private int mHorizontalSpace = 10;
    private int mVerticalSpace = 10;

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public void setSpace(int horizontalSpace, int verticalSpace) {
        this.mHorizontalSpace = horizontalSpace;
        this.mVerticalSpace = verticalSpace;
    }

    // 根据孩子View的排布情况计算出自己的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 清空
        mLines.clear();
        mCurrrenLine = null;

        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);

        // 获取行最大的宽度
        int maxLineWidth = layoutWidth - getPaddingLeft() - getPaddingRight();

        // 测量孩子
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            // 如果孩子不可见
            if (view.getVisibility() == View.GONE) {
                continue;
            }

            // 测量孩子
            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            // 往lines添加孩子
            if (mCurrrenLine == null) {
                // 说明还没有开始添加孩子
                mCurrrenLine = new Line(maxLineWidth, mHorizontalSpace);

                // 添加到 Lines中
                mLines.add(mCurrrenLine);

                // 行中一个孩子都没有
                mCurrrenLine.addView(view);
            } else {
                // 行不为空,行中有孩子了
                if (mCurrrenLine.canAdd(view)) {
                    // 可以添加
                    mCurrrenLine.addView(view);
                } else {
                    // 不可以添加,装不下去
                    // 换行

                    // 新建行
                    mCurrrenLine = new Line(maxLineWidth, mHorizontalSpace);
                    // 添加到lines中
                    mLines.add(mCurrrenLine);
                    // 将view添加到line
                    mCurrrenLine.addView(view);
                }
            }
        }

        // 设置自己的宽度和高度
        int measuredWidth = layoutWidth;
        // paddingTop + paddingBottom + 所有的行间距 + 所有的行的高度

        float allHeight = 0;
        for (int i = 0; i < mLines.size(); i++) {
            float mHeigth = mLines.get(i).mHeigth;

            // 加行高
            allHeight += mHeigth;
            // 加间距
            if (i != 0) {
                allHeight += mVerticalSpace;
            }
        }

        int measuredHeight = (int) (allHeight + getPaddingTop() + getPaddingBottom() + 0.5f);

        // 已测量出FlowLayout的宽高
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 给Line布局 ---> 给Child布局

        int paddingLeft = getPaddingLeft();
        int offsetTop = getPaddingTop();

        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);

            // 给每行中的孩子view布局
            line.layout(paddingLeft, offsetTop);

            offsetTop += line.mHeigth + mVerticalSpace;
        }
    }

    class Line {

        private List<View> mViews = new ArrayList<View>();    // 用来记录每一行有几个View
        private float mMaxWidth;                              // 行最大的宽度
        private float mUsedWidth;                             // 已经使用了多少宽度
        private float mHeigth;                                // 行的高度
        private float mHorizontalSpace;                       // View和view之间的水平间距
        private float mMarginLeft;
        private float mMarginRight;
        private float mMarginTop;
        private float mMarginBottom;

        public Line(int maxWidth, int horizontalSpace) {
            this.mMaxWidth = maxWidth;
            this.mHorizontalSpace = horizontalSpace;
        }

        //添加view，演绎/预估view排布情况 第一次之后回首先执行canAdd方法再执行此方法
        public void addView(View view) {
            int size = mViews.size();
            // 前面测量过了
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();

            // 计算宽和高
            if (size == 0) {
                // 第一次 直接放进去
                if (viewWidth > mMaxWidth) {
                    mUsedWidth = mMaxWidth;
                } else {
                    mUsedWidth = viewWidth;
                }
                mHeigth = viewHeight;
            } else {
                // 多个view的情况
                mUsedWidth += viewWidth + mHorizontalSpace;
                mHeigth = mHeigth < viewHeight ? viewHeight : mHeigth;
            }

            // 将View记录到集合中
            mViews.add(view);
        }

        //判断是否可以将View添加到line中
        public boolean canAdd(View view) {
            int size = mViews.size();

            if (size == 0) {
                return true;
            }

            int viewWidth = view.getMeasuredWidth();

            // 预计使用的宽度
            float planWidth = mUsedWidth + mHorizontalSpace + viewWidth;

            if (planWidth > mMaxWidth) {
                // 加不进去
                return false;
            }

            return true;
        }

        //给孩子布局
        public void layout(int offsetLeft, int offsetTop) {
            int currentLeft = offsetLeft;
            int size = mViews.size();
            // 判断已经使用的宽度是否小于最大的宽度
            float extra = 0;
            float widthAvg = 0;

            if (mMaxWidth > mUsedWidth) {
                extra = mMaxWidth - mUsedWidth;
                // 多余的空间平均分给此行的字view
                widthAvg = extra / size;
            }

            for (int i = 0; i < size; i++) {
                View view = mViews.get(i);
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();

                // 判断是否有富余
                if (widthAvg != 0) {
                    // 微调
                    int newWidth = (int) (viewWidth + widthAvg + 0.5f);
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
                    view.measure(widthMeasureSpec, heightMeasureSpec);

                    viewWidth = view.getMeasuredWidth();
                    viewHeight = view.getMeasuredHeight();
                }

                // 布局
                int left = currentLeft;
                int top = (int) (offsetTop + (mHeigth - viewHeight) / 2 + 0.5f);
                int right = left + viewWidth;
                int bottom = top + viewHeight;
                view.layout(left, top, right, bottom);

                currentLeft += viewWidth + mHorizontalSpace;
            }
        }
    }
}
