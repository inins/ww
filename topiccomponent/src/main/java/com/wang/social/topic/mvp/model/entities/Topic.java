package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class Topic {
    private String topicId;
    private String title;
    private String firstStrff;
    private String userId;
    private String userName;
    private String userCover;
    private String createTime;
    private String topicSupportNum;
    private String topicCommentNum;
    private String topicReadNum;
    private String isFree;
    private String price;
    private String isShopping;
    private List<Tag> tags;
}
