package com.wang.social.im.mvp.model.entities;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * ============================================
 * 分享消息
 * <p>
 * Create by ChenJing on 2018-05-25 16:29
 * ============================================
 */
@Data
public class ShareElemData {

    private String type;
    /**
     * 标题
     */
    private String title = "";
    /**
     * 内容
     */
    private String content = "";
    /**
     * 封面
     */
    private String cover = "";
    /**
     * 分享内容ID
     */
    @SerializedName("id")
    private String objectId = "-1";
}
