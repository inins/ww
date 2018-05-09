package com.frame.component.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.frame.component.common.ConerBkSpan;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

public class ConerTextView extends android.support.v7.widget.AppCompatTextView {

    public ConerTextView(Context context) {
        super(context);
    }

    public ConerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            setPadding(0, SizeUtils.dp2px(3), 0, SizeUtils.dp2px(3));
            String s = getText().toString();
            setTagText(s);
        }
    }

    public void setTagText(String text) {
        setText(getConerSpanString(text));
    }

    private SpannableString getConerSpanString(String text) {
        String[] strs = text.split(",");
        int colorBk = Color.parseColor("#cccccc");
        int colorText = Color.parseColor("#ffffff");

        SpannableString spannableString = new SpannableString(text.replace(",", " "));

        int end = 0;
        for (int i = 0; i < strs.length; i++) {
            int start = end + 1;
            if (i == 0) start = 0;
            end = start + strs[i].length();
            ConerBkSpan roundedBackgroundSpan = new ConerBkSpan(colorBk, colorText);
            spannableString.setSpan(roundedBackgroundSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
