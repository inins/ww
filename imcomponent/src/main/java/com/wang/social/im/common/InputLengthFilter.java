package com.wang.social.im.common;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText输入长度过滤器
 * Created by ChenJing on 2015/9/16.
 */
public class InputLengthFilter implements InputFilter {

    private int maxLength = 0;
    private boolean filterChinese;

    public InputLengthFilter(int maxLength, boolean filterChinese) {
        this.maxLength = maxLength;
        this.filterChinese = filterChinese;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int sourceChineseCount = filterChinese ? countChinese(source.toString()) : 0;  //刚输入中文字符数
        int destChineseCount = filterChinese ? countChinese(dest.toString()) : 0;  //已经输入中文字符数
        int keep = maxLength - destChineseCount - (dest.length() - (dend - dstart));  //剩余字符数

        if (keep <= 0) {//超出限制长度
            return "";
        } else if (keep - sourceChineseCount >= end - start) {//未超出限制长度
            return null;
        } else {//超出限制长度，截取可输入字符
            char[] chars = source.toString().toCharArray();
            int k = keep;
            keep = 0;
            for (int i = 0; i < chars.length; i++) {
                if (filterChinese && isChinese(chars[i])) {
                    k = k - 2;
                } else {
                    k--;
                }
                if ((filterChinese && k <= 0) || k < 0) {
                    break;
                }
                keep++;
            }
            CharSequence charSequence = source.subSequence(start, start + keep);
            return charSequence;
        }
    }

    public static int countChinese(String string) {
        int count = 0;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (isChinese(c)) {
                count++;
            }
        }
        return count;
    }

    private final static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
