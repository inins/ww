package com.wang.social.moneytree.mvp.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class FlyDiamond extends AppCompatImageView {


    public interface FlyDiamondListener {
        void onAnimationEnd(FlyDiamond view);
    }

    FlyDiamondListener mListener;

    private int originX;
    private int originY;
    private int destX;
    private int destY;


    public FlyDiamond(Context context) {
        this(context, null);
    }

    public FlyDiamond(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlyDiamond(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(FlyDiamondListener listener) {
        this.mListener = listener;
    }


    public void set(int originX, int originY, int destX, int destY) {
        this.originX = originX;
        this.originY = originY;
        this.destX = destX;
        this.destY = destY;
    }

    public void set(int destX, int destY) {
        this.destX = destX;
        this.destY = destY;
    }

    public void startFly() {
        int pointX = (int)getX();
        int pointY = destY;
        startFly(pointX, pointY);
    }

    private ValueAnimator valueAnimator;
    public void startFly(int controlX, int controlY) {
        Point startPosition = new Point((int)getX(), (int)getY());
        Point endPosition = new Point(destX, destY);


        Point controlPoint = new Point(controlX, controlY);

        valueAnimator = ValueAnimator.ofObject(
                new BezierEvaluator2(controlPoint), startPosition, endPosition);
        valueAnimator.setDuration(1800);
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != mListener) {
                    mListener.onAnimationEnd(FlyDiamond.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.addUpdateListener(animation -> {
            Point point = (Point) animation.getAnimatedValue();
            setX(point.x);
            setY(point.y);
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != valueAnimator && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }
}
