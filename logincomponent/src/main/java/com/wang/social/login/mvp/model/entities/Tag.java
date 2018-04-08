package com.wang.social.login.mvp.model.entities;

import lombok.Data;

@Data
public class Tag {
    private int id;
    private String tagName;
    // state:是否已选为个人标签(0：不是，1：是)
    private int state;

    public boolean isPersonalTag() {
        return getState() == 1;
    }
    public void clickTag() {
        state = state == 0 ? 1 : 0;
    }
}
