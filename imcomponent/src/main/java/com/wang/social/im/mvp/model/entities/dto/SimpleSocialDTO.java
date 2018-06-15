package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.SimpleSocial;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 15:09
 * ============================================
 */
public class SimpleSocialDTO implements Mapper<SimpleSocial> {

    private String id;
    private String headUrl;
    private String groupName;

    @Override
    public SimpleSocial transform() {
        SimpleSocial social = new SimpleSocial();
        social.setSocialId(id == null ? "-1" : id);
        social.setAvatar(headUrl == null ? "" : headUrl);
        social.setName(groupName == null ? "" : groupName);
        return social;
    }
}
