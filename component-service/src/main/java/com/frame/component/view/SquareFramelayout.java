package com.frame.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/8/10.
 */
public class SquareFramelayout extends FrameLayout {

    private float wihi = 1f / 1f;

    public SquareFramelayout(Context context) {
        super(context);
    }

    public SquareFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (size / wihi), mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setWihi(float wihi) {
        this.wihi = wihi;
        requestLayout();
    }
}
