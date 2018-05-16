package com.wang.social.im.mvp.model.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 11:14
 * ============================================
 */
@Data
public class ContactCheckResult {

    private String phoneNumber;
    private String userId;
    private boolean isFriend;
}
