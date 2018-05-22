package com.wang.social.im.mvp.model.entities;

import com.google.gson.annotations.SerializedName;
import com.tencent.imsdk.TIMElem;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================================
 * 消息携带的用户信息， 当发送匿名消息或使用分身发送消息时需使用{@link com.tencent.imsdk.TIMMessage#addElement(TIMElem)}中添加{@link com.tencent.imsdk.TIMCustomElem}
 * 将{@link CarryUserInfo}转换为Json格式{@link com.tencent.imsdk.TIMCustomElem#setData(byte[])}插入到消息中,并使用{@link com.tencent.imsdk.TIMCustomElem#setDesc(String)}
 * 描述消息类型
 * <p>
 * Create by ChenJing on 2018-04-02 15:14
 * ==========================================
 */
public class CarryUserInfo {

    @Setter
    private String type;
    @Setter
    @Getter
    @SerializedName("name")
    private String nickname;
    @Setter
    @Getter
    private String faceUrl;
}