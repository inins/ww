package com.frame.component.ui.acticity.PersonalCard.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class FriendList {
    private int total;
    private int current;
    private int pages;
    private int size;
    private int isPermission;
    private List<PersonalInfo> list;
}
