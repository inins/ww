package com.wang.social.pictureselector.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by King on 2018/3/28.
 *
 * 显示图片时，图片应该是宽高相等的
 */

public class ThumbnailLayout extends RelativeLayout {

    public ThumbnailLayout(Context context) {
        this(context, null);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置宽高相等
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int width = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(width, width);

        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        lp.height = width;
        setLayoutParams(lp);
    }
}
