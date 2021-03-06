package com.wang.social.login.mvp.model.entities.dto;

import com.frame.component.entities.User;
import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.http.api.Mapper;
import com.frame.component.ui.acticity.tags.Tags;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:12
 * ========================================
 */

public class LoginInfoDTO implements Mapper<LoginInfo>{
    String token;
    String userSig;
    User userInfo;
    Tags userTags;

    @Override
    public LoginInfo transform() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setToken(token == null ? "" : token);
        loginInfo.setUserInfo(userInfo);
        loginInfo.setUserTags(userTags);
        loginInfo.setImSign(userSig);
        return loginInfo;
    }
}