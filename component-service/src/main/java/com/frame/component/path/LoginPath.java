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
}