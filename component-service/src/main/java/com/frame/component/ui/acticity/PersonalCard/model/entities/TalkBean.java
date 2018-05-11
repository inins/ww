package com.frame.component.ui.acticity.PersonalCard.model.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;

@Data
public class TalkBean {
    private int id;
    private int pid;
    private String groupName;
    private String groupHeadUrl;
    private String groupCoverPlan;
    private List<Tag> tags;
    private int isOpen;
    private int isFree;
    private int groupMemberNum;
}
