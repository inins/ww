package com.frame.component.view.waveview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * liaoinstan
 * 封装波形绘制
 */
public class Wave {

    private View v;
    private Paint mPaint;

    private int width;
    private int height;
    private int waveHeight = 20;// 波浪高度
    //每页显示几个波长
    private float waveCount = 2f;
    private int speed = 1;


    private float preCount; //在开头预画几个波长
    private int baseLine;// 基线，用于控制水位上涨的
    private int waveWidth;//波长
    private float offset;//偏移量
    private float offsetlv; //初始偏移量

    private ValueAnimator animator;

    public Wave(View v) {
        this.v = v;
        this.width = v.getMeasuredWidth();
        this.height = v.getMeasuredHeight();

        waveWidth = (int) (width / waveCount);
        baseLine = height / 2;
        preCount = speed + 1.5f;

        mPaint = new Paint();
        mPaint.setColor(Color.RED); //默认红色
        mPaint.setStyle(Paint.Style.FILL);

        //设置一个波长的偏移
        animator = ValueAnimator.ofFloat(0, 1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                offset = value * speed * waveWidth + offsetlv * waveWidth;//不断的设置偏移量，并重画
                Wave.this.v.postInvalidate();
            }
        });
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        if (v.isInEditMode()) {
            waveHeight = 50;
        }
    }

    public Wave setColor(@ColorInt int color) {
        mPaint.setColor(color);
        return this;
    }

    public Wave setWaweCount(float waweCount) {
        this.waveCount = waweCount;
        waveWidth = (int) (width / waveCount);
        return this;
    }

    //速度分为10档
    public Wave setSpeed(@IntRange(from = 1, to = 10) int speed) {
        this.speed = speed;
        preCount = speed + 1.5f;
        return this;
    }

    //设置偏移量0-1
    public Wave setOffsetLv(@FloatRange(from = 0f, to = 1f) float offsetlv) {
        this.offsetlv = offsetlv;
        return this;
    }

    //设置偏移量0-1
    public Wave setBaselineLv(@FloatRange(from = 0f, to = 1f) float baselineLv) {
        this.baseLine = (int) (height * (1 - baselineLv));
        return this;
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(getPath(), mPaint);
    }

    /**
     * 核心代码，计算path
     *
     * @return
     */
    private Path getPath() {
        //总共几个半波长
        int halfcount = (int) ((waveCount + preCount) * 2);
        //开头位置
        int startcount = (int) (-preCount * 2);

        int itemWidth = waveWidth / 2;//半个波长
        Path mPath = new Path();
        mPath.moveTo(itemWidth * startcount, baseLine);//起始坐标
        mPath.lineTo(itemWidth * startcount + offset, baseLine);//起始坐标
        //核心的代码就是这里
        for (int i = startcount; i < halfcount + startcount; i++) {
            int startX = i * itemWidth;
            mPath.quadTo(
                    startX + itemWidth / 2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine//结束点的Y
            );//只需要处理完半个波长，剩下的有for循环自已就添加了。
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        return mPath;
    }

    //奇数峰值是正的，偶数峰值是负数
    private int getWaveHeigh(int num) {
        if (num % 2 == 0) {
            return baseLine + waveHeight + exheight;
        }
        return baseLine - waveHeight - exheight;
    }

    ////////////////////

    //开始动画
    public void startWave() {
        if (animator != null) animator.start();
    }

    //暂停动画
    public void pauseWave() {
        if (animator != null) animator.pause();
    }

    //继续动画
    public void resumeWave() {
        if (animator != null) animator.resume();
    }

    //取消动画
    public void cancelWave() {
        if (animator != null) animator.cancel();
    }


    private int exheight;
    ValueAnimator anim;

    public int getExheight() {
        return exheight;
    }

    public void setExheight(final int exheight) {
        if (anim != null) anim.cancel();
        anim = ValueAnimator.ofInt(this.exheight, exheight);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(300);
        anim.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            Wave.this.exheight = value;
        });
        anim.start();
    }
}
