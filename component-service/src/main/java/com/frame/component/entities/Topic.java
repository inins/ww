package com.frame.component.entities;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Topic {

    private int topicId;
    private int creatorId;
    private String firstStrff;
    private String backgroundImage;
    private String title;
    private int readTotal;
    private int supportTotal;
    private long createTime;
    private String address;
    private int commentTotal;
    private int shareTotal;
    private int relateState;
    private int gemstone;
    private List<Tag> tags;
    private boolean shopping;
    private boolean support;
    private String avatar;
    private String nickname;
}
