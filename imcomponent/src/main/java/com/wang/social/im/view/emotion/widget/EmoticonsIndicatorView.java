package com.wang.social.im.view.emotion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.frame.utils.SizeUtils;

import java.util.ArrayList;

import com.wang.social.im.R;
import com.wang.social.im.view.emotion.data.PageSetEntity;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class EmoticonsIndicatorView extends LinearLayout {

    private static final int MARGIN_LEFT = 4;
    protected Context mContext;
    protected ArrayList<ImageView> mImageViews;
    protected Drawable mDrawableSelect;
    protected Drawable mDrawableNomal;
    protected LayoutParams mLeftLayoutParams;

    public EmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.im_EmoticonsIndicatorView, 0, 0);

        try {
            mDrawableSelect = a.getDrawable(R.styleable.im_EmoticonsIndicatorView_im_eiv_select);
            mDrawableNomal = a.getDrawable(R.styleable.im_EmoticonsIndicatorView_im_eiv_normal);
        } finally {
            a.recycle();
        }

        if(mDrawableNomal == null) {
            mDrawableNomal = getResources().getDrawable(R.drawable.im_emotion_indicator_point_normal);
        }
        if(mDrawableSelect == null) {
            mDrawableSelect = getResources().getDrawable(R.drawable.im_emotion_indicator_point_select);
        }

        mLeftLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftLayoutParams.leftMargin = SizeUtils.dp2px(MARGIN_LEFT);
    }

    public void playTo(int position, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        for (ImageView iv : mImageViews) {
            iv.setImageDrawable(mDrawableNomal);
        }
        mImageViews.get(position).setImageDrawable(mDrawableSelect);
    }

    public void playBy(int startPosition, int nextPosition, PageSetEntity pageSetEntity) {
        if (!checkPageSetEntity(pageSetEntity)) {
            return;
        }

        updateIndicatorCount(pageSetEntity.getPageCount());

        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }

        if (startPosition < 0) {
            startPosition = nextPosition = 0;
        }

        final ImageView imageViewStrat = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        imageViewStrat.setImageDrawable(mDrawableNomal);
        imageViewNext.setImageDrawable(mDrawableSelect);
    }

    protected boolean checkPageSetEntity(PageSetEntity pageSetEntity) {
        if (pageSetEntity != null && pageSetEntity.isShowIndicator()) {
            setVisibility(VISIBLE);
            return true;
        } else {
            setVisibility(GONE);
            return false;
        }
    }

    protected void updateIndicatorCount(int count) {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        if (count > mImageViews.size()) {
            for (int i = mImageViews.size(); i < count; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageDrawable(i == 0 ? mDrawableSelect : mDrawableNomal);
                this.addView(imageView, mLeftLayoutParams);
                mImageViews.add(imageView);
            }
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
            }
        }
    }
}