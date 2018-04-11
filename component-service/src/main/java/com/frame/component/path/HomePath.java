package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface HomePath extends BasePath{

    String HOST = "app/";

    //登陆页面
    String HOME_PATH = "main";
    String HOME_URL = SCHEME + HOST + HOME_PATH;
}