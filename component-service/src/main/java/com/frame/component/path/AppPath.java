package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface AppPath extends BasePath{

    String HOST = "app";

    String HOME_URL = SCHEME + HOST + "/main";
    String SEARCH_URL = SCHEME + HOST + "/search";
}