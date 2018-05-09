package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 16:08
 * ============================================
 */
public class SimpleGroupInfoDTO implements Mapper<SimpleGroupInfo> {

    private String id;
    private String pid;
    private String createUserId;
    private String groupName;
    private String groupHeadUrl;
    private int groupMemberNum;

    @Override
    public SimpleGroupInfo transform() {
        SimpleGroupInfo groupInfo = new SimpleGroupInfo();
        groupInfo.setId(id == null ? "-1" : id);
        groupInfo.setPid(pid == null ? "-1" : pid);
        groupInfo.setSocial(pid.equals("0"));
        groupInfo.setMasterUid(createUserId == null ? "-1" : createUserId);
        groupInfo.setName(groupName == null ? "" : groupName);
        groupInfo.setAvatar(groupHeadUrl == null ? "" : groupHeadUrl);
        groupInfo.setMemberCount(groupMemberNum);
        return groupInfo;
    }
}
