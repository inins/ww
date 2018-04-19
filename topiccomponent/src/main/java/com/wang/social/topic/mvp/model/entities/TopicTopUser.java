package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class TopicTopUser {

    private int userId;
    private int publishNum;
    private int participateNum;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private List<Tags> tags;
}
