package com.wang.social.im.mvp.model.entities;

import lombok.Getter;
import lombok.Setter;

import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_GAME;

/**
 * ============================================
 * 摇钱树消息Elem
 * <p>
 * Create by ChenJing on 2018-05-18 11:01
 * ============================================
 */
public class GameElemData {

    @Getter
    private String type = CUSTOM_ELEM_GAME;

    /**
     * 游戏房间ID
     */
    @Getter
    @Setter
    private String roomId;

    /**
     * 参加游戏的钻石数量
     */
    @Getter
    @Setter
    private int diamond;
}
