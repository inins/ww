package com.frame.component.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 15:10
 * ============================================
 */
@Data
public class ShareUserInfo {

    private String userId;
    private String nickname;
    private String portrait;
    private int referrals;
}
