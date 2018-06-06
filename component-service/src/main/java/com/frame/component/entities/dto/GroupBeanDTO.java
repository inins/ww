package com.frame.component.entities.dto;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.frame.component.utils.EntitiesUtil;
import com.frame.component.entities.GroupBean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GroupBeanDTO implements Mapper<GroupBean> {
    private Integer id;
    private Integer pid;
    private Integer groupId;
    private String groupName;
    private String groupHeadUrl;
    private String groupCoverPlan;
    private List<Tag> tags;
    private List<Tag> groupTags;
    private Integer isOpen;
    private Integer isFree;
    private Integer memberNum;
    private Integer groupMemberNum;

    @Override
    public GroupBean transform() {
        GroupBean object = new GroupBean();

        if (null != groupId) {
            object.setGroupId(EntitiesUtil.assertNotNull(groupId));
        } else {
            object.setGroupId(EntitiesUtil.assertNotNull(id));
        }

        object.setPid(EntitiesUtil.assertNotNull(pid));
        object.setGroupName(EntitiesUtil.assertNotNull(groupName));
        object.setGroupHeadUrl(EntitiesUtil.assertNotNull(groupHeadUrl));
        object.setGroupCoverPlan(EntitiesUtil.assertNotNull(groupCoverPlan));
        if (null != tags) {
            object.setTags(tags);
        } else if (null != groupTags) {
            object.setTags(groupTags);
        } else {
            object.setTags(new ArrayList<>());
        }
        object.setIsOpen(EntitiesUtil.assertNotNull(isOpen));
        object.setIsFree(EntitiesUtil.assertNotNull(isFree));
        object.setGroupMemberNum(
                Math.max(
                        EntitiesUtil.assertNotNull(groupMemberNum) ,
                        EntitiesUtil.assertNotNull(memberNum)));

        return object;
    }
}
