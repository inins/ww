package com.frame.component.ui.acticity.PersonalCard.model.entities.DTO;

import com.frame.component.ui.acticity.PersonalCard.model.entities.EntitiesUtil;
import com.frame.component.ui.acticity.PersonalCard.model.entities.GroupBean;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GroupBeanDTO implements Mapper<GroupBean> {
    private Integer id;
    private Integer pid;
    private String groupName;
    private String groupHeadUrl;
    private String groupCoverPlan;
    private List<Tag> tags;
    private Integer isOpen;
    private Integer isFree;
    private Integer groupMemberNum;

    @Override
    public GroupBean transform() {
        GroupBean object = new GroupBean();

        object.setId(EntitiesUtil.assertNotNull(id));
        object.setPid(EntitiesUtil.assertNotNull(pid));
        object.setGroupName(EntitiesUtil.assertNotNull(groupName));
        object.setGroupHeadUrl(EntitiesUtil.assertNotNull(groupHeadUrl));
        object.setGroupCoverPlan(EntitiesUtil.assertNotNull(groupCoverPlan));
        object.setTags(null == tags ? new ArrayList<>() : tags);
        object.setIsOpen(EntitiesUtil.assertNotNull(isOpen));
        object.setIsFree(EntitiesUtil.assertNotNull(isFree));
        object.setGroupMemberNum(EntitiesUtil.assertNotNull(groupMemberNum));

        return object;
    }
}
