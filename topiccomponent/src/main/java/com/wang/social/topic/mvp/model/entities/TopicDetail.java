package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class TopicDetail {
    private String title;
    private String content;
    private int templateId;
    private String backgroundImage;
    private int creatorId;
    private int birthday;
    private String avatar;
    private String nickname;
    private int shareTotal;
    private int commentTotal;
    private int supportTotal;
    private List<String> tags;
}
