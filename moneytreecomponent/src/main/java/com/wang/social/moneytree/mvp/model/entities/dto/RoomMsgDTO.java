package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.RoomMsg;

import lombok.Data;

@Data
public class RoomMsgDTO implements Mapper<RoomMsg> {
    private Integer groupId;
    private Integer resetTime;
    private Integer peopleNum;
    private Integer joinNum;
    private Integer diamond;
    private Integer isJoin;
    private Integer isGroupMember;
    private Integer creatorId;
    private String roomName;
    private Integer gameNum;
    private Integer type;
    private Integer gameType;
    private Integer totalDiamond;

    @Override
    public RoomMsg transform() {
        RoomMsg object = new RoomMsg();

        object.setGroupId(EntitiesUtil.assertNotNull(groupId));
        object.setResetTime(EntitiesUtil.assertNotNull(resetTime));
        object.setPeopleNum(EntitiesUtil.assertNotNull(peopleNum));
        object.setJoinNum(EntitiesUtil.assertNotNull(joinNum));
        object.setDiamond(EntitiesUtil.assertNotNull(diamond));
        object.setIsJoin(EntitiesUtil.assertNotNull(isJoin));
        object.setIsGroupMember(EntitiesUtil.assertNotNull(isGroupMember));
        object.setCreatorId(EntitiesUtil.assertNotNull(creatorId));
        object.setRoomName(EntitiesUtil.assertNotNull(roomName));
        object.setGameNum(EntitiesUtil.assertNotNull(gameNum));
        object.setType(EntitiesUtil.assertNotNull(type));
        object.setGameType(EntitiesUtil.assertNotNull(gameType));
        object.setTotalDiamond(EntitiesUtil.assertNotNull(totalDiamond));

        return object;
    }
}
