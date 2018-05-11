package com.wang.social.im.mvp.model.entities;

import android.support.annotation.NonNull;

import com.frame.utils.TimeUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.helper.FriendShipHelper;
import com.wang.social.im.helper.GroupHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.adapters.ConversationAdapter} 数据元素
 * <p>
 * Create by ChenJing on 2018-04-17 13:58
 * ============================================
 */
public class UIConversation implements Comparable {

    private TIMConversation mConversation;
    @Getter
    private String identify;
    @Getter
    private ConversationType conversationType;
    @Setter
    @Getter
    private UIMessage lastMessage;

    public UIConversation(@NonNull TIMConversation conversation) {
        this.mConversation = conversation;
        if (conversation.getType() == TIMConversationType.C2C) {
            conversationType = ConversationType.PRIVATE;
        } else if (conversation.getType() == TIMConversationType.Group) {
            if (conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_MIRROR)) {
                conversationType = ConversationType.MIRROR;
            } else {
                //根据拉取的自定义字段判断是趣聊还是觅聊
                int groupType = GroupHelper.getInstance().getGroupProfile(conversation.getPeer()).getGroupType();
                if (groupType == GroupProfile.GROUP_TYPE_SOCIAL) {
                    conversationType = ConversationType.SOCIAL;
                } else if (groupType == GroupProfile.GROUP_TYPE_TEAM) {
                    conversationType = ConversationType.TEAM;
                }
            }
        }
        identify = conversation.getPeer();
    }

    /**
     * 获取最后一条消息的摘要
     *
     * @return
     */
    public String getLastMessageSummary() {
        if (lastMessage == null) {
            return "";
        }
        return lastMessage.getSummary();
    }

    /**
     * 获取会话名称
     *
     * @return
     */
    public String getName() {
        if (conversationType == ConversationType.PRIVATE) {
            FriendProfile friendProfile = FriendShipHelper.getInstance().getFriendProfile(identify);
            if (friendProfile != null) {
                return friendProfile.getName();
            }
        } else {
            GroupProfile groupProfile = GroupHelper.getInstance().getGroupProfile(identify);
            if (groupProfile != null) {
                return groupProfile.getName();
            }
        }
        return "";
    }

    /**
     * 获取会话头像
     *
     * @return
     */
    public String getPortrait() {
        if (conversationType == ConversationType.PRIVATE) {
            FriendProfile friendProfile = FriendShipHelper.getInstance().getFriendProfile(identify);
            if (friendProfile != null) {
                return friendProfile.getPortrait();
            }
        } else {
            GroupProfile groupProfile = GroupHelper.getInstance().getGroupProfile(identify);
            if (groupProfile != null) {
                return groupProfile.getPortrait();
            }
        }
        return "";
    }

    /**
     * 获取时间
     *
     * @return
     */
    public String getTime() {
        if (lastMessage != null) {
            long timestamp = lastMessage.getTimMessage().timestamp() * 1000;
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(timestamp));
            int messageYear = cal.get(Calendar.YEAR);
            int messageMonth = cal.get(Calendar.MONTH);
            int messageDay = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(new Date());
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);
            if (messageYear != currentYear || messageMonth != currentMonth || messageDay != currentDay) {
                if (messageYear != currentYear) {
                    return TimeUtils.millis2String(timestamp, new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()));
                } else {
                    return TimeUtils.millis2String(timestamp, new SimpleDateFormat("MM.dd HH:mm", Locale.getDefault()));
                }
            } else {
                return TimeUtils.millis2String(timestamp, new SimpleDateFormat("HH:mm", Locale.getDefault()));
            }
        }
        return "";
    }

    /**
     * 获取消息未读数
     *
     * @return
     */
    public int getUnreadNum() {
        if (mConversation != null) {
            TIMConversationExt conversationExt = new TIMConversationExt(mConversation);
            return (int) conversationExt.getUnreadMessageNum();
        }
        return 0;
    }

    /**
     * 获取最后一条消息时间
     *
     * @return
     */
    public long getLastMessageTime() {
        if (lastMessage == null) {
            return 0;
        }
        return lastMessage.getTimMessage().timestamp();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        UIConversation uiConversation = (UIConversation) obj;
        if (!uiConversation.getIdentify().equals(identify)) {
            return false;
        }
        return uiConversation.getConversationType() == conversationType;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof UIConversation) {
            UIConversation conversation = (UIConversation) o;
            long timeGap = conversation.getLastMessageTime() - getLastMessageTime();
            if (timeGap > 0) return 1;
            else if (timeGap < 0) return -1;
            else return 0;
        } else {
            throw new ClassCastException();
        }
    }
}
