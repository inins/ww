package com.wang.social.im.mvp.model.entities.notify;

import lombok.Data;

@Data
public class SysMsg {

    /**
     * userId : 10010
     * sendUserId : null
     * nickname : Cele
     * type : 5
     * shareUserId : null
     * shareNickname : null
     * objectId : 24
     * name : 测试收费222222
     * content : Cele付费查看了你的话题测试收费222222，获得收益30
     * orderId : 93
     * money : 30.0
     * reason : 有共同话题的人就是知音，您的话题被测试收费222222查看，并获得30钻。
     * pass : 0
     * receiveUserId : 10013
     * title : 收益消息
     * state : 1
     * msgCategory : 0
     * createTime : 1524827157000
     * updateTime : 1524827157000
     * extras : null
     * msgId : 13
     */

    private int userId;
    private int sendUserId;
    private String nickname;
    private int type;
    private int shareUserId;
    private String shareNickname;
    private int objectId;
    private String name;
    private String content;
    private int orderId;
    private float money;
    private String reason;
    private int pass;
    private int receiveUserId;
    private String title;
    private int state;
    private int msgCategory;
    private long createTime;
    private long updateTime;
    private Object extras;
    private int msgId;
}
