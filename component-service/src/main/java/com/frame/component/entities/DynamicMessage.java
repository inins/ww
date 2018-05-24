package com.frame.component.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-23 16:29
 * ============================================
 */
@Data
public class DynamicMessage {

    //话题点赞
    public static final int TYPE_PRAISE_TOPIC = 1;
    //话题评论点赞
    public static final int TYPE_PRAISE_TOPIC_COMMENT = 2;
    //趣晒点赞
    public static final int TYPE_PRAISE_FUN_SHOW = 3;
    //趣晒评论点赞
    public static final int TYPE_PRAISE_FUN_SHOW_COMMENT = 4;
    //话题评论
    public static final int TYPE_COMMENT_TOPIC = 5;
    //话题评论回复
    public static final int TYPE_REPLY_TOPIC_COMMENT = 6;
    //趣晒评论
    public static final int TYPE_COMMENT_FUN_SHOW = 7;
    //趣晒评论回复
    public static final int TYPE_REPLY_FUN_SHOW_COMMENT = 8;
    //趣晒@消息
    public static final int TYPE_REPLY_FUN_SHOW_AITE = 9;

    //消息ID
    private String msgId;
    //发送者用户ID
    private String sendUserId;
    //接受者用户ID
    private String receiveUserId;
    //消息类型
    private String msgSendType;
    //消息内容
    private String msgContent;
    //推送消息
    private String pushContent;
    private String modePkId;
    //对象ID
    private String modeId;
    //对象名称（趣晒/话题名称）
    private String modeName;
    //趣晒缩略图
    private String modeDesc;
    /*
     * 消息内容对应类型 1：话题点赞；2：话题评论点赞；3：趣晒点赞；4：趣晒评论点赞；5：话题评论；6：话题评论回复；7：趣晒评论；8：趣晒评论回复 9：趣晒@消息
     */
    private int modeType;
}
