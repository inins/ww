package com.wang.social.im.mvp.model.entities;

import android.text.TextUtils;

import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.ext.group.TIMGroupBasicSelfInfo;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 11:21
 * ============================================
 */
public class GroupProfile implements ProfileSummary{

    private TIMGroupDetailInfo profile;
    private TIMGroupBasicSelfInfo selfInfo;

    public GroupProfile(TIMGroupCacheInfo cacheInfo) {
        this.profile = cacheInfo.getGroupInfo();
        this.selfInfo = cacheInfo.getSelfInfo();
    }


    @Override
    public String getPortrait() {
        if (!TextUtils.isEmpty(profile.getFaceUrl())){
            return profile.getFaceUrl();
        }
        return "";
    }

    @Override
    public String getName() {
        if (!TextUtils.isEmpty(profile.getGroupName())){
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
    public TIMGroupMemberRoleType getRole(){
        return selfInfo.getRole();
    }

    /**
     * 获取消息接收状态
     */
    public TIMGroupReceiveMessageOpt getMessagOpt(){
        return selfInfo.getRecvMsgOption();
    }
}
