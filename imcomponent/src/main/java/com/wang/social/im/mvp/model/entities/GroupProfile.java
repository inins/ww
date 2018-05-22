package com.wang.social.im.mvp.model.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.ext.group.TIMGroupBasicSelfInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.wang.social.im.app.IMConstants;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 11:21
 * ============================================
 */
public class GroupProfile implements ProfileSummary {

    public static final int GROUP_TYPE_SOCIAL = 0x001;
    public static final int GROUP_TYPE_TEAM = 0x002;
    public static final int GROUP_TYPE_MIRROR = 0x003;

    private TIMGroupDetailInfo profile;
    private TIMGroupBasicSelfInfo selfInfo;

    public GroupProfile(@NonNull TIMGroupCacheInfo cacheInfo) {
        this.profile = cacheInfo.getGroupInfo();
        this.selfInfo = cacheInfo.getSelfInfo();
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
        if (!TextUtils.isEmpty(profile.getGroupName())) {
            return profile.getGroupName();
        }
        return "";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getIdentify() {
        return profile.getGroupId();
    }

    /**
     * 获取自己身份
     */
    public TIMGroupMemberRoleType getRole() {
        return selfInfo.getRole();
    }

    /**
     * 获取消息接收状态
     */
    public TIMGroupReceiveMessageOpt getMessagOpt() {
        return selfInfo.getRecvMsgOption();
    }
}
