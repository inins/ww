package com.wang.social.im.mvp.model.entities.dto;

import android.text.TextUtils;

import com.frame.component.entities.dto.TagDTO;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:24
 * ============================================
 */
public class TeamInfoDTO implements Mapper<TeamInfo> {

    private String groupId;
    private String miGroupId;
    private String groupName;
    private String groupCoverPlan;
    private int memberNum;
    private int isFree; //1：不收费，0：收费
    private int validation; //是否验证（0（默认） 不需要 1需要）
    private int gemstone;
    private int isJoined;
    private String tagName;
    private List<TagDTO> tags;

    @Override
    public TeamInfo transform() {
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setTeamId(groupId == null ? "-1" : groupId);
        if (teamInfo.getTeamId().equals("-1")) {
            teamInfo.setTeamId(miGroupId == null ? "-1" : miGroupId);
        }
        teamInfo.setName(groupName == null ? "" : groupName);
        teamInfo.setCover(groupCoverPlan == null ? "" : groupCoverPlan);
        teamInfo.setMemberSize(memberNum);
        teamInfo.setFree(isFree == 1);
        teamInfo.setJoinCost(gemstone);
        teamInfo.setJoined(isJoined == 1);
        teamInfo.setValidation(validation == 1);
        List<Tag> tagList = new ArrayList<>();
        if (tags != null) {
            for (TagDTO tag : tags) {
                tagList.add(tag.transform());
            }
        }
        if (tagList.isEmpty() && !TextUtils.isEmpty(tagName)) {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            tagList.add(tag);
        }
        teamInfo.setTags(tagList);
        return teamInfo;
    }
}
