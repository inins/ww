package com.wang.social.funshow.mvp.entities.eva;

import lombok.Data;

@Data
public class CommentReply {
    private int commentId;
    private int userId;
    private String nickname;
    private String avatar;
    private String content;
    private int targetUserId;
    private String targetNickname;
    private long createTime;
}
