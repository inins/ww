package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.TeamInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 11:23
 * ============================================
 */
public class TeamHomeDTO implements Mapper<TeamInfo> {

    private MemberInfoDTO member;
    private TeamInfoDTO group;

    @Override
    public TeamInfo transform() {
        TeamInfo teamInfo = group.transform();
        if (member != null) {
            teamInfo.setSelfProfile(member.transform());
        }
        return teamInfo;
    }
}
