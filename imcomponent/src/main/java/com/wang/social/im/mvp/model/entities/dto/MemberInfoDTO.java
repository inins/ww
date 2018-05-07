package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.mvp.model.entities.MemberInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 9:49
 * ============================================
 */
public class MemberInfoDTO implements Mapper<MemberInfo> {

    private String groupId;
    private String userId;
    private String memberName;
    private String memberHeadUrl;
    private int memberRole;
    private int receiveMsgType;//0全部1不提示2只提示@

    @Override
    public MemberInfo transform() {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setGroupId(groupId == null ? "-1" : groupId);
        memberInfo.setMemberId(userId == null ? "-1" : userId);
        memberInfo.setNickname(memberName == null ? "" : memberName);
        memberInfo.setPortrait(memberHeadUrl == null ? "" : memberHeadUrl);
        memberInfo.setRole(memberRole == 1 ? MemberInfo.ROLE_MASTER : MemberInfo.ROLE_MEMBER);
        switch (receiveMsgType) {
            case 0:
                memberInfo.setNotifyType(MessageNotifyType.ALL);
                break;
            case 1:
                memberInfo.setNotifyType(MessageNotifyType.NONE);
                break;
            case 2:
                memberInfo.setNotifyType(MessageNotifyType.ALERT);
                break;
        }
        return memberInfo;
    }
}
