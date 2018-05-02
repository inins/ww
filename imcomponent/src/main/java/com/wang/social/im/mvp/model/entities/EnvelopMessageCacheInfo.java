package com.wang.social.im.mvp.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================
 * 红包消息本地缓存信息
 * <p>
 * Create by ChenJing on 2018-04-25 10:31
 * ============================================
 */
@NoArgsConstructor
public class EnvelopMessageCacheInfo {

    /**
     * 红包未领取
     */
    public static final int STATUS_INITIAL = 0;
    /**
     * 红包已领取
     */
    public static final int STATUS_ADOPTED = 1;
    /**
     * 红包已过期
     */
    public static final int STATUS_OVERDUE = 2;
    /**
     * 红包已抢光
     */
    public static final int STATUS_EMPTY = 3;

    @Getter
    @Setter
    private int status = STATUS_INITIAL;
    @Getter
    @Setter
    private int gotDiamond;
}