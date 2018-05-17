package com.wang.social.im.mvp.model.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.imsdk.TIMUserProfile;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 13:32
 * ============================================
 */
public class FriendProfile implements ProfileSummary {

    private TIMUserProfile profile;

    public FriendProfile(@NonNull TIMUserProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getPortrait() {
        if (!TextUtils.isEmpty(profile.getFaceUrl())) {
            return profile.getFaceUrl();
        }
        return "";
    }

    @Override
    public String getName() {
        if (!TextUtils.isEmpty(profile.getRemark())) {
            return profile.getRemark();
        }
        if (!TextUtils.isEmpty(profile.getNickName())) {
            return profile.getNickName();
        }
        return "";
    }

    @Override
    public String getDescription() {
        if (!TextUtils.isEmpty(profile.getSelfSignature())) {
            return profile.getSelfSignature();
        }
        return "";
    }

    @Override
    public String getIdentify() {
        return profile.getIdentifier();
    }
}
