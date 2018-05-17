package com.wang.social.pictureselector.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wang.social.pictureselector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by king on 2016/1/2.
 */
public class DotIndicator extends LinearLayout {
    public final static String TAG = DotIndicator.class.getSimpleName().toString();

    private final static float[] SCALES = { 0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f };

    private Drawable mImage;
    private Drawable mImageSelected;
    private int mPadding;
    private int mNum;

    private int mIndex = -1;
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    public DotIndicator(Context context) {
        this(context, null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ps_DotIndicator);

        mImage = ta.getDrawable(R.styleable.ps_DotIndicator_ps_diImage);
        mImageSelected = ta.getDrawable(R.styleable.ps_DotIndicator_ps_diImageSelected);
        mPadding = ta.getDimensionPixelOffset(R.styleable.ps_DotIndicator_ps_diPadding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mNum = ta.getInteger(R.styleable.ps_DotIndicator_ps_diNum, 0);

        initView();
    }

    private void initView() {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);

        resetView(mNum, 0);
    }

    public void resetView(int num, int index) {
        this.removeAllViews();

        mNum = num;
        mIndex = -1;
        mImageViews.clear();

        for (int i = 0; i < mNum; i++) {
            ImageView imageView = new ImageView(getContext());



            imageView.setImageDrawable(mImage);
            if (i > 0) {
                imageView.setPadding(mPadding, 0, 0, 0);
            }
            imageView.setEnabled(false);
            mImageViews.add(imageView);
            this.addView(imageView);
        }

        select(index);
    }

    public void select(int index) {
        if (index >= 0 && index < mImageViews.size()) {
            if (index != mIndex) {
                mImageViews.get(index).setImageDrawable(mImageSelected);
                if (mIndex >= 0 && mIndex < mImageViews.size()) {
                    mImageViews.get(mIndex).setImageDrawable(mImage);
                }

//                for (int i = 0; i < mImageViews.size(); i++) {
//                    int scale = Math.min(Math.abs(index - i), SCALES.length - 1) % SCALES.length;
//                    if (i == index) {
//                        mImageViews.get(i).setScaleX(1.0f);
//                        mImageViews.get(i).setScaleY(1.0f);
//                    } else {
//                        mImageViews.get(i).setScaleX(SCALES[scale]);
//                        mImageViews.get(i).setScaleY(SCALES[scale]);
//                    }
//                }

                mIndex = index;
            }
        }
    }
}
