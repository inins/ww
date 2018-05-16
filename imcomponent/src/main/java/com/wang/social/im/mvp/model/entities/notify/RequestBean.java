package com.wang.social.im.mvp.model.entities.notify;

import com.frame.component.entities.Tag;

import java.util.List;

import lombok.Data;

@Data
public class RequestBean {

    private int msgId;
    private int userId;
    private boolean isAgree;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String reason;
    private long createTime;

    public boolean isMale() {
        return sex == 0;
    }
}
