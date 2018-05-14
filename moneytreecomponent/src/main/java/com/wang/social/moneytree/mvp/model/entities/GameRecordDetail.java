package com.wang.social.moneytree.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class GameRecordDetail {
    /**
     * isFinish:是否已完成(0：未完成，1：已完成)，
     * diamond:钻石数，
     * gameType:游戏类型（1：人数，2：时间）；
     * needPeapleNum：还差多少人；
     * timeLen:剩余时长；
     * gotDiamond:得到钻石；
     * isMyself:是否是自己;
     * gameNum:游戏局数;
     * type:创建类型（1：通过群，2：用户）
     */
    private int gameId;
    private int gameType;
    private int gameNum;
    private int joinNum;
    private int timeLen;
    private int groupId;
    private int isFinish;
    private int isGroupMember;
    private int type;
    private List<GameScore> list;
    private int peopleNum;
    private String roomName;
    private int roomId;
    private int diamond;
    private int needPeopleNum;
}
