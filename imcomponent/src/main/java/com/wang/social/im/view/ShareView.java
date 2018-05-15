package com.wang.social.im.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.ShareModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Chao on 2017/8/11.
 */

public class ShareView extends ViewGroup {
    private int SIZE = SizeUtils.dp2px(60);
    private int INTERVAL = SizeUtils.dp2px(40);
    private int LEFTINTERVAL = SizeUtils.dp2px(15);
    private int width = 100;
    private int height = 100;
    Paint paint = new Paint();
    float scale = 1f;
    float x;
    float y;
    float rx;
    float ry;
    float distanceC;

    PointF startCenter;
    PointF mScaleCenter = new PointF();
    PointF mScaleXY = new PointF();
    PointF mCenter = new PointF();

    private List<ShareModel> data;
    private Map<Integer, Integer> levmap;
    private ImageLoader imageLoader;

    public ShareView(@NonNull Context context) {
        this(context, null);
    }

    public ShareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        paint.setColor(Color.RED);
        //paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (levmap != null) {
            for (int i = 0; i < data.size(); i++) {
                ImageView view = (ImageView) getChildAt(i);
                view.layout(data.get(i).getLeft(), data.get(i).getTop(), data.get(i).getRight(), data.get(i).getBottom());
            }
        }
        if (startCenter != null) {
            scrollTo(((int) startCenter.x - getWidth() / 2 + SIZE / 2), ((int) startCenter.y));
        } else {
            if (getChildAt(0) != null)
                scrollTo((int) getChildAt(0).getX() - getWidth() / 2 + SIZE / 2, (int) getChildAt(0).getY());
        }
    }


    float startX;
    float endX;
    float startY;
    float endY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null) return;
        canvas.scale(scale, scale/*, mCenter.x / scale, mCenter.y / scale*/);
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if (data.get(i).getSuperid() == data.get(j).getId()) {
                    if (data.get(i).getHaveMoney() == 1) {
                        paint.setColor(Color.parseColor("#da24a8"));
                    } else {
                        paint.setColor(Color.parseColor("#19d2fe"));
                    }
                    canvas.drawLine(getChildAt(j).getX() + SIZE / 2, getChildAt(j).getY() + SIZE, getChildAt(j).getX() + SIZE / 2, getChildAt(j).getY() + SIZE + INTERVAL / 2, paint);
                    canvas.drawLine(getChildAt(i).getX() + SIZE / 2, getChildAt(i).getY(), getChildAt(i).getX() + SIZE / 2, getChildAt(j).getY() + SIZE + INTERVAL / 2, paint);
                    startX = getChildAt(j).getX() + SIZE / 2;
                    endX = getChildAt(i).getX() + SIZE / 2;
                    startY = getChildAt(i).getY() - INTERVAL / 2;
                    endY = getChildAt(i).getY() - INTERVAL / 2;
                    canvas.drawLine(startX, startY, endX, endY, paint);
                }
            }
        }
        //canvas.drawCircle(0, 0, 10, paint);
        //canvas.drawCircle(mCenter.x, mCenter.y, 10, paint);
    }


    public void setData(List<ShareModel> data, Map<Integer, Integer> levmap, int maxWidth, int maxle, PointF startCenter) {
        if (imageLoader == null) {
            imageLoader = FrameUtils.obtainAppComponentFromContext(getContext()).imageLoader();
        }
        this.data = data;
        this.levmap = levmap;
        this.startCenter = startCenter;
        for (int i = 0; i < data.size(); i++) {
            ImageView iv = new ImageView(getContext());
            imageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(iv)
                    .url(data.get(i).getHeaderImage())
                    .build());
            addView(iv);
            width = Math.max(width, data.get(i).getRight());
        }
        //width = (maxWidth + 1) * SIZE + (maxWidth - 1) * INTERVAL;
        height = maxle * INTERVAL + SIZE * (maxle + 1);
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                distanceC = getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                mScaleXY.set(event.getX(1), event.getY(1));
                rx = (Math.max(mScaleXY.x, mScaleCenter.x) - Math.min(mScaleXY.x, mScaleCenter.x)) / 2 + Math.min(mScaleXY.x, mScaleCenter.x);
                ry = (Math.max(mScaleXY.y, mScaleCenter.y) - Math.min(mScaleXY.y, mScaleCenter.y)) / 2 + Math.min(mScaleXY.y, mScaleCenter.y);
                mCenter.set((rx + getScrollX()) / scale, (ry + getScrollY()) / scale);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                x = event.getX(event.getPointerCount() - 1 - event.getActionIndex());
                y = event.getY(event.getPointerCount() - 1 - event.getActionIndex());
                break;
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                mScaleCenter.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    float distance = getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    scale = (float) (scale + (distance - distanceC) * 0.0001);
                    scrollTo((int) (mCenter.x * scale - rx), (int) (mCenter.y * scale - ry));
                    if (scale > 2) {
                        scale = 2;
                    }
                    if (scale < 0.5) {
                        scale = 0.5f;
                    }
                    if (getScrollX() < 0) {
                        scrollTo(0, getScrollY());
                    }
                    if (getScrollY() < 0) {
                        scrollTo(getScrollX(), 0);
                    }
                    invalidate();
                } else if (event.getPointerCount() == 1) {
                    int moveX = (int) (x - event.getX());
                    int moveY = (int) (y - event.getY());
                    scrollBy(moveX, moveY);
                    x = event.getX();
                    y = event.getY();
//                    if (getScrollX() > (width) * scale - getWidth()) {
//                        scrollTo((int) ((width) * scale - getWidth()), getScrollY());
//                    }
//                    if (getScrollY() > (height - SIZE - INTERVAL) * scale - getHeight()) {
//                        scrollTo(getScrollX(), (int) ((height - SIZE - INTERVAL) * scale - getHeight()));
//                    }
//                    if (getScrollX() < 0) {
//                        scrollTo(0, getScrollY());
//                    }
//                    if (getScrollY() < 0) {
//                        scrollTo(getScrollX(), 0);
//                    }
                }
                break;
        }
        return true;
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

}
