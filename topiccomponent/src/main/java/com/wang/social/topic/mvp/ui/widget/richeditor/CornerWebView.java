package com.wang.social.topic.mvp.ui.widget.richeditor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;

import timber.log.Timber;

public class CornerWebView extends WebView {

    private float[] radiusArray = {
            20f, 20f,
            20f, 20f,
            20f, 20f,
            20f, 20f};

    private Path mPath;
    private RectF mRectF;
    private int mWidth;
    private int mHeight;

    public CornerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPath = new Path();
        mRectF = new RectF();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null); //关闭硬件加速
//        setBackgroundColor(Color.TRANSPARENT);
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
