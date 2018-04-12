package com.wang.social.login.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Keys {
    public final static String NAME_MOBILE = "NAME_MOBILE";
    public final static String NAME_VERIFY_CODE = "NAME_VERIFY_CODE";

    public final static String EVENTBUS_TAG_DELETE = "EVENTBUS_TAG_DELETE";
    public final static String EVENTBUS_TAG_ENTITY = "EVENTBUS_TAG_ENTITY";

    public final static String NAME_MODE = "NAME_MODE";
    public final static String NAME_FROM_LOGIN = "NAME_FROM_LOGIN";
    public final static String NAME_TAG_TYPE = "NAME_TAG_TYPE";
    public final static String NAME_SELECTED_LIST = "NAME_SELECTED_LIST";
    public final static String NAME_PARENT_ID = "NAME_PARENT_ID";
    // 标签选择
    public final static String MODE_SELECTION = "MODE_SELECTION";
    // 确认标签选择
    public final static String MODE_CONFIRM = "MODE_CONFIRM";

    /**
     * 标签类型定义
     */
    public final static int TAG_TYPE_UNKNOWN = -1;  // 标签类型未知
    public final static int TAG_TYPE_INTEREST = 0;  // 兴趣标签
    public final static int TAG_TYPE_PERSONAL = 1;  // 个人标签
    @IntDef({
            TAG_TYPE_UNKNOWN,
            TAG_TYPE_INTEREST,
            TAG_TYPE_PERSONAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TagType {}

    /**
     * 密码修改类型
     */
    public final static int TYPE_RESET_PASSWORD = 1;
    public final static int TYPE_FORGOT_PASSWORD = 2;
    @IntDef({
            TYPE_RESET_PASSWORD,
            TYPE_FORGOT_PASSWORD
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypePasswordUI {}
}
