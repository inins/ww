package com.wang.social.login.mvp.model.entities;


import com.frame.component.entities.User;
import com.frame.http.api.BaseJson;

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
    private String token;
    private User userInfo;
}