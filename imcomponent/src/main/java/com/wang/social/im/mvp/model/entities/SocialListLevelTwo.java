package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.view.expand.model.ExpandableListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 10:13
 * ============================================
 */
public class SocialListLevelTwo implements ExpandableListItem {

    private boolean expanded = false;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String pid;
    @Getter
    @Setter
    private String socialId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String avatar;
    @Getter
    @Setter
    private int memberCount;
    @Setter
    private List<TeamInfo> teams;

    @Override
    public List<?> getChildItemList() {
        if (teams == null || teams.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(teams);
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }
}
