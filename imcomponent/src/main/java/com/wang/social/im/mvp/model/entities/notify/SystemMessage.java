package com.wang.social.im.mvp.model.entities.notify;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-23 16:03
 * ============================================
 */
@Data
public class SystemMessage {

    //好友申请
    public static final int TYPE_ADD_FRIEND = 1;
    //群申请
    public static final int TYPE_GROUP_APPLY = 2;
    //群邀请
    public static final int TYPE_GROUP_INVITE = 3;
    //趣晒支付
    public static final int TYPE_PAY_FOR_FUN_SHOW = 4;
    //话题支付
    public static final int TYPE_PAY_FOR_TOPIC = 5;
    //入群支付
    public static final int TYPE_PAY_FOR_GROUP = 6;
    //充值成功
    public static final int TYPE_RECHARGE = 7;
    //提现申请成功
    public static final int TYPE_WITHDRAW_APPLY = 8;
    //提现成功
    public static final int TYPE_WITHDRAW_SUCCESS = 9;
    //趣晒分享
    public static final int TYPE_SHARE_FUN_SHOW = 11;
    //话题分享
    public static final int TYPE_SHARE_TOPIC = 12;
    //群分享
    public static final int TYPE_SHARE_GROUP = 13;
    //讨论组
    public static final int TYPE_SHARE_DISCUSSION = 14;
    //公益金
    public static final int TYPE_CHEST = 15;
    //游戏退款
    public static final int TYPE_GAME_REFUND = 16;
    //游戏获得钻石
    public static final int TYPE_GAME_GOT_DIAMOND = 17;

    //消息ID
    private String msgId;
    //发送者用户ID
    private String userId;
    //发送者用户昵称
    private String nickname;
    /*
     * 消息类型： 1 用户加好友 2 用户申请加群 3邀请加入群组 4用户支付查看故事 5用户支付查看话题 6用户支付入群费用 7充值成功 8申请提现 9提现成功
     *             11故事分享 12话题分享 13群分享 14 讨论组 15 公益金 16 摇钱树游戏人数不够退费 17 摇钱树游戏获得钻石
     */
    private int type;
    //内容分享用户ID
    private String shareUserId;
    //shareUserId 对应的昵称
    private String shareNickname;
    //推送信息主键ID
    private String objectId;
    //objectId对应的名称
    private String name;
    //推送正文
    private String content;
    //推送消息
    private String pushContent;
    //订单号
    private String orderId;
    //金额
    private String money;
    //申请理由
    private String reason;
    //0 未处理 1通过 2过期 3拒绝
    private int pass;
    //接受者用户ID
    private String receiveUserId;
    private String title;
    //消息类别：0：系统消息（默认）；1：好友申请；2：加群申请；3：群邀请
    private String msgCategory;
}
