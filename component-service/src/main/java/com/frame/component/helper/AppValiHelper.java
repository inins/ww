package com.frame.component.helper;

import android.text.TextUtils;

import com.frame.utils.RegexUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;

import java.util.List;

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

    public static String addFunshow(String content, List<String> photoPaths, String videoPath) {
        if (TextUtils.isEmpty(content)) {
            return "请输入趣晒内容";
        } else if (StrUtil.isEmpty(photoPaths) && TextUtils.isEmpty(videoPath)) {
            return "请添加图片或视频";
        } else {
            return null;
        }
    }

    public static String newGuide(String name, String path, Integer sex, String birthday) {
        if (TextUtils.isEmpty(path)) {
            return "请选择头像";
        } else if (TextUtils.isEmpty(name)) {
            return "请输入昵称";
        } else if (!RegexUtils.isUsernameMe(name)) {
            return "昵称仅允许输入下划线符号";
        } else if (sex == null) {
            return "请选择性别";
        } else if (TextUtils.isEmpty(birthday)) {
            return "请选择生日";
        } else {
            return null;
        }
    }
}
