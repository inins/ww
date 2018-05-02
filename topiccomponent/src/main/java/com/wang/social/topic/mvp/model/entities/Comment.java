package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class Comment {
    private Integer commentId;
    private Integer userId;
    private String avatar;
    private String nickname;
    private String content;
    private Integer supportTotal;
    private Integer isSupport;
    private Long createTime;
    private Integer replyNum;
    private Integer targetUserId;
    private String targetNickname;
    private List<Comment> commentReply;
}
