package com.wang.social.im.mvp.model.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

/**
 * ============================================
 * 觅聊实体
 * <p>
 * Create by ChenJing on 2018-04-28 17:04
 * ============================================
 */
@Data
public class TeamInfo {

    private String teamId;
    private String name;
    private String cover;
    private int memberSize;
    private boolean isFree;
    private int joinCost;
    private boolean joined;
    private boolean validation;
    private List<Tag> tags;
}
