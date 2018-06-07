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
    private int pass;   //0 未处理 1通过 2过期 3拒绝
    private int groupId;
    private String groupName;
    private String headUrl;
    private int memberNum;
    private long createTime;
    private int readState;

    public boolean isRead() {
        return readState == 1;
    }

    public boolean isDeal() {
        return pass != 0;
    }

    public String getStatusText() {
        switch (pass) {
            case 0:
                return "同意";
            case 1:
                return "已同意";
            case 2:
                return "已过期";
            case 3:
                return "已拒绝";
            default:
                return "";
        }
    }
}
