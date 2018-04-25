package com.wang.social.funshow.mvp.entities.eva;

import java.util.List;

import lombok.Data;

@Data
public class Comment {

    /**
     * commentId : 10
     * userId : 10000
     * nickname : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * content : 测试4下回复呢
     * supportTotal : 0
     * createTime : 1524052895000
     * commentReply : [{"commentId":11,"userId":10013,"nickname":"W-NewType","avatar":"http://resouce.dongdongwedding.com/FisxstkH-zOZjTyxO1Ej_8svMSBU","content":"哈哈","targetUserId":10000,"targetNickname":"飘飘飘香","createTime":1524053352000}]
     */

    private int commentId;
    private int userId;
    private String nickname;
    private String avatar;
    private String content;
    private int supportTotal;
    private long createTime;
    private List<CommentReply> commentReply;

    //本地字段
    private boolean isShowAll;

    ////////////////////

    public int getCommentReplySize() {
        return commentReply != null ? commentReply.size() : 0;
    }

}
