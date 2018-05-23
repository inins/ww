package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface FunshowPath extends BasePath {

    String HOST = "funshow";

    //发布趣晒页面
    String FUNSHOW_ADD_URL = SCHEME + HOST + "/add";

    //发布趣晒页面
    String FUNSHOW_DETAIL_URL = SCHEME + HOST + "/detail";

    //趣晒魔页面
    String FUNSHOW_HOTUSER_URL = SCHEME + HOST + "/hotUser";
}