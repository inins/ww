package com.wang.social.im.mvp.model.entities;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_ENVELOP;

/**
 * ============================================
 * 红包消息Elem
 * <p>
 * Create by ChenJing on 2018-04-25 10:25
 * ============================================
 */
public class EnvelopElemData {

    /**
     * 指定自定义Elem消息类型为红包
     */
    @Getter
    private String type = CUSTOM_ELEM_ENVELOP;
    /**
     * 红包ID
     */
    @Getter
    @Setter
    @SerializedName("value")
    private long envelopId;
    /**
     * 红包消息内容
     */
    @Getter
    @Setter
    @SerializedName("title")
    private String message;
}
