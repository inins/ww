package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface LoginPath extends BasePath{

    String HOST = "parentTagList/";

    //登陆页面
    String LOGIN_PATH = "login";
    //标签选择页面
    String TAG_PATH = "tag_select";

    String LOGIN_URL = SCHEME + HOST + LOGIN_PATH;
    String TAG_URL = SCHEME + HOST + TAG_PATH;
}