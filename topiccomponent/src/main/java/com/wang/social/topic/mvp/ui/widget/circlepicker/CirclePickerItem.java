package com.wang.social.topic.mvp.ui.widget.circlepicker;

import android.graphics.drawable.Drawable;

public class CirclePickerItem {
    private Drawable mDrawable;
    private String mText;
    private int mTextSize;

    public CirclePickerItem(Drawable drawable, String text, int textSize) {
        mDrawable = drawable;
        mText = text;
        mTextSize = textSize;
    }

    public CirclePickerItem(Drawable drawable) {
        mDrawable = drawable;
    }

    public CirclePickerItem(String text, int textSize) {
        mText = text;
        mTextSize = textSize;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }
}
