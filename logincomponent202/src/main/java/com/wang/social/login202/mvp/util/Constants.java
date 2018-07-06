package com.wang.social.login202.mvp.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {
    /**
     * （注册 type=1;
     * 找回密码 type=2;
     * 三方账号绑定手机 type=4;
     * 更换手机号 type=5;
     * 短信登录 type=6）
     */
    public static final int VERIFY_CODE_TYPE_REGISTER = 1;
    public static final int VERIFY_CODE_TYPE_FORGOT_PASSWORD = 2;
    public static final int VERIFY_CODE_TYPE_PLATFORM_BIND_PHONE = 4;
    public static final int VERIFY_CODE_TYPE_REPLACE_MOBILE = 5;
    public static final int VERIFY_CODE_TYPE_SMS_LOGIN = 6;

    @IntDef({
            VERIFY_CODE_TYPE_REGISTER,
            VERIFY_CODE_TYPE_FORGOT_PASSWORD,
            VERIFY_CODE_TYPE_PLATFORM_BIND_PHONE,
            VERIFY_CODE_TYPE_REPLACE_MOBILE,
            VERIFY_CODE_TYPE_SMS_LOGIN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface VerifyCodeType {}
}

