package com.frame.component.entities;

import lombok.Data;

@Data
public class GroupMemberInfo {
    private String memberName;
    private String memberHeadUrl;
    private int memberRole;
    private int userId;
    private int receiveMsgType;
}
