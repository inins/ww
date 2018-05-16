package com.wang.social.im.mvp.ui.PersonalCard.model.entities;

import com.frame.component.entities.PersonalInfo;

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
