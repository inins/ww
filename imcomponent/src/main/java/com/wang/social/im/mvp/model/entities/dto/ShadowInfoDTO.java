package com.wang.social.im.mvp.model.entities.dto;

import com.frame.component.enums.Gender;
import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ShadowInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 10:23
 * ============================================
 */
public class ShadowInfoDTO implements Mapper<ShadowInfo> {

    private String groupId;
    private String nickname;
    private String avatar;
    private int state;
    private int gender; //性别：0女1男2迷

    @Override
    public ShadowInfo transform() {
        ShadowInfo shadow = new ShadowInfo();
        shadow.setNickname(nickname == null ? "" : nickname);
        shadow.setPortrait(avatar == null ? "" : avatar);
        shadow.setSocialId(groupId == null ? "-1" : groupId);
        switch (gender) {
            case 0:
                shadow.setGender(Gender.FEMALE);
                break;
            case 1:
                shadow.setGender(Gender.MALE);
                break;
            default:
                shadow.setGender(Gender.SECRET);
                break;
        }
        shadow.setStatus(state == 0 ? ShadowInfo.STATUS_CLOSE : ShadowInfo.STATUS_OPEN);
        return shadow;
    }
}
