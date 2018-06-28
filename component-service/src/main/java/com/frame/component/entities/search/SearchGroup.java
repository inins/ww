package com.frame.component.entities.search;

import com.frame.component.entities.Tag;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SearchGroup implements Serializable {
    private int groupId;
    private String groupName;
    private String parentGroupName;
    private String groupCoverPlan;
    private int memberNum;
    private int isGroup;
    private List<Tag> tags;

    public boolean isMi() {
        return isGroup == 0;
    }
}
