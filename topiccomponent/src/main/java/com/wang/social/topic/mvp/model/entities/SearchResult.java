package com.wang.social.topic.mvp.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SearchResult {

    private String firstStrff;
    private long birthday;
    private String backgroundImage;
    private int supportTotal;
    private int isSupport;
    private int creatorId;
    private int commentTotal;
    private int readTotal;
    private String avatar;
    private String title;
    private int templateId;
    private int relateDiamond;
    private int relateState;
    private List<Tag> tags;
    private int topicId;
    private String shareTotal;
    private long createTime;
    private String nickname;

    public SearchResult() {
        firstStrff = "";
        birthday = 0L;
        backgroundImage = "";
        supportTotal = 0;
        isSupport = 0;
        creatorId = 0;
        commentTotal = 0;
        readTotal = 0;
        avatar = "";
        title = "";
        templateId = 0;
        relateDiamond = 0;
        relateState = 0;
        List<Tag> tags = new ArrayList<>();
        topicId = 0;
        shareTotal = "";
        createTime = 0L;
        nickname = "";
    }
}
