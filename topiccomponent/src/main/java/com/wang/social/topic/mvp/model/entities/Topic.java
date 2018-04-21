package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class Topic {
    private String firstStrff;
    private String title;
    private String userName;
    private int topicReadNum;
    private int userId;
    private int topicId;
    private int isFree;
    private long createTime;
    private int topicCommentNum;
    private int price;
    private int topicSupportNum;
    private String userCover;
    private int isShopping;
    private List<Tag> tags;
}
