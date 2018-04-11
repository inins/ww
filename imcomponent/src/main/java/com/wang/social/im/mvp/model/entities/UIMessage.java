package com.wang.social.im.mvp.model.entities;


import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.enums.MessageScope;
import com.wang.social.im.enums.MessageType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================================
 * 消息实体， 接收到消息{@link TIMMessage}通过{@link UIMessage#obtain(TIMMessage)}方法转换
 * <p>
 * Create by ChenJing on 2018-04-02 14:44
 * ==========================================
 */
public class UIMessage {

    @Getter
    private TIMMessage timMessage;
    @Getter
    private MessageScope messageScope = MessageScope.NORMAL;
    @Getter
    private MessageType messageType;
    @Getter
    private CarryUserInfo carryUserInfo;
    @Getter
    @Setter
    private boolean showTime;

    public static UIMessage obtain(TIMMessage message) {
        UIMessage uiMessage = new UIMessage();
        uiMessage.timMessage = message;
        uiMessage.setScope(message);
        uiMessage.setType(message);
        return uiMessage;
    }

    public static List<UIMessage> obtain(List<TIMMessage> timMessages) {
        List<UIMessage> uiMessages = new ArrayList<>();
        for (int i = timMessages.size() - 1; i >= 0; i--) {
            TIMMessage timMessage = timMessages.get(i);
            if (timMessage.status() == TIMMessageStatus.HasDeleted) { //若消息已经删除则不显示
                continue;
            }
            uiMessages.add(UIMessage.obtain(timMessage));
        }
        return uiMessages;
    }

    private void setScope(TIMMessage timMessage) {
        int elementCount = (int) timMessage.getElementCount();
        for (int i = 0; i < elementCount; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem instanceof TIMCustomElem) {
                TIMCustomElem customElem = (TIMCustomElem) elem;
                try {
                    String dataJson = new String(customElem.getData(), "UTF-8");

                    CustomElemType elemType = CustomElemType.getElemType(customElem);
                    if (elemType == CustomElemType.ANONYMITY) {
                        messageScope = MessageScope.ANONYMITY;
                        carryUserInfo = new Gson().fromJson(dataJson, CarryUserInfo.class);
                        break;
                    } else if (elemType == CustomElemType.SHADOW) {
                        messageScope = MessageScope.SHADOW;
                        carryUserInfo = new Gson().fromJson(dataJson, CarryUserInfo.class);
                        break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setType(TIMMessage timMessage) {
        int elementCount = (int) timMessage.getElementCount();
        for (int i = 0; i < elementCount; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem.getType() == TIMElemType.Text) {
                messageType = MessageType.TEXT;
                break;
            } else if (elem.getType() == TIMElemType.Face) {
                messageType = MessageType.EMOTION;
                break;
            } else if (elem.getType() == TIMElemType.Image) {
                messageType = MessageType.IMAGE;
                break;
            } else if (elem.getType() == TIMElemType.Location) {
                messageType = MessageType.LOCATION;
                break;
            } else if (elem.getType() == TIMElemType.Sound) {
                messageType = MessageType.VOICE;
                break;
            } else if (elem.getType() == TIMElemType.SNSTips) {
                messageType = MessageType.NOTIFY;
                break;
            } else if (elem.getType() == TIMElemType.Custom) {
                TIMCustomElem customElem = (TIMCustomElem) elem;
                CustomElemType elemType = CustomElemType.getElemType(customElem);
                if (elemType == CustomElemType.RED_ENVELOP) {
                    messageType = MessageType.RED_ENVELOP;
                } else if (elemType == CustomElemType.REVOKE) {
                    messageType = MessageType.NOTIFY;
                }
            }
        }
    }
}