package com.frame.component.app;

/**
 * ======================================
 * 用于存放App全局使用的一些常量
 * <p>
 * Create by ChenJing on 2018-04-04 14:37
 * ======================================
 */
public interface Constant {

    /**
     * 云通信IM SDK AppID
     */
    int IM_APPID = 1400075076;

    /**
     * 支付类型:创建趣聊
     */
    String PAY_OBJECT_TYPE_CREATE_SOCIAL = "creategroup";
    /**
     * 支付类型:趣晒
     */
    String PAY_OBJECT_TYPE_TALK = "talk";
    /**
     * 支付类型:话题
     */
    String PAY_OBJECT_TYPE_TOPIC = "topic";
    /**
     * 支付类型：修改分身
     */
    String PAY_OBJECT_TYPE_SHADOW = "changeShadow";
    /**
     * 付费加群
     */
    String PAY_OBJECT_TYPE_ADD_GROUP = "addgroup";
    /**
     * 支付渠道：宝石支付
     */
    String PAY_CHANNEL_STONE = "gemstone";

    /**
     * 分享树类型：话题
     */
    int SHARE_TYPE_TOPIC = 1;
    /**
     * 分享树类型：趣晒
     */
    int SHARE_TYPE_TALK = 2;
    /**
     * 分享树类型：群组
     */
    int SHARE_TYPE_GROUP = 3;
}
