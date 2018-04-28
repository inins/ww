package com.wang.social.funshow.mvp.entities.post;

import lombok.Data;

@Data
public class UserPost {
    private int userId;

    public UserPost(int userId) {
        this.userId = userId;
    }
}
