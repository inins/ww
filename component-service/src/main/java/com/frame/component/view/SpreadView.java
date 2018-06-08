package com.frame.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.frame.component.service.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpreadView extends View {

    private Paint centerPaint; //中心圆paint
    private Paint spreadPaint; //扩散圆paint
    private int radius = 100; //中心圆半径
    private int maxRadius = 300; //最大扩散半径
    private float centerX;//圆心x
    private float centerY;//圆心y
    private int spaceCircle = 80; //每次圆递增间距
    private int distance = 5; //每帧圆半径增长
    private int delayMilliseconds = 30;//每帧间隔时间
    private int perAlpha = 3;//每帧透明度减少
    private int spreadColor;
    private int maxAlpha;

    private boolean isStart;
    private boolean _isStart;

    private List<Wave> waveList = new ArrayList<>();

    public SpreadView(Context context) {
        this(context, null);
    }

    public SpreadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpreadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initBase();
    }

    private void initAttr(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpreadView, 0, 0);
        spreadColor = a.getColor(R.styleable.SpreadView_spread_spread_color, ContextCompat.getColor(getContext(), R.color.common_blue));
        radius = a.getDimensionPixelOffset(R.styleable.SpreadView_spread_radius, radius);
        maxRadius = a.getDimensionPixelOffset(R.styleable.SpreadView_spread_max_radius, maxRadius);
        distance = a.getDimensionPixelOffset(R.styleable.SpreadView_spread_distance, distance);
        spaceCircle = a.getDimensionPixelOffset(R.styleable.SpreadView_spread_space_circle, spaceCircle);
        delayMilliseconds = a.getInt(R.styleable.SpreadView_spread_delay_milliseconds, delayMilliseconds);
        perAlpha = a.getInt(R.styleable.SpreadView_spread_per_alpha, perAlpha);
        a.recycle();
    }

    private void initBase() {
        maxAlpha = Color.alpha(spreadColor);
        centerPaint = new Paint();
        centerPaint.setColor(spreadColor);
        centerPaint.setAntiAlias(true);
        spreadPaint = new Paint();
        spreadPaint.setAntiAlias(true);
        spreadPaint.setColor(spreadColor);
        if (isInEditMode()) {
            //编辑器预览
            int spaceAll = maxRadius - radius;
            int circleCount = spaceAll / spaceCircle;
            for (int i = 0; i < circleCount; i++) {
                waveList.add(new Wave(radius + spaceCircle * i < maxRadius ? radius + spaceCircle * i : maxRadius, maxAlpha / (circleCount + 1)));
            }
            waveList.add(new Wave(maxRadius, maxAlpha / (circleCount + 1)));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆心位置
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < waveList.size(); i++) {
            Wave wave = waveList.get(i);
            spreadPaint.setAlpha(wave.alpha);
            //绘制扩散的圆
            canvas.drawCircle(centerX, centerY, wave.radius, spreadPaint);

            //每次扩散圆半径递增，圆透明度递减
            wave.alpha = wave.alpha - perAlpha >= 0 ? wave.alpha - perAlpha : 0;
            if (wave.radius < maxRadius) {
                wave.radius += distance;
            }
        }

        if (waveList.size() > 0) {
            //当最内层扩散圆半径达到最大间距时，在内层添加一个新圆
            if (waveList.get(waveList.size() - 1).radius > spaceCircle + radius) {
                if (isStart) waveList.add(new Wave(radius, maxAlpha));
            }
            //把已经变为透明的圆移除掉
            Iterator<Wave> iterator = waveList.iterator();
            while (iterator.hasNext()) {
                Wave next = iterator.next();
                if (next.alpha <= 0) {
                    iterator.remove();
                }
            }
            //中间的圆
            //canvas.drawCircle(centerX, centerY, radius, centerPaint);
            //延迟更新，达到扩散视觉差效果
            postInvalidateDelayed(delayMilliseconds);
        }

        Log.e("test:", waveList.size() + "");
    }

    //////////////////////

    public void start() {
        isStart = true;
        //最开始不透明且扩散距离为0
        waveList.add(new Wave(radius, maxAlpha));
        postInvalidate();
    }

    public void end() {
        isStart = false;
    }

    private class Wave {
        public Wave(int radius, int alpha) {
            this.radius = radius;
            this.alpha = alpha;
        }

        public int radius;
        public int alpha;
    }
}
