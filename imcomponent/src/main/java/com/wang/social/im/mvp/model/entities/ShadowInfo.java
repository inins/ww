package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.enums.Gender;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-04 19:30
 * ============================================
 */
@Data
public class ShadowInfo {

    private String socialId;
    private String portrait;
    private String nickname;
    private Gender gender;
}