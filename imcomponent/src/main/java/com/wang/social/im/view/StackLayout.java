package com.wang.social.im.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.wang.social.im.R;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 16:53
 * ============================================
 */
public class StackLayout extends ViewGroup {

    private static final long MAX_DURATION = 350L;

    //布局错开的距离
    private int mOffsetX;
    //定义在X方向上移动的判定值
    private float mTouchSlop;
    //当前显示在最上层View的Id
    private int mShowViewId = R.id.im_sl_upper_view;
    //记录手指按下时的X坐标
    private float mTouchDownX;
    //手指在X方向上移动的距离
    private float mTouchMoveX;
    //移动时子View在X方向的移动距离
    private int mMoveX;
    //记录当前一次移动最上层的View是否改变过
    private boolean mShowViewChanged;
    //切换显示View的临界值
    private int mCritical;
    //X方向最多移动的距离
    private int mMaxMoveX;

    public StackLayout(@NonNull Context context) {
        this(context, null);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.im_StackLayout, defStyleAttr, 0);
        mOffsetX = array.getDimensionPixelSize(R.styleable.im_StackLayout_im_sl_xoffset, 100);
        array.recycle();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
        }
        int measureWidth = measureWidth(widthMeasureSpec, childWidth);
        int measureHeight = measureHeight(heightMeasureSpec, childHeight);
        setMeasuredDimension(measureWidth, measureHeight);

        mCritical = (childWidth - mOffsetX) / 2;
        mMaxMoveX = mCritical * 2 + mOffsetX;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View lowerView = findViewById(R.id.im_sl_lower_view);
        View upperView = findViewById(R.id.im_sl_upper_view);

        View showView = mShowViewId == lowerView.getId() ? lowerView : upperView;
        View hideView = mShowViewId == lowerView.getId() ? upperView : lowerView;

        int showWidth = showView.getMeasuredWidth();
        int showHeight = showView.getMeasuredHeight();
        showView.layout(mOffsetX + mMoveX, 0, showWidth + mOffsetX + mMoveX, showHeight);

        int hideWidth = hideView.getMeasuredWidth();
        int hideHeight = hideView.getMeasuredHeight();
        hideView.layout(0 - mMoveX, 0, hideWidth + (0 - mMoveX), hideHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mMoveX = 0;
                mShowViewChanged = false;
                mTouchDownX = ev.getX();
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                //计算在X方向移动的距离
                mTouchMoveX = mTouchDownX - ev.getX();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE && Math.abs(mTouchMoveX) > mTouchSlop) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (mMoveX == -mOffsetX) {
                    return false;
                }
                //移动子控件
                mMoveX = (int) (event.getX() - mTouchDownX);
                if (mMoveX < 0) {
                    return false;
                }
                if (mMoveX > mMaxMoveX) {
                    mMoveX = mMaxMoveX;
                }
                //判断当前是否已经移动到临界位置
                if (mMoveX >= mCritical) {
                    if (!mShowViewChanged) {
                        mShowViewChanged = true;
                        stackSwitch();
                    }
                    mMoveX = mCritical - (mMoveX - mCritical);
                } else if (mShowViewChanged) {
                    mShowViewChanged = false;
                    stackSwitch();
                }
                requestLayout();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mMoveX == -mOffsetX) {
                    return false;
                }
                //格式化子控件位置
                int moveX = (int) (event.getX() - mTouchDownX);
                if (moveX > 0 && moveX != mMaxMoveX) {
                    formatChildLocation(moveX);
                } else if (mShowViewChanged) {
                    mShowViewChanged = false;
                    changeShowView();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void formatChildLocation(int moveX) {
        int endMoveX;
        if (mShowViewChanged) {
            endMoveX = mMaxMoveX;
        } else {
            endMoveX = 0;
        }
        ValueAnimator animator = ValueAnimator.ofInt(moveX, endMoveX);
        long duration = (long) (MAX_DURATION * (Math.abs(moveX - endMoveX) / (float) mMaxMoveX));
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveX = (int) animation.getAnimatedValue();

                if (mMoveX >= mCritical) {
                    mMoveX = mCritical - (mMoveX - mCritical);
                }
                requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mShowViewChanged) {
                    mShowViewChanged = false;
                    changeShowView();
                }
                mMoveX = 0;
                requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void stackSwitch() {
        if (mShowViewChanged) {
            if (mShowViewId == R.id.im_sl_upper_view) {
                findViewById(R.id.im_sl_lower_view).bringToFront();
            } else {
                findViewById(R.id.im_sl_upper_view).bringToFront();
            }
        } else {
            if (mShowViewId == R.id.im_sl_upper_view) {
                findViewById(R.id.im_sl_upper_view).bringToFront();
            } else {
                findViewById(R.id.im_sl_lower_view).bringToFront();
            }
        }
    }

    private void changeShowView() {
        mShowViewId = mShowViewId == R.id.im_sl_lower_view ? R.id.im_sl_upper_view : R.id.im_sl_lower_view;
    }

    private int measureWidth(int measureSpec, int viewWidth) {
        int width = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                width = viewWidth + mOffsetX;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                width = measureSpec;
                break;
        }
        return width;
    }

    private int measureHeight(int measureSpec, int viewHeight) {
        int height = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                height = viewHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                height = specSize;
                break;
        }
        return height;
    }
}