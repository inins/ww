package com.wang.social.im.mvp.model.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-23 9:45
 * ============================================
 */
@Data
public class GroupGameCheckResult {

    private boolean hasGame;
    private String roomId;
    private int diamondLimit;
}
