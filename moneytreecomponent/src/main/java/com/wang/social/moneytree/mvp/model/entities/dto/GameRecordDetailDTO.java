package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.GameRecordDetail;
import com.wang.social.moneytree.mvp.model.entities.GameScore;

import java.util.ArrayList;
import java.util.List;

public class GameRecordDetailDTO implements Mapper<GameRecordDetail> {
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
    private Integer gameId;
    private Integer gameType;
    private Integer gameNum;
    private Integer joinNum;
    private Integer timeLen;
    private Integer groupId;
    private Integer isFinish;
    private Integer isGroupMember;
    private Integer type;
    private List<GameScore> list;
    private Integer peopleNum;
    private String roomName;
    private Integer roomId;
    private Integer diamond;
    private Integer needPeopleNum;

    @Override
    public GameRecordDetail transform() {
        GameRecordDetail object = new GameRecordDetail();

        object.setGameId(EntitiesUtil.assertNotNull(gameId));
        object.setGameType(EntitiesUtil.assertNotNull(gameType));
        object.setGameNum(EntitiesUtil.assertNotNull(gameNum));
        object.setJoinNum(EntitiesUtil.assertNotNull(joinNum));
        object.setTimeLen(EntitiesUtil.assertNotNull(timeLen));
        object.setGroupId(EntitiesUtil.assertNotNull(groupId));
        object.setIsFinish(EntitiesUtil.assertNotNull(isFinish));
        object.setIsGroupMember(EntitiesUtil.assertNotNull(isGroupMember));
        object.setType(EntitiesUtil.assertNotNull(type));
        object.setList(null == list ?  new ArrayList<>() : list);
        object.setPeopleNum(EntitiesUtil.assertNotNull(peopleNum));
        object.setRoomName(EntitiesUtil.assertNotNull(roomName));
        object.setRoomId(EntitiesUtil.assertNotNull(roomId));
        object.setDiamond(EntitiesUtil.assertNotNull(diamond));
        object.setNeedPeopleNum(EntitiesUtil.assertNotNull(needPeopleNum));

        return object;
    }
}
