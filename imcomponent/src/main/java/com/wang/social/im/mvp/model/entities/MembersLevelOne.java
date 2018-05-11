package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.view.expand.model.ExpandableListItem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:09
 * ============================================
 */
public class MembersLevelOne implements ExpandableListItem {

    private boolean expanded = true;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private List<MemberInfo> members;

    @Override
    public List<?> getChildItemList() {
        return members;
    }

    @Override
    public boolean isExpanded() {
        return this.expanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }
}
