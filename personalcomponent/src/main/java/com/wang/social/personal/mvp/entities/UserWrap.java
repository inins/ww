package com.wang.social.personal.mvp.entities;

import com.frame.component.entities.User;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/3.
 */

public class UserWrap implements Serializable{
    private User userInfo;

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
