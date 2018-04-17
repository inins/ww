package com.wang.social.im.mvp.model.entities;

/**
 * ============================================
 * 获取资料
 * <p>
 * Create by ChenJing on 2018-04-17 11:25
 * ============================================
 */
public interface ProfileSummary {

    /**
     * 获取头像信息
     * @return
     */
    String getPortrait();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取描述
     * @return
     */
    String getDescription();

    /**
     * 获取ID
     * @return
     */
    String getIdentify();
}
