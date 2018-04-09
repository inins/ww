package com.wang.social.personal.helper;

import android.text.TextUtils;

import com.frame.utils.RegexUtils;

public class AppValiHelper {

    public static String feedback(String phone, String content) {
        if (TextUtils.isEmpty(content)) {
            return "请输入您要反馈的内容";
        } else if (!RegexUtils.isMobileSimple(phone)) {
            return "请输入正确的手机号";
        } else {
            return null;
        }
    }
}
