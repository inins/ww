package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class XFrameLayout extends FrameLayout {

    private BitmapDrawable mBitmapDrawable;

    public XFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public XFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmapDrawable(BitmapDrawable bitmapDrawable) {
        mBitmapDrawable = bitmapDrawable;
        if (null != mBitmapDrawable) {
            mBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mBitmapDrawable) {
            canvas.drawBitmap(mBitmapDrawable.getBitmap(), getLeft(), getTop(), new Paint());
        }

        super.onDraw(canvas);
    }
}
