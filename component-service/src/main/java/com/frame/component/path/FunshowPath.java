package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface FunshowPath extends BasePath {

    String HOST = "funshow/";

    //发布趣晒页面页面
    String FUNSHOW_PATH = "add";
    String FUNSHOW_URL = SCHEME + HOST + FUNSHOW_PATH;
}