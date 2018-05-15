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

    public static String deposit(int money, String account, String realName) {
        if (money == 0) {
            return "提现金额必须大于0";
        } else if (TextUtils.isEmpty(account)) {
            return "请输入支付宝账户";
        } else if (TextUtils.isEmpty(realName)) {
            return "请输入支付宝真实姓名";
        } else {
            return null;
        }
    }
}
