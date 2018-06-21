package com.wang.social.im.utils;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by bingoo on 2015/10/27.
 */
public class ActivitySwitcher {

    private final static int DURATION = 200;
    private final static float DEPTH = 400.0f;

    /* ----------------------------------------------- */

    public interface AnimationFinishedListener {
        /**
         * Called when the animation is finished.
         */
        void onAnimationFinished();
    }

    /* ----------------------------------------------- */

    public static void animationIn(View container, WindowManager windowManager) {
        animationIn(container, windowManager, null);
    }

    public static void animationIn(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(90, 0, false, container, windowManager, listener);
    }

    public static void animationOut(View container, WindowManager windowManager) {
        animationOut(container, windowManager, null);
    }

    public static void animationOut(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(0, -90, true, container, windowManager, listener);
    }

    public static void animationInT(View container, WindowManager windowManager) {
        animationInT(container, windowManager, null);
    }

    public static void animationInT(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(-90, 0, false, container, windowManager, listener);
    }

    public static void animationOutT(View container, WindowManager windowManager, AnimationFinishedListener listener) {
        apply3DRotation(0, 90, true, container, windowManager, listener);
    }

    /* ----------------------------------------------- */

    private static void apply3DRotation(float fromDegree, float toDegree, boolean reverse, View container, WindowManager windowManager, final AnimationFinishedListener listener) {
        Display display = windowManager.getDefaultDisplay();
        final float centerX = display.getWidth() / 2.0f;
        final float centerY = display.getHeight() / 2.0f;

        final Rotate3dAnimation a = new Rotate3dAnimation(fromDegree, toDegree, centerX, centerY, DEPTH, reverse);
        a.reset();
        a.setDuration(DURATION);
        a.setFillAfter(true);
        a.setInterpolator(new LinearInterpolator());
        if (listener != null) {
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    listener.onAnimationFinished();
                }
            });
        }
        container.clearAnimation();
        container.startAnimation(a);
    }

}
