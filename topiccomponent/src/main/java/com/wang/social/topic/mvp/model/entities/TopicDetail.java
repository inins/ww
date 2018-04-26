package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class TopicDetail {
    private String title;
    private List<String> tags;
    private Long createTime;
    private String avatar;
    private String nickname;
    private Integer sex;
    private String backgroundImage;
    private String backgroundMusicId;
    private String backgroundMusicName;
    private String backgroundMusicUrl;
    private Long birthday;
    private Integer supportTotal;
    private Integer isSupport;
    private Integer creatorId;
    private Integer commentTotal;
    private Integer readTotal;
    private String templateId;
    private String content;
    private Integer relateState;
    private Integer gemstone;
    private Integer topicId;
    private String shareTotal;
}
