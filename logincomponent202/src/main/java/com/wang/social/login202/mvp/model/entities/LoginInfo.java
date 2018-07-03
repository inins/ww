package com.wang.social.login202.mvp.model.entities;


import com.frame.component.entities.User;
import com.frame.component.ui.acticity.tags.Tags;

import lombok.Data;

/**
 * ========================================
 * 登陆用户信息实体
 * <p>
 * Create by ChenJing on 2018-03-20 14:12
 * ========================================
 */
@Data
public class LoginInfo {
    String token;
    String imSign;
    User userInfo;
    Tags userTags;
    boolean isBind;
    boolean isFirst;
}