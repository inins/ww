package com.wang.social.im.app;

/**
 * 常量
 */
public interface IMConstants {

    String USRE_IDENTIFIER_1 = "10009";
    String USRE_IDENTIFIER_2 = "10034";
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

    /**
     * 撤回消息发送时间超过两分钟
     */
    int TIM_ERROR_CODE_REVOKE_TIMEOUT = 6223;
}
