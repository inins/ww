package com.wang.social.moneytree.mvp.model.entities;

import lombok.Data;

@Data
public class GameBean {
    /**
     * roomId：游戏房间id；
     * roomAvatar:房间头像；
     * roomNickname:房间昵称；
     * diamond：参与游戏付费钻数；
     * peopleNum：满足游戏开始的人数；
     * gameId:当前进行的游戏id；
     * groupId:创建游戏的群id；
     * joinNum：已加入游戏的人数；
     * reset Time：游戏重置时间（单位为S）；
     * gameCreator:游戏创建者;
     * joinRoomId：该用户加入的房间号
     */
    private int roomId;
    private int gameId;
    private String groupId;
    private int joinNum;
    private int gameType;
    private int gameCreator;
    private String timeLen;
    private int peopleNum;
    private int isJoined;
    private int diamond;
    private String roomAvatar;
    private String roomNickname;
    private int resetTime;
}
