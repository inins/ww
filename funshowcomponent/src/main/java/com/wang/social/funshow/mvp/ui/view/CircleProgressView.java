package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2017/5/9.
 */

public class CircleProgressView extends View {

    //进度条线宽
    private int progWidth;

    //开始角度
    private final float START = -90;
    private float max = 100;
    private float progress;

    // 画圆所在的距形区域
    private final RectF mRectF;
    private final Paint mPaint;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mPaint = new Paint();
        init();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽和高一样
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        progWidth = dp2px(getContext(), 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#33ffffff"));
        mPaint.setStrokeWidth(progWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        mRectF.left = progWidth / 2; // 左上角x
        mRectF.top = progWidth / 2; // 左上角y
        mRectF.right = width - progWidth / 2; // 左下角x
        mRectF.bottom = height - progWidth / 2; // 右下角y

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);

        //画进度
        mPaint.setColor(Color.parseColor("#aaffffff"));

        float sweepAngle = progress / max * 360;

        canvas.drawArc(mRectF, START, sweepAngle, false, mPaint);
    }


    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }

    ////////////////////////////////

    public float getProgress() {
        return progress;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }


    //////////////////工具类方法

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }
}
