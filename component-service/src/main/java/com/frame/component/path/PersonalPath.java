package com.frame.component.path;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-23 17:27
 * =========================================
 */

public interface PersonalPath extends BasePath{

    String HOST = "personal";

    String RECHARGE_URL = SCHEME + HOST + "/recharge";
    String OFFICIAL_URL = SCHEME + HOST + "/officialPhoto";
    String ACCOUNT_URL = SCHEME + HOST + "/account";
}