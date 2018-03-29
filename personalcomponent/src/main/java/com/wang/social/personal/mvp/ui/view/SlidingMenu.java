package com.wang.social.personal.mvp.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/9/22.
 */

public class SlidingMenu extends HorizontalScrollView {

    private static final float radio = 0.3f;//菜单占屏幕宽度比
    private final int mScreenWidth;
    private final int mMenuWidth;
    private boolean once = true;

    private boolean slidEnable = true;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            mScreenWidth = dp2px(360);

        } else {
            mScreenWidth = getScreenWidth(context);
        }
        mMenuWidth = (int) (mScreenWidth * radio);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            wrapper.getChildAt(0).getLayoutParams().width = mScreenWidth;
            wrapper.getChildAt(1).getLayoutParams().width = mMenuWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (slidEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    int scrollX = getScrollX();
                    if (Math.abs(scrollX) > mMenuWidth / 2) {
                        this.smoothScrollTo(mMenuWidth, 0);
                    } else {
                        this.smoothScrollTo(0, 0);
                    }
                    return true;
            }
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    public void open() {
        this.smoothScrollTo(mMenuWidth, 0);
    }

    public void close() {
        this.smoothScrollTo(0, 0);
    }

    public void setSlidEnable(boolean slidEnable) {
        this.slidEnable = slidEnable;
    }

    //获取屏幕宽度
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().getDisplayMetrics());
    }
}
