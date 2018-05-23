package com.wang.social.im.mvp.model.entities.notify;

import lombok.Data;

@Data
public class GroupRequest {

    /**
     * msgId : 23
     * pass : 0
     * groupId : 2
     * groupName : 电视剧
     * headUrl :
     * memberNum : 2
     * createTime : 1525228466000
     */

    private int msgId;
    private int pass;
    private int groupId;
    private String groupName;
    private String headUrl;
    private int memberNum;
    private long createTime;

    public boolean isAgree() {
        return pass == 1;
    }
}
