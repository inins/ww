package com.frame.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.frame.component.service.R;

/**
 * Created by king on 2018/2/13.
 *
 * 圆形数字标记，自动改变TextSize来适应控件空间
 */

public class CircularAutoSizeTextView extends AppCompatTextView {
    // 半径
    int mRadius = 15;
    // 背景圆
    int mBgColor = Color.RED;
    Paint mPaint;
    // 边框
    int mBorderColor = Color.GRAY;
    int mBorderWidth = 2;
    Paint mBorderPaint;
    // 装文字的正方形的边长
    int mLengthOfSide;
    Paint mAutoSizeTextPaint;
    float mAutoSizeTextSize = 18.0f;
    int mAutoSizeTextColor = Color.WHITE;
    private int mTextYOffset = 0;

    int mCenterX;
    int mCenterY;

    public CircularAutoSizeTextView(Context context) {
        this(context, null);
    }

    public CircularAutoSizeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularAutoSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (null != context && null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ps_CircularAutoSizeTextView);

            float scale = context.getResources().getDisplayMetrics().density;

            // 半径
            mRadius = typedArray.getDimensionPixelSize(R.styleable.ps_CircularAutoSizeTextView_ps_Radius, 30);
            mBgColor = typedArray.getColor(R.styleable.ps_CircularAutoSizeTextView_ps_BackgroundColor, Color.RED);
            mBorderColor = typedArray.getColor(R.styleable.ps_CircularAutoSizeTextView_ps_BorderColor, Color.GRAY);
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.ps_CircularAutoSizeTextView_ps_BorderWidth, 5);
            mAutoSizeTextSize = typedArray.getDimensionPixelSize(R.styleable.ps_CircularAutoSizeTextView_ps_TextSize, 18) / scale;
            mAutoSizeTextColor = typedArray.getColor(R.styleable.ps_CircularAutoSizeTextView_ps_TextColor, Color.WHITE);

            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);


        mAutoSizeTextPaint = new Paint();
        mAutoSizeTextPaint.setAntiAlias(true);
        mAutoSizeTextPaint.setTextAlign(Paint.Align.CENTER);
        mAutoSizeTextPaint.setColor(mAutoSizeTextColor);
        mAutoSizeTextPaint.setTextSize(mAutoSizeTextSize);
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setBackgroundColor(int color) {
        mBgColor = color;
        mPaint.setColor(mBgColor);
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
        mBorderPaint.setColor(mBorderColor);
    }

    public void setTextYOffset(int textYOffset) {
        mTextYOffset = textYOffset;
    }

    public void setBorderWidth(int width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public void setTextColor(int color) {
        mAutoSizeTextColor = color;
        mAutoSizeTextPaint.setColor(mAutoSizeTextColor);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置控件大小
        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);


        String text = getText().toString();
        if (text.length() <= 0) return;

        // 计算装文字的正方形
        mLengthOfSide = mRadius * 2 - mBorderWidth * 2;
        mLengthOfSide = (int)(Math.sqrt(2) * mRadius);

        do {
            Rect rect = new Rect();
            mAutoSizeTextPaint.getTextBounds(text, 0, text.length(), rect);

            // 文字范围必须在正方形内
            if (rect.right - rect.left <= mLengthOfSide && rect.bottom - rect.top <= mLengthOfSide) {
                break;
            }

            mAutoSizeTextSize = mAutoSizeTextSize - 1;
            mAutoSizeTextPaint.setTextSize(mAutoSizeTextSize);

            Log.i("", "RESET SIZE : " + Float.toString(mAutoSizeTextSize));

            if (mAutoSizeTextSize <= 1) {
                break;
            }

        } while (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        int radius = mRadius;
        if (mBorderWidth > 0) {
            radius = mRadius - mBorderWidth / 2;
        }

        canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);

        Rect rect = new Rect();
        mAutoSizeTextPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), rect);


//        Paint.FontMetrics fontMetrics = mAutoSizeTextPaint.getFontMetrics();
//        float top = fontMetrics.top;//为 基线到字体上边框的距离,即上图中的top
//        float bottom = fontMetrics.bottom;// 为基线到字体下边框的距离,即上图中的bottom
//        Rect r = new Rect();
//        r.set(getLeft(), getTop(), getRight(), getBottom());
//        int baseLineY = (int) (r.centerY() - top/2 - bottom/2);// 基线中间点的y轴计算公式

        canvas.drawText(getText().toString(), mCenterX, mCenterY + (rect.height() / 2) + mTextYOffset, mAutoSizeTextPaint);

        if (mBorderWidth > 0) {
            canvas.drawCircle(mCenterX, mCenterY, radius, mBorderPaint);
        }
    }
}
