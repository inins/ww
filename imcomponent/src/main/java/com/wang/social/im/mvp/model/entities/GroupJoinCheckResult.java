package com.wang.social.im.mvp.model.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 9:08
 * ============================================
 */
@Data
public class GroupJoinCheckResult {

    private String applyId;
    private boolean needPay;
    private int joinCost;
    private boolean validation;
}
