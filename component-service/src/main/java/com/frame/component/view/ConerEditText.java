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
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class ConerEditText extends android.support.v7.widget.AppCompatEditText {

    boolean byUser = false;
    private String mCopy;

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
//                if (!byUser) {
//                    String s = charSequence.toString();
//                    SpannableString conerSpanString = getConerSpanString(s);
//                    byUser = true;
//                    setText(conerSpanString);
//                    setTextWithSelectionAtLast(ConerEditText.this);
//                } else {
//                    byUser = false;
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (!TextUtils.isEmpty(str) &&
                        (str.substring(str.length() - 1, str.length()).equals(" ") ||
                        str.length() < getCopyLen())) {
                    if (!byUser) {
                        String s = str;
                        SpannableString conerSpanString = getConerSpanString(s);
                        byUser = true;
                        setText(conerSpanString);
                        setTextWithSelectionAtLast(ConerEditText.this);
                    } else {
                        byUser = false;
                    }

                }

                mCopy = editable.toString();
            }
        });
    }

    private int getCopyLen() {
        return null == mCopy ? 0 : mCopy.length();
    }

    private SpannableString getConerSpanString(String text) {
        String[] strs = text.split(" ");
        int colorBk = Color.parseColor("#cccccc");
        int colorText = Color.parseColor("#ffffff");

        SpannableString spannableString = new SpannableString(text);

        int end = 0;
        for (int i = 0; i < strs.length; i++) {
            if (!text.endsWith(" ") && i == strs.length - 1) {
                //当最后一个字符不是空格时，最后一段视为内容，不显示为标签
                continue;
            }
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

    /**
     * 获取tags
     */
    public List<String> getTags() {
        List<String> tags = new ArrayList<>();
        Editable s = getText();
        ConerBkSpan[] spans = s.getSpans(0, s.length(), ConerBkSpan.class);
        for (int i = 0; i < spans.length; i++) {
            int start = s.getSpanStart(spans[i]);
            int end = s.getSpanEnd(spans[i]);
            String spanStr = s.toString().substring(start, end);
            tags.add(spanStr);
        }
        return tags;
    }

    //上面方法的重载，获取tags,以逗号分隔
    public String getTagsStr() {
        List<String> tags = getTags();
        String tagsStr = "";
        for (String tag : tags) {
            tagsStr += tag + ",";
        }
        return StrUtil.subLastChart(tagsStr, ",");
    }

    /**
     * 获取key
     */
    public String getKey() {
        String content = getText().toString();
        if (TextUtils.isEmpty(content.trim())) return "";
        String[] strs = content.split(" ");
        if (content.endsWith(" ")) {
            return "";
        } else {
            return strs[strs.length - 1];
        }
    }
}
