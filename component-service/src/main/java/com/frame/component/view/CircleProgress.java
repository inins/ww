package com.frame.component.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by king on 2017/12/12.
 */

public class CircleProgress extends View {
    public final static String TAG = CircleProgress.class.getSimpleName().toString();

    public final static double HALF_PI = Math.PI / 2;

    // 当前进度（0-100）
    int progress = 0;
    int animationProgress;
    // 圆环宽度
    int circleWidth = 4;
    // 圆环半径（居中顶边）
    int radius;
    // 中心点
    int centerX;
    int centerY;

    int mTextColor = Color.WHITE;
    int mTextSize = 18;
    int mStartColor = Color.GREEN;
    int mEndColor = Color.RED;

    

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);


        radius = (getWidth() > getHeight() ? getHeight() : getWidth()) / 2 - circleWidth / 2;

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleWidth);

        // 底部圆环
        paint.setColor(0xFFF2F2F2);
        canvas.drawCircle(centerX, centerY, radius, paint);

        // 绿到红着色器
        Shader shader = new SweepGradient(centerX, centerY, mStartColor, mEndColor);
        paint.setShader(shader);

        // 进度色条
        RectF rect = new RectF();
        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.save();
        canvas.rotate(-90, centerX, centerY);
        canvas.drawArc(rect, 0, getDegree(), false, paint);
        canvas.restore();

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        String text = "" + progress + "%";
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText("" + progress + "%", centerX, centerY + (bounds.height() / 2), textPaint);



//        // 测试信息
//        Paint textPaint = new Paint();
//        textPaint.setColor(Color.BLACK);
//        textPaint.setTextSize(20);
//        canvas.drawText("x : " + (int)dragX, 10, 20, textPaint);
//        canvas.drawText("y : " + (int)dragY, 10, 40, textPaint);
//
//        paint.setShader(null);
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(1);
//        canvas.drawRect(rect, paint);
//        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
//        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
    }

    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setStartColor(int startColor) {
        mStartColor = startColor;
    }

    public void setEndColor(int endColor) {
        mEndColor = endColor;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (this.progress == progress) {
            return;
        }

        progress = Math.min(100, progress);
        progress = Math.max(0, progress);
        this.progress = progress;

        invalidate();
    }

    public float getDegree() {
        return 360 * getProgress() / 100;
    }

}
