package com.frame.component.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class WWClickableSpan extends ClickableSpan {
    private int mTextColor;
    private View.OnClickListener mListener;

    public WWClickableSpan(int textColor, View.OnClickListener listener) {
        mTextColor = textColor;
        mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (null != mListener) {
            mListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mTextColor);
        ds.setUnderlineText(false);
    }
}
