package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

public class CornerNestedScrollView extends NestedScrollView {


    private float[] radiusArray = {
            20f, 20f,
            20f, 20f,
            0f, 0f,
            0f, 0f};

    private Path mPath;
    private RectF mRectF;
    private int mWidth;
    private int mHeight;


    public CornerNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public CornerNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mPath = new Path();
        mRectF = new RectF();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null); //关闭硬件加速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    @Override
    public void onDraw(Canvas canvas) {
        int x = getScrollX();
        int y = getScrollY();

        mRectF.set(x, y, x + mWidth, y + mHeight);
        mPath.addRoundRect(mRectF, radiusArray, Path.Direction.CW);
        canvas.clipPath(mPath);

        super.onDraw(canvas);
    }
}
