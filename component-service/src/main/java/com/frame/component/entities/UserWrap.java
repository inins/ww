package com.frame.component.entities;

import com.frame.component.entities.User;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/3.
 */

public class UserWrap implements Serializable{
    private User userInfo;

    private String token;
    private String userSig;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }
}
