package com.frame.component.ui.acticity.wwfriendsearch.entities;

import lombok.Data;

@Data
public class SearchGroup extends SearchBase {
    private String head_url;
    private int group_id;
    private String group_name;
    private int pid;
}
