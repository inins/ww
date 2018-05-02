package com.wang.social.topic.mvp.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicTopUser {

    private Integer userId;
    private Integer publishNum;
    private Integer participateNum;
    private String nickname;
    private String avatar;
    private Integer sex;
    private Long birthday;
    private List<Tag> tags;

    public TopicTopUser() {
        userId = 0;
        publishNum = 0;
        participateNum = 0;
        nickname = "";
        avatar = "";
        sex = 0;
        birthday = 0L;
        tags = new ArrayList<>();
    }
}
