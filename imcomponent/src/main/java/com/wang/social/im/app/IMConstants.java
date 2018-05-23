package com.wang.social.im.app;

/**
 * 常量
 */
public interface IMConstants {

    /**
     * 小米推送：bussId
     */
    long MIPUSH_BUSSID = 3720;
    /**
     * 小米推送：AppId
     */
    String MIPUSH_APPID = "2882303761517578585";
    /**
     * 小米推送：AppKey
     */
    String MIPUSH_APPKEY = "5641757831585";
    /**
     * 魅族推送：bussId
     */
    long MZPUSH_BUSSID = 3721;
    /**
     * 魅族推送：AppId
     */
    String MZPUSH_APPID = "1000160";
    /**
     * 魅族推送：AppKey
     */
    String MZPUSH_APPKEY = "3379086898464c90bc7914ed668496dd";
    /**
     * 华为推送：bussId
     */
    long HMSPUSH_BUSSID = 3719;

    /**
     * 服务端推送消息使用账号
     */
    String SERVER_PUSH_MESSAGE_ACCOUNT = "admin";

    /**
     * 自定义消息Elem类型：红包
     * {@link com.wang.social.im.enums.CustomElemType}
     */
    String CUSTOM_ELEM_ENVELOP = "envelop";

    /**
     * 自定义消息Elem类型：匿名消息
     * {@link com.wang.social.im.enums.CustomElemType}
     */
    String CUSTOM_ELEM_ANONYMITY = "anonymous";

    /**
     * 自定义消息Elem类型：分身消息
     * {@link com.wang.social.im.enums.CustomElemType}
     */
    String CUSTOM_ELEM_SHADOW = "doppelganger";

    /**
     * 自定义消息Elem类型：游戏通知
     */
    String CUSTOM_ELEM_GAME_NOTIFY = "GameNtf";

    /**
     * 自定义消息Elem类型：游戏
     */
    String CUSTOM_ELEM_GAME = "game_yqs";

    /**
     * 个人红包上限
     */
    int PRIVATE_ENVELOP_DIAMOND_LIMIT = 2000;

    /**
     * 群红包每人上限
     */
    int MULTI_ENVELOP_DIAMOND_LIMIT = 20000;

    /**
     * 趣聊收费下限
     */
    int SOCIAL_CHARGE_LIMIT_MIN = 100;
    /**
     * 趣聊收费上限
     */
    int SOCIAL_CHARGE_LIMIT_MAX = 400000;
    /**
     * 创建趣聊/觅聊费用
     */
    int CREATE_GROUP_PRICE = 100;
    /**
     * 修改分身费用
     */
    int UPDATE_SHADOW_PRICE = 100;

    /**
     * IM会话标识前缀：镜像聊天室
     */
    String IM_IDENTITY_PREFIX_MIRROR = "JX";
    /**
     * IM会话标识前缀：摇钱树
     */
    String IM_IDENTITY_PREFIX_GAME = "YQS";
    /**
     * IM会话标识前缀：趣聊
     */
    String IM_IDENTITY_PREFIX_SOCIAL = "QU";
    /**
     * IM会话标识前缀：觅聊
     */
    String IM_IDENTITY_PREFIX_TEAM = "MI";

    /**
     * 群类型：私有群
     */
    String IM_GROUP_TYPE_PRIVATE = "Private";
    /**
     * 群类型：公共群
     */
    String IM_GROUP_TYPE_PUBLIC = "Public";
    /**
     * 群类型：聊天室
     */
    String IM_GROUP_TYPE_CHAT_ROOM = "ChatRoom";
    /**
     * 群类型：直播聊天室
     */
    String IM_GROUP_TYPE_AV_CHAT_ROOM = "AVChatRoom";
    /**
     * 摇钱树通知操作类型：创建
     */
    String GAME_NOTIFY_TYPE_CREATE = "Create";
    /**
     * 摇钱树通知操作类型：加入
     */
    String GAME_NOTIFY_TYPE_JOIN = "Add";
    /**
     * 摇钱树通知操作类型：游戏结果
     */
    String GAME_NOTIFY_TYPE_RESULT = "Result";

    /** -------------------IM自定义字段---------------------*/
    /**
     * 群组类型（1：趣聊、2：觅聊、3：镜子聊天室）
     * 群组类型判别使用会话ID的前缀来判断
     */
    @Deprecated
    String IM_FIELD_GROUP_PROFILE_TYPE = "GroupType";

    /**
     * 好友备注头像
     */
    String IM_FIELD_FRIEND_PORTRAIT = "Tag_SNS_Custom_RmrkFUrl";

    /**
     * 群用户头像
     */
    String IM_FIELD_GROUP_MEMBER_PORTRAIT = "cardFaceUrl";
    /**------------------------------------------------**/

    /**--------------------IM错误码-----------------------**/
    /**
     * 撤回消息发送时间超过两分钟
     */
    int TIM_ERROR_CODE_REVOKE_TIMEOUT = 6223;
    int TIM_ERROR_CODE_REVOKE_TIMEOUT_ = 10031;
    /**
     * 已经加入过群组
     */
    int TIM_ERROR_CODE_GROUP_JOINED = 10013;
    /**------------------------------------------------**/

    /**
     * ----------------------文案--------------------------
     **/
    String CONTENT_INVITE_JOIN_APP = "加入往往咯";
    /** ---------------------------------------------------- **/
}