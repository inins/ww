package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface Login202Path extends BasePath{

    String HOST = "login202/";

    //登陆页面
    String LOGIN_PATH = "login";
    String LOGIN_URL = SCHEME + HOST + LOGIN_PATH;
    // 标签选择
    String LOGIN_TAG_SELECTION = "login_tag_selection";
    String LOGIN_TAG_SELECTION_URL = SCHEME + HOST + LOGIN_TAG_SELECTION;

}