package com.frame.component.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

@Data
public class GroupBean {
    private int id;
    private int pid;
    private int groupId;
    private String groupName;
    private String groupHeadUrl;
    private String groupCoverPlan;
    private List<Tag> tags;
    private int isOpen;
    private int isFree;
    private int groupMemberNum;
    private String tagName;
}
