package com.frame.component.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tmall.ultraviewpager.UltraViewPager;

public class UnScrollUltraViewPager extends UltraViewPager {

    private boolean scrollEnable = false;

    public UnScrollUltraViewPager(Context context) {
        super(context);
    }

    public UnScrollUltraViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (scrollEnable) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollEnable) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    public boolean isScrollEnable() {
        return scrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }
}
