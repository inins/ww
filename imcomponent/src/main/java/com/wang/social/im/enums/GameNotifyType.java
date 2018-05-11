package com.wang.social.im.enums;

import com.wang.social.im.app.IMConstants;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 17:11
 * ============================================
 */
public enum GameNotifyType {

    /**
     * 创建
     */
    CREATE(IMConstants.GAME_NOTIFY_TYPE_CREATE),
    /**
     * 加入
     */
    JOIN(IMConstants.GAME_NOTIFY_TYPE_JOIN),
    /**
     * 结果
     */
    RESULT(IMConstants.GAME_NOTIFY_TYPE_RESULT);

    private String value;

    GameNotifyType(String value) {
        this.value = value;
    }

    public static GameNotifyType instanceOf(String value) {
        for (GameNotifyType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("value is not find");
    }
}