package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.wang.social.topic.R;

public class GradualImageView extends AppCompatImageView {

    private Drawable mDrawableStart;
    private Drawable mDrawableEnd;
    private float mRate;
    boolean isDrawStart;

    private Animation mAnimation;

    public GradualImageView(Context context) {
        this(context, null);
    }

    public GradualImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradualImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void init() {
        setRate(mRate);
    }

    public void startAnimation() {
        if (null == mAnimation) {
            mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.topic_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            mAnimation.setInterpolator(lin);
        }

        startAnimation(mAnimation);
    }

    public void stopAnimation() {
        clearAnimation();
    }

    public void setDrawable(int startResId, int endResId) {
        mDrawableStart = getResources().getDrawable(startResId);
        mDrawableEnd = getResources().getDrawable(endResId);

        setRate(mRate);
    }

    public void setRate(float rate) {
        this.mRate = Math.max(0, Math.min(rate, 1.0f));

        if (null == mDrawableStart && null == mDrawableEnd) {
            setAlpha(rate);
        } else if (null == mDrawableStart) {
            setDrawStart();

            setAlpha(rate);
        } else {
            // 两张图切换
            if (mRate <= 0.5) {
                setDrawStart();

                setAlpha((0.5f - mRate) / 0.5f);
            } else {
                setDrawEnd();

                setAlpha((mRate - 0.5f) / 0.5f);
            }
        }

        invalidate();
    }

    private void setDrawStart() {
        if (!isDrawStart) {
            setImageDrawable(mDrawableStart);
            isDrawStart = true;
        }
    }

    private void setDrawEnd() {
        if (isDrawStart) {
            setImageDrawable(mDrawableEnd);
            isDrawStart = false;
        }
    }
}
