package com.wang.social.im.mvp.model.entities.notify;

import lombok.Data;

@Data
public class CommonMsg {
    private int id;
    private String avatar;
    private String picUrl;
    private String name;
    private long time;
    private String content;

    private int modeType;
    private int modeId;
    private int modePkId;
    private int readState;

    public boolean isRead() {
        return readState == 1;
    }

    ////////////////////////////

    /**
     * 1：话题点赞；
     * 2：话题评论点赞；
     * 3：趣晒点赞；
     * 4：趣晒评论点赞；
     * 5：话题评论；
     * 6：话题评论回复；
     * 7：趣晒评论；
     * 8：趣晒评论回复
     * 9：发布趣晒@
     */

    public boolean isFunshow() {
        return modeType == 3 || modeType == 4 || modeType == 7 || modeType == 8 || modeType == 9;
    }

    public boolean isTopic() {
        return modeType == 1 || modeType == 2 || modeType == 5 || modeType == 6;
    }
}
