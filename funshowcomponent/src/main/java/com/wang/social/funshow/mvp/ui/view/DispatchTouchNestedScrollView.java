package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DispatchTouchNestedScrollView extends NestedScrollView {
    public DispatchTouchNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public DispatchTouchNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchTouchNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && onDispatchTouchEventCallback != null) {
            onDispatchTouchEventCallback.dispatchTouchEventDown();
        }
        return super.dispatchTouchEvent(ev);
    }


    /////////////////////////////////////////////

    private OnDispatchTouchEventCallback onDispatchTouchEventCallback;

    public void setOnDispatchTouchEventCallback(OnDispatchTouchEventCallback onDispatchTouchEventCallback) {
        this.onDispatchTouchEventCallback = onDispatchTouchEventCallback;
    }

    public interface OnDispatchTouchEventCallback {
        void dispatchTouchEventDown();
    }
}
