package com.wang.social.im.mvp.model.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 14:31
 * ============================================
 */
@Data
public class EnvelopAdoptInfo {

    private String userId;
    private String portrait;
    private String nickname;
    private long time;
    private int gotDiamondNumber;
    private boolean lucky;
}