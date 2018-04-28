package com.wang.social.funshow.mvp.entities.event;

import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.entities.eva.CommentReply;

import java.io.Serializable;

import lombok.Data;

@Data
public class CommentEvent implements Serializable {
    private Integer talkCommentId;
    private Integer answeredUserId;
    private String answeredUserName;

    public CommentEvent() {
    }

    public CommentEvent(Comment comment) {
        this.talkCommentId = comment.getCommentId();
        this.answeredUserId = comment.getUserId();
        this.answeredUserName = comment.getNickname();
    }

    public CommentEvent(CommentReply commentReply) {
        this.talkCommentId = commentReply.getParentId();
        this.answeredUserId = commentReply.getUserId();
        this.answeredUserName = commentReply.getNickname();
    }
}
