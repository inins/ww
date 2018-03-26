package com.wang.social.login.mvp.model.entities.dto;

import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.http.api.Mapper;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:12
 * ========================================
 */

public class LoginInfoDTO implements Mapper<LoginInfo>{

    private String token;

    @Override
    public LoginInfo transform() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setToken(token == null ? "" : token);
        return loginInfo;
    }
}