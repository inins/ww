package com.frame.component.entities.dto;

import com.frame.component.entities.GroupMemberInfo;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class GroupMemberInfoDTO implements Mapper<GroupMemberInfo> {
    private String memberName;
    private String memberHeadUrl;
    private Integer memberRole;
    private Integer userId;
    private Integer receiveMsgType;

    @Override
    public GroupMemberInfo transform() {
        GroupMemberInfo object = new GroupMemberInfo();

        object.setMemberName(EntitiesUtil.assertNotNull(memberName));
        object.setMemberHeadUrl(EntitiesUtil.assertNotNull(memberHeadUrl));
        object.setMemberRole(EntitiesUtil.assertNotNull(memberRole));
        object.setUserId(EntitiesUtil.assertNotNull(userId));
        object.setReceiveMsgType(EntitiesUtil.assertNotNull(receiveMsgType));

        return object;
    }
}
