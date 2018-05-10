package com.wang.social.moneytree.mvp.model.entities;

import lombok.Data;

@Data
public class RoomMsg {
    /**
     * :isJoin:是否加入该游戏(0：未加入，1:加入);
     * isGroupMember:是否为群成员（0：不是，1：是）；
     * resetTime：重置时间，peopleNum：游戏开始人数，
     * joinNum：已加入人数，
     * diamond：游戏开始钻值;
     * creatorId:创建者id；
     * groupName:群组名称;
     * gameNum:该房间已进行的局数
     * type:创建类型（1：通过群，2：用户）;
     * gameType:游戏模式（1：人数，2：时间）
     */
    private int groupId;
    private int resetTime;
    private int peopleNum;
    private int joinNum;
    private int diamond;
    private int isJoin;
    private int isGroupMember;
    private int creatorId;
    private String roomName;
    private int gameNum;
    private int type;
    private int gameType;
}
