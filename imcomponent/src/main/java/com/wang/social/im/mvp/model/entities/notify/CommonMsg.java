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

    public boolean isCtrlInEva() {
        return modeType == 2 || modeType == 4 || modeType == 6 || modeType == 8;
    }

    public boolean isZan() {
        return modeType == 1 || modeType == 2 || modeType == 3 || modeType == 4;
    }

    public boolean isEva() {
        return modeType == 5 || modeType == 6 || modeType == 7 || modeType == 8;
    }

    public int getMainId() {
        if (isZan()) {
            //点赞消息：如果是对评论点赞，则取 modePkId 否则 modeId
            return isCtrlInEva() ? modePkId : modeId;
        } else if (isEva()) {
            //评论消息：评论或者是对评论进行回复，都取modePkId
            return modePkId;
        } else {
            //@好友消息：取modeId
            return modeId;
        }
    }
}
