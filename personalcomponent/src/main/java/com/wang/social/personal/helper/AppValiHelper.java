package com.wang.social.personal.helper;

import android.text.TextUtils;

import com.frame.utils.RegexUtils;

public class AppValiHelper {

    public static String feedback(String phone, String content, int picCount) {
        if (TextUtils.isEmpty(content) && picCount == 0) {
            return "请填入您要反馈的内容或者图片";
        } else if (!TextUtils.isEmpty(phone) && !RegexUtils.isMobileSimple(phone)) {
            return "请输入正确的手机号";
        } else {
            return null;
        }
    }
}
