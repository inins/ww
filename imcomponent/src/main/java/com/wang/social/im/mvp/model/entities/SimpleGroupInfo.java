package com.wang.social.im.mvp.model.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 16:13
 * ============================================
 */
@Data
public class SimpleGroupInfo {

    private String id;
    private String pid;
    private boolean social;
    private String masterUid;
    private String name;
    private String avatar;
    private int memberCount;
}