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
import android.webkit.WebView;

import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;

import timber.log.Timber;

public class CornerWebView extends WebView {
    private float top_left = 0;
    private float top_right = 0;
    private float bottom_left = 0;
    private float bottom_right = 0;
    private int vWidth;
    private int vHeight;
    private int x;
    private int y;
    private Paint paint1;
    private Paint paint2;

    private float[] radiusArray = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};


    public CornerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setAntiAlias(true);
        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);

        int size = SizeUtils.dp2px(16);
        top_left = size;
        top_right = size;
        bottom_left = size;
        bottom_right = size;

        setRadius(top_left, top_right, bottom_right, bottom_left);
    }

    /**
     * 设置四个角的圆角半径
     */
    public void setRadius(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        radiusArray[0] = leftTop;
        radiusArray[1] = leftTop;
        radiusArray[2] = rightTop;
        radiusArray[3] = rightTop;
        radiusArray[4] = rightBottom;
        radiusArray[5] = rightBottom;
        radiusArray[6] = leftBottom;
        radiusArray[7] = leftBottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vWidth = getMeasuredWidth();
        vHeight = getMeasuredHeight();
    }


    @Override
    public void onDraw(Canvas canvas) {
        Timber.i("onDraw 圆角WebView");
        x = this.getScrollX();
        y = this.getScrollY();
        Path path = new Path();
        path.addRoundRect(new RectF(0, y, x + vWidth, y + vHeight), radiusArray, Path.Direction.CW);        // 使用半角的方式，性能比较好
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
