package com.wang.social.im.mvp.model.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

/**
 * ============================================
 * 趣聊信息
 * <p>
 * Create by ChenJing on 2018-05-02 9:29
 * ============================================
 */
@Data
public class SocialInfo {

    private String socialId;
    private String name;
    private String cover;
    private String avatar;
    private String desc;
    private String ageLimit;
    private SocialAttribute attr;
    private boolean createTeam;
    private List<Tag> tags;
    //分身信息
    private ShadowInfo shadowInfo;
    //我在趣聊中的信息
    private MemberInfo memberInfo;
}