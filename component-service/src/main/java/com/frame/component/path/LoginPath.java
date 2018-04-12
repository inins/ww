package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface LoginPath extends BasePath{

    String HOST = "login/";

    //登陆页面
    String LOGIN_PATH = "login";
    String LOGIN_URL = SCHEME + HOST + LOGIN_PATH;
    // 标签选择
    String LOGIN_TAG_SELECTION = "login_tag_selection";
    String LOGIN_TAG_SELECTION_URL = SCHEME + HOST + LOGIN_TAG_SELECTION;
    // 手机绑定
    String LOGIN_BIND_PHONE = "login_bind_phone";
    String LOGIN_BIND_PHONE_URL = SCHEME + HOST + LOGIN_BIND_PHONE;
    // 修改/重置密码
    String LOGIN_RESET_PASSWORD = "login_reset_password";
    String LOGIN_RESET_PASSWORD_URL = SCHEME + HOST + LOGIN_RESET_PASSWORD;
    // 验证手机
    String LOGIN_VERIFY_PHONE = "login_verify_phone";
    String LOGIN_VERIFY_PHONE_URL = SCHEME + HOST + LOGIN_VERIFY_PHONE;
}