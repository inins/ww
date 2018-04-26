package com.wang.social.topic.mvp.ui.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 颜色可渐变的TextView
 */
public class GradualColorTextView extends AppCompatTextView {
    // 渐变色开始色
    private int mColorStart;
    // 渐变色结束色
    private int mColorEnd;
    // 渐变百分比 0.0 - 0.1
    private float mRate;
    // 是否可渐变
    private boolean mGradual = true;

    ArgbEvaluator mArgbEvaluator;

    public GradualColorTextView(Context context) {
        this(context, null);
    }

    public GradualColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradualColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mArgbEvaluator = new ArgbEvaluator();

        setGradualColor(Color.BLACK, Color.WHITE);
    }

    public void setGradualColor(int start, int end) {
        mColorStart = start;
        mColorEnd = end;

        setRate(mRate);
    }

    public boolean isGradual() {
        return mGradual;
    }

    public void setGradual(boolean gradual) {
        mGradual = gradual;
    }

    /**
     * 改变颜色进度
     * @param rate 变化百分比
     */
    public void setRate(float rate) {
        if (!isGradual()) return;

        // 变化范围是 0.0 ~ 1.0
        this.mRate = Math.max(0, Math.min(rate, 1));

        if (null != mArgbEvaluator) {
            int color = (int)mArgbEvaluator.evaluate(rate, mColorStart, mColorEnd);
            setTextColor(color);
        }

        invalidate();
    }
}
