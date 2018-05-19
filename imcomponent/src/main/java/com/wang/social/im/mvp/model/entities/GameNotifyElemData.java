package com.wang.social.im.mvp.model.entities;

import android.text.TextUtils;

import com.wang.social.im.enums.GameNotifyType;

import lombok.Getter;

/**
 * ============================================
 * 游戏通知elem
 * <p>
 * Create by ChenJing on 2018-05-11 17:08
 * ============================================
 */
public class GameNotifyElemData {

    /**
     * 用户ID
     */
    @Getter
    private String operatorUserId;
    /**
     * 用户昵称
     */
    @Getter
    private String operatorNickname;
    /**
     * 操作 {@link com.wang.social.im.app.IMConstants#GAME_NOTIFY_TYPE_CREATE}
     */
    private String operation;
    /**
     * 参加游戏人数
     */
    @Getter
    private int joinPersonNum;

    public GameNotifyType getNotifyType() {
        if (!TextUtils.isEmpty(operation)) {
            return GameNotifyType.instanceOf(operation);
        }
        return null;
    }
}