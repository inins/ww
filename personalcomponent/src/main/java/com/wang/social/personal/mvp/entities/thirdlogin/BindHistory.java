package com.wang.social.personal.mvp.entities.thirdlogin;

import lombok.Data;

@Data
public class BindHistory {

    /**
     * id : 1
     * userId : 10001
     * bindType : 2
     * bindData1 : 1234
     * createTime : 1522142622000
     */

    private int id;
    private int userId;
    private int bindType;
    private String bindData1;
    private long createTime;

}
