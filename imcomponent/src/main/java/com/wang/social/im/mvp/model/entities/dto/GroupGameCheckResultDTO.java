package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.GroupGameCheckResult;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-23 9:45
 * ============================================
 */
public class GroupGameCheckResultDTO implements Mapper<GroupGameCheckResult> {

    private int status; //是否有可加入游戏（0：有，1：没有）
    private String roomId; //房间ID
    private int diamondTop; //创建游戏上限

    @Override
    public GroupGameCheckResult transform() {
        GroupGameCheckResult result = new GroupGameCheckResult();
        result.setHasGame(status == 0);
        result.setRoomId(roomId == null ? "-1" : roomId);
        result.setDiamondLimit(diamondTop);
        return result;
    }
}
