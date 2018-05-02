package com.wang.social.topic.mvp.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Topic {

    private String firstStrff;
    private String topicImage;
    private String title;
    private String userName;
    private Integer topicReadNum;
    private Integer userId;
    private Integer topicId;
    private Integer isFree;
    private long createTime;
    private Integer topicCommentNum;
    private Integer price;
    private Integer topicSupportNum;
    private List<TopicTag> topicTag;
    private String userCover;
    private Integer isShopping;

    public Topic() {
        firstStrff = "";
        topicImage = "";
        title = "";
        userName = "";
        topicReadNum = 0;
        userId = 0;
        topicId = 0;
        isFree = 0;
        createTime = 0;
        topicCommentNum = 0;
        price = 0;
        topicSupportNum = 0;
        topicTag = new ArrayList<>();
        userCover = "";
        isShopping = 0;
    }
}
