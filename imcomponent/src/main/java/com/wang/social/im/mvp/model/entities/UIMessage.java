package com.wang.social.im.mvp.model.entities;


import android.text.TextUtils;

import com.frame.component.utils.UIUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.wang.social.im.R;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.enums.MessageScope;
import com.wang.social.im.enums.MessageType;

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
    private MessageType messageType = MessageType.UNKNOWN;
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

    public void refresh() {
        setScope(timMessage);
        setType(timMessage);
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
        //如果消息以及被撤回，则此条消息显示为撤回通知
        if (timMessage.status() == TIMMessageStatus.HasRevoked) {
            messageType = MessageType.NOTIFY;
            return;
        }
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
                }
            }
        }
    }

    public String getSummary() {
        String summary = getRevokeSummary();
        if (!TextUtils.isEmpty(summary)) {
            return summary;
        }
        if (messageType == null) {
            return "";
        }
        switch (messageType) {
            case TEXT:
                for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
                    TIMElem elem = timMessage.getElement(i);
                    if (elem instanceof TIMTextElem) {
                        TIMTextElem textElem = (TIMTextElem) elem;
                        summary = textElem.getText();
                    } else {
                        summary = "";
                    }
                }
                break;
            case IMAGE:
                summary = UIUtil.getString(R.string.im_cvs_summary_image);
                break;
            case VOICE:
                summary = UIUtil.getString(R.string.im_cvs_summary_voice);
                break;
            case EMOTION:
                summary = UIUtil.getString(R.string.im_cvs_summary_emotion);
                break;
            case LOCATION:
                summary = UIUtil.getString(R.string.im_cvs_summary_location);
                break;
            case RED_ENVELOP:
                summary = UIUtil.getString(R.string.im_cvs_summary_red_envelope);
                break;
            default:
                summary = "";
                break;
        }
        return summary;
    }

    public String getRevokeSummary() {
        if (timMessage.status() == TIMMessageStatus.HasRevoked) {
            String nickname;
            if (timMessage.isSelf()) {
                nickname = UIUtil.getString(R.string.im_self);
            } else if (messageScope == MessageScope.NORMAL) {
                nickname = timMessage.getSenderProfile().getNickName();
            } else { //分身消息或者匿名消息 展示消息携带昵称
                nickname = carryUserInfo.getNickname();
            }
            return UIUtil.getString(R.string.im_cvs_revoke, nickname);
        }
        return null;
    }

    /**
     * 获取消息内容Elem
     *
     * @param cls 内容
     * @return
     */
    public TIMElem getMessageElem(Class cls) {
        for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
            TIMElem elem = timMessage.getElement(i);
            if (cls.isInstance(elem)) {
                return elem;
            }
        }
        return null;
    }

    /**
     * 获取用户昵称
     *
     * @param conversationType
     * @return
     */
    public String getNickname(ConversationType conversationType) {
        String nickname = "";
        switch (conversationType) {
            case PRIVATE:
                TIMUserProfile profile = timMessage.getSenderProfile();
                if (profile != null) {
                    if (!TextUtils.isEmpty(profile.getRemark())) {
                        nickname = profile.getRemark();
                    } else {
                        nickname = profile.getNickName();
                    }
                }
                break;
            case SOCIAL:
            case TEAM:
                TIMGroupMemberInfo memberInfo = timMessage.getSenderGroupMemberProfile();
                if (memberInfo != null && !TextUtils.isEmpty(memberInfo.getNameCard())) {
                    nickname = memberInfo.getNameCard();
                } else {
                    nickname = getNickname(ConversationType.PRIVATE);
                }
                break;
            case MIRROR:
                if (carryUserInfo != null) {
                    nickname = carryUserInfo.getNickname();
                }
                break;
        }
        return nickname;
    }

    /**
     * 获取用户头像
     *
     * @param conversationType
     * @return
     */
    public String getPortrait(ConversationType conversationType) {
        if (timMessage.isSelf()){
            // TODO: 2018-04-23 根据会话类型获取自己的头像信息
            return "http://resouce.dongdongwedding.com/2017-08-08_rtUbDxhH.png";
        }
        String portrait = "";
        switch (conversationType) {
            case PRIVATE:
                TIMUserProfile profile = timMessage.getSenderProfile();
                if (profile != null) {
                    portrait = profile.getFaceUrl();
                }
                break;
            case SOCIAL:
            case TEAM:
                // TODO: 2018-04-21 获取用户群头像
                break;
            case MIRROR:
                if (carryUserInfo != null) {
                    portrait = carryUserInfo.getFaceUrl();
                }
                break;
        }
        return portrait;
    }
}