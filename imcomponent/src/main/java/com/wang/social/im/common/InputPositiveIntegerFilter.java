package com.wang.social.im.common;

import android.text.InputFilter;
import android.text.Spanned;

import com.frame.utils.RegexConstants;
import com.frame.utils.RegexUtils;

/**
 * {@link android.widget.EditText}输入正整数过滤器
 * Created by Bo on 2018-01-10.
 */

public class InputPositiveIntegerFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (start + dstart > 9) {
            return "";
        }
        if (dest.length() == 0 && source.equals("0")) {
            return "";
        }
        if (RegexUtils.isMatch(RegexConstants.REGEX_NOT_NEGATIVE_INTEGER, source)) {
            return source;
        }
        return "";
    }
}
