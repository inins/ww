package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface FunshowPath extends BasePath {

    String HOST = "funshow";

    //发布趣晒页面页面
    String FUNSHOW_ADD_PATH = "/add";
    String FUNSHOW_ADD_URL = SCHEME + HOST + FUNSHOW_ADD_PATH;

    //发布趣晒页面页面
    String FUNSHOW_DETAIL_PATH = "/detail";
    String FUNSHOW_DETAIL_URL = SCHEME + HOST + FUNSHOW_DETAIL_PATH;
}