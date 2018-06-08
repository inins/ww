package com.wang.social.personal.mvp.entities.msg;

import lombok.Data;

@Data
public class MsgSetting {

    /**
     * id : 1
     * userId : 10001
     * pushFlag : 1
     * createTime : 1526874220000
     * updateTime : 1526874220000
     */

    private int id;
    private int userId;
    private int pushFlag;
    private long createTime;
    private long updateTime;

    public boolean isPushMsg() {
        return pushFlag == 0;
    }
}
