package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.enums.MessageNotifyType;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 17:35
 * ============================================
 */
@Data
public class MemberInfo {

    //群成员
    public static final int ROLE_MEMBER = 0x001;
    //群主
    public static final int ROLE_MASTER = 0x001;

    private String groupId;
    private String memberId;
    private String nickname;
    private String portrait;
    //消息提醒类型
    private MessageNotifyType notifyType;
    //角色
    private int role;
}
