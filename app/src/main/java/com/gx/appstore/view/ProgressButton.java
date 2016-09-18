package com.gx.appstore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class ProgressButton extends Button {
    private boolean isProgressEnable;
    private long mMax = 100;
    private long mProgress;

    /**
     * 设置是否有progress的效果
     */
    public void setProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }

    /**
     * 设置进度的最大值
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置当前的进度
     */
    public void setProgress(long progress) {
        mProgress = progress;
        // 重绘button
        invalidate();
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isProgressEnable) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.BLUE);

            int left = 0;
            int top = 0;
            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
            int bottom = getBottom();
            colorDrawable.setBounds(left, top, right, bottom);
            colorDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }
}
