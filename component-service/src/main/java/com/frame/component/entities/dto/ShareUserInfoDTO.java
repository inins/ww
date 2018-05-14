package com.frame.component.entities.dto;

import com.frame.component.entities.ShareUserInfo;
import com.frame.http.api.Mapper;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 15:42
 * ============================================
 */
public class ShareUserInfoDTO implements Mapper<ShareUserInfo> {

    private String user_id;
    private String nickname;
    private String avatar;
    private int shareNum;

    @Override
    public ShareUserInfo transform() {
        ShareUserInfo userInfo = new ShareUserInfo();
        userInfo.setUserId(user_id == null ? "" : user_id);
        userInfo.setNickname(nickname == null ? "" : nickname);
        userInfo.setPortrait(avatar == null ? "" : avatar);
        userInfo.setReferrals(shareNum);
        return userInfo;
    }
}
