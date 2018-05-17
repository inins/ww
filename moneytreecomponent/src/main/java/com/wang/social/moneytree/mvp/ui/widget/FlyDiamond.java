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
//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "translationX", destX);
//        ObjectAnimator animatorY1 = ObjectAnimator.ofFloat(this, "translationY", getY() - 100);
//        ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(this, "translationY", destY);
//        animatorY1.setInterpolator(new DecelerateInterpolator());
//        animatorY2.setInterpolator(new AccelerateInterpolator());
//
//        AnimatorSet set2 = new AnimatorSet();
//        set2.playSequentially(animatorY1, animatorY2);

//        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationX", getX() - 200);
//        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationY", getY() - 100);
//
//        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2);
//        animator1.setInterpolator(new DecelerateInterpolator());
//
//        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("translationX", destX);
//        PropertyValuesHolder holder4 = PropertyValuesHolder.ofFloat("translationY", destY);
//
//        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(this, holder3, holder4);
//        animator1.setInterpolator(new AccelerateInterpolator());


        Point startPosition = new Point((int)getX(), (int)getY());
        Point endPosition = new Point(destX, destY);

        int pointX = (startPosition.x + endPosition.x) / 2 - 100;
        int pointY = startPosition.y - 200;
        Point controllPoint = new Point(pointX, pointY);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(
                new BezierEvaluator2(controllPoint), startPosition, endPosition);
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
}
