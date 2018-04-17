package com.wang.social.funpoint.mvp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.wang.social.funpoint.common.ConerBkSpan;

public class ConerEditText extends android.support.v7.widget.AppCompatEditText {

    boolean byUser = false;

    public ConerEditText(Context context) {
        super(context);
    }

    public ConerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!byUser) {
                    String s = charSequence.toString();
                    SpannableString conerSpanString = getConerSpanString(s);
                    byUser = true;
                    setText(conerSpanString);
                    setTextWithSelectionAtLast(ConerEditText.this);
                } else {
                    byUser = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private SpannableString getConerSpanString(String text) {
        String[] strs = text.trim().split(" ");
        int colorBk = Color.parseColor("#cccccc");
        int colorText = Color.parseColor("#ffffff");

        SpannableString spannableString = new SpannableString(text);

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

    private static void setTextWithSelectionAtLast(EditText editText) {
        editText.setSelection(editText.getText().length());
    }
}
