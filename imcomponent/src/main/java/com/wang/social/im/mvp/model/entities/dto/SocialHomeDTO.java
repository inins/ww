package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.SocialInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 10:22
 * ============================================
 */
public class SocialHomeDTO implements Mapper<SocialInfo> {

    private ShadowInfoDTO shadow;
    private MemberInfoDTO member;
    private SocialDTO group;

    @Override
    public SocialInfo transform() {
        SocialInfo social = group.transform();
        if (shadow != null) {
            social.setShadowInfo(shadow.transform());
        }
        if (member != null) {
            social.setMemberInfo(member.transform());
        }
        return social;
    }
}
