package com.wang.social.im.enums;

import com.tencent.imsdk.TIMCustomElem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_ANONYMITY;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_DYNAMIC_NOTIFY;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_ENVELOP;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_GAME;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_GAME_NOTIFY;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_SHADOW;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_SHARE_FUN_SHOW;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_SHARE_TOPIC;
import static com.wang.social.im.app.IMConstants.CUSTOM_ELEM_SYSTEM_NOTIFY;

/**
 * ============================================
 * 自定义消息类型
 * <p>
 * Create by ChenJing on 2018-04-10 9:34
 * ============================================
 */
public enum CustomElemType {

    /*
     * 红包消息
     */
    RED_ENVELOP(CUSTOM_ELEM_ENVELOP),
    /*
     * 匿名消息
     */
    ANONYMITY(CUSTOM_ELEM_ANONYMITY),
    /*
     * 分身消息
     */
    SHADOW(CUSTOM_ELEM_SHADOW),
    /*
     * 游戏通知消息
     */
    GAME_NOTIFY(CUSTOM_ELEM_GAME_NOTIFY),
    /*
     * 摇钱树游戏
     */
    GAME(CUSTOM_ELEM_GAME),
    /*
     * 系统消息通知
     */
    SYSTEM_NOTIFY(CUSTOM_ELEM_SYSTEM_NOTIFY),
    /*
     * 动态消息通知
     */
    DYNAMIC_NOTIFY(CUSTOM_ELEM_DYNAMIC_NOTIFY),
    /*
     * 趣晒分享
     */
    SHARE_FUN_SHOW(CUSTOM_ELEM_SHARE_FUN_SHOW),
    /*
     * 话题分享
     */
    SHARE_TOPIC(CUSTOM_ELEM_SHARE_TOPIC);

    String value;

    CustomElemType(String value) {
        this.value = value;
    }

    public static CustomElemType getElemType(String value) {
        for (CustomElemType elemType : values()) {
            if (elemType.value.equals(value)) {
                return elemType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public static CustomElemType getElemType(TIMCustomElem customElem) {
        try {
            String dataJson = new String(customElem.getData(), "UTF-8");

            JSONObject jsonObject = new JSONObject(dataJson);
            if (jsonObject.has("type")) {
                String type = jsonObject.getString("type");
                return CustomElemType.getElemType(type);
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}