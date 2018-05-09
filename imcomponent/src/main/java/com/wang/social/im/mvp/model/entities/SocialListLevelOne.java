package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.view.expand.model.ExpandableListItem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.SocialListActivity} 界面扩展列表所需实体
 * <p>
 * Create by ChenJing on 2018-05-09 10:11
 * ============================================
 */
public class SocialListLevelOne implements ExpandableListItem {

    public boolean expanded = true;
    @Getter
    @Setter
    public String title;
    @Setter
    public List<SocialListLevelTwo> socials;

    @Override
    public List<?> getChildItemList() {
        return socials;
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
