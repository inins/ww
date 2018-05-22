package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.JoinGroupResult;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 9:15
 * ============================================
 */
public class JoinGroupResultDTO implements Mapper<JoinGroupResult> {

    private int state;    //1:加入群成功  2：已经发送加群申请

    @Override
    public JoinGroupResult transform() {
        JoinGroupResult joinGroupResult = new JoinGroupResult();
        joinGroupResult.setJoinState(state == 1 ? JoinGroupResult.STATE_JOIN_SUCCESS : JoinGroupResult.STATE_APPLY_SUCCESS);
        return joinGroupResult;
    }
}