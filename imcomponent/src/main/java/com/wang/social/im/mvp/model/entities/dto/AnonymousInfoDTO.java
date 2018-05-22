package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.AnonymousInfo;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 15:39
 * ============================================
 */
public class AnonymousInfoDTO implements Mapper<AnonymousInfo> {

    private List<String> list;

    @Override
    public AnonymousInfo transform() {
        if (list != null && !list.isEmpty()) {
            AnonymousInfo anonymousInfo = new AnonymousInfo();
            anonymousInfo.setNickname(list.get(0));
            return anonymousInfo;
        }
        return null;
    }
}
