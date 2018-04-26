package com.wang.social.funshow.common;

import android.text.style.ForegroundColorSpan;

public class StringColorSpan extends ForegroundColorSpan {
    protected String mKeyWords;

    public StringColorSpan(int color, String keyWords) {
        super(color);
        mKeyWords = keyWords;
    }

    public String keyWords() {
        return mKeyWords;
    }
}
