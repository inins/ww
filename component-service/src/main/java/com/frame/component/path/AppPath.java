package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface AppPath extends BasePath{

    String HOST = "app";

    String HOME_PATH = "/main";
    String SEARCH_PATH = "/search";

    String HOME_URL = SCHEME + HOST + HOME_PATH;
    String SEARCH_URL = SCHEME + HOST + SEARCH_PATH;
}