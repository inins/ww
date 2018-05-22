package com.wang.social.im.mvp.model.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 9:15
 * ============================================
 */
public class JoinGroupResult {

    /**
     * 加入成功
     */
    public static final int STATE_JOIN_SUCCESS = 1;
    /**
     * 申请成功
     */
    public static final int STATE_APPLY_SUCCESS = 2;

    @Getter
    @Setter
    private int joinState;
}