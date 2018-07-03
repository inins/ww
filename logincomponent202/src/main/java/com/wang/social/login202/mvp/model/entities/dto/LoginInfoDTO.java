package com.wang.social.login202.mvp.model.entities.dto;

import com.frame.component.entities.User;
import com.frame.component.ui.acticity.tags.Tags;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.login202.mvp.model.entities.LoginInfo;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:12
 * ========================================
 */

public class LoginInfoDTO implements Mapper<LoginInfo> {
    String token;
    String userSig;
    User userInfo;
    Tags userTags;
    Integer isBind;
    int isFirst;

    @Override
    public LoginInfo transform() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setToken(token == null ? "" : token);
        loginInfo.setUserInfo(userInfo);
        loginInfo.setUserTags(userTags);
        loginInfo.setImSign(userSig);
        loginInfo.setBind(EntitiesUtil.assertNotNull(isBind) == 1);
        loginInfo.setFirst(EntitiesUtil.assertNotNull(isFirst) == 1);

        return loginInfo;
    }
}