package com.wang.social.im.entities;


import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;
import com.wang.social.im.enums.MessageScope;
import com.wang.social.im.enums.MessageType;

import java.util.Arrays;

import lombok.Getter;

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

    public static UIMessage obtain(TIMMessage message) {
        UIMessage uiMessage = new UIMessage();
        uiMessage.timMessage = message;
        uiMessage.setScope(message);
        uiMessage.setType(message);
        return uiMessage;
    }

    private void setScope(TIMMessage timMessage) {
        int elementCount = (int) timMessage.getElementCount();
        for (int i = 0; i < elementCount; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (elem instanceof TIMCustomElem) {
                TIMCustomElem customElem = (TIMCustomElem) elem;
                String dataJson = Arrays.toString(customElem.getData());
                // TODO: 2018-04-02 需拟定以哪个字段来判断当前消息是否为匿名消息或分身消息或者是否为其他自定义消息
//                if (customElem.getDesc().equals("anonymity")){
//                    messageScope = MessageScope.ANONYMITY;
//                    carryUserInfo = new Gson().fromJson(dataJson, CarryUserInfo.class);
//                    break;
//                }else if (customElem.getDesc().equals("shadow")){
//                    messageScope = MessageScope.SHADOW;
//                    carryUserInfo = new Gson().fromJson(dataJson, CarryUserInfo.class);
//                    break;
//                }
            }
        }
    }

    private void setType(TIMMessage timMessage) {
        int elementCount = (int) timMessage.getElementCount();
        for (int i = 0; i < elementCount; i++) {
            TIMElem elem =  timMessage.getElement(i);
            if (elem.getType() == TIMElemType.Text){
                messageType = MessageType.TEXT;
            }else if (elem.getType() == TIMElemType.Face){
                messageType = MessageType.EMOTION;
            }else if (elem.getType() == TIMElemType.Image){
                messageType = MessageType.IMAGE;
            }else if (elem.getType() == TIMElemType.Location){
                messageType = MessageType.LOCATION;
            }else if (elem.getType() == TIMElemType.Sound){
                messageType = MessageType.VOICE;
            }else if (elem.getType() == TIMElemType.SNSTips){
                messageType = MessageType.NOTIFY;
            }else if (elem.getType() == TIMElemType.Custom){
                TIMCustomElem customElem = (TIMCustomElem) elem;
                // TODO: 2018-04-02 获取自定义消息类型
            }
        }
    }
}