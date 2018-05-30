package com.frame.component.utils.viewutils;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * by liaoinstan 2017
 */
public class MotionEventHelper {

    /**
     * 模拟一条从 @start 到 @end 的触摸轨迹在 @time 的时间内分发给 @view
     */
    public static void createTrack(Point start, Point end, int time, View view) {
        long downTime = SystemClock.uptimeMillis();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(time).start();
        animator.addUpdateListener(animation -> {
            float lv = (Float) animation.getAnimatedValue();
            int x = (int) (start.x + (end.x - start.x) * lv);
            int y = (int) (start.y + (end.y - start.y) * lv);
            MotionEvent motionEvent;
            if (lv == 0) {
                motionEvent = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
            } else if (lv == 1) {
                motionEvent = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
            } else {
                motionEvent = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x, y, 0);
            }
            //Log.e("test", motionEvent.toString());
            view.dispatchTouchEvent(motionEvent);
        });
    }
}
