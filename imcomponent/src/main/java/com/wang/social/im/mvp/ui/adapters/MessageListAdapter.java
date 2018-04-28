package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.ui.adapters.holders.BaseMessageViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.EnvelopViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.ImageViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.LocationViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.NotifyViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.SoundViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.TextViewHolder;
import com.wang.social.im.mvp.ui.adapters.holders.UnknownViewHolder;

import lombok.Setter;

/**
 * 消息列表适配器
 * <p>
 * Create by ChenJing on 2018.4.2 13:52
 */
public class MessageListAdapter extends BaseAdapter<UIMessage> {

    //文本消息
    private final int TYPE_RECEIVE_TEXT = 0;
    private final int TYPE_SEND_TEXT = 1;
    //图片消息
    private final int TYPE_RECEIVE_IMAGE = 2;
    private final int TYPE_SEND_IMAGE = 3;
    //录音消息
    private final int TYPE_RECEIVE_VOICE = 4;
    private final int TYPE_SEND_VOICE = 5;
    //位置消息
    private final int TYPE_RECEIVE_LOCATION = 6;
    private final int TYPE_SEND_LOCATION = 7;
    //表情消息
    private final int TYPE_RECEIVE_EMOTION = 8;
    private final int TYPE_SEND_EMOTION = 9;
    //红包消息
    private final int TYPE_RECEIVE_RED_ENVELOP = 10;
    private final int TYPE_SEND_RED_ENVELOP = 11;
    //通知消息
    private final int TYPE_NOTIFY = 12;
    //未知消息
    private final int TYPE_UNKNOWN = 13;

    private ConversationType mConversationType;

    @Setter
    private BaseMessageViewHolder.OnHandleListener handleListener;

    public MessageListAdapter(ConversationType conversationType) {
        this.mConversationType = conversationType;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        BaseMessageViewHolder viewHolder;
        switch (viewType) {
            case TYPE_RECEIVE_TEXT:
                viewHolder = new TextViewHolder(context, parent, R.layout.im_item_msg_text_left);
                break;
            case TYPE_SEND_TEXT:
                viewHolder = new TextViewHolder(context, parent, R.layout.im_item_msg_text_right);
                break;
            case TYPE_RECEIVE_IMAGE:
                viewHolder = new ImageViewHolder(context, parent, R.layout.im_item_msg_image_left);
                break;
            case TYPE_SEND_IMAGE:
                viewHolder = new ImageViewHolder(context, parent, R.layout.im_item_msg_image_right);
                break;
            case TYPE_RECEIVE_VOICE:
                viewHolder = new SoundViewHolder(context, parent, R.layout.im_item_msg_sound_left);
                break;
            case TYPE_SEND_VOICE:
                viewHolder = new SoundViewHolder(context, parent, R.layout.im_item_msg_sound_right);
                break;
            case TYPE_RECEIVE_RED_ENVELOP:
                viewHolder = new EnvelopViewHolder(context, parent, R.layout.im_item_msg_envelop_left);
                break;
            case TYPE_SEND_RED_ENVELOP:
                viewHolder = new EnvelopViewHolder(context, parent, R.layout.im_item_msg_envelop_right);
                break;
            case TYPE_RECEIVE_LOCATION:
                viewHolder = new LocationViewHolder(context, parent, R.layout.im_item_msg_location_left);
                break;
            case TYPE_SEND_LOCATION:
                viewHolder = new LocationViewHolder(context, parent, R.layout.im_item_msg_location_right);
                break;
            case TYPE_NOTIFY:
                viewHolder = new NotifyViewHolder(context, parent);
                break;
            default:
                viewHolder = new UnknownViewHolder(context, parent);
                break;
        }
        //配置全局参数
        if (mConversationType == ConversationType.PRIVATE) {
            viewHolder.showHeader = true;
            viewHolder.showNickname = false;
        } else if (mConversationType == ConversationType.SOCIAL || mConversationType == ConversationType.TEAM) {
            viewHolder.showHeader = true;
            viewHolder.showNickname = true;
        } else if (mConversationType == ConversationType.MIRROR) {
            viewHolder.showHeader = false;
            viewHolder.showNickname = true;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UIMessage currentMessage = valueList.get(position);
        ((BaseMessageViewHolder) holder).conversationType = mConversationType;
        ((BaseMessageViewHolder) holder).mHandleListener = handleListener;
        if (position > 0) {
            UIMessage lastMessage = valueList.get(position - 1);
            if (currentMessage.getTimMessage().timestamp() - lastMessage.getTimMessage().timestamp() > 60) {
                currentMessage.setShowTime(true);
            }
        } else {
            currentMessage.setShowTime(true);
        }
        ((BaseViewHolder) holder).setData(currentMessage, position, onItemClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        UIMessage message = valueList.get(position);
        int viewType;
        switch (message.getMessageType()) {
            case TEXT:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_TEXT;
                } else {
                    viewType = TYPE_RECEIVE_TEXT;
                }
                break;
            case IMAGE:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_IMAGE;
                } else {
                    viewType = TYPE_RECEIVE_IMAGE;
                }
                break;
            case EMOTION:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_EMOTION;
                } else {
                    viewType = TYPE_RECEIVE_EMOTION;
                }
                break;
            case VOICE:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_VOICE;
                } else {
                    viewType = TYPE_RECEIVE_VOICE;
                }
                break;
            case LOCATION:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_LOCATION;
                } else {
                    viewType = TYPE_RECEIVE_LOCATION;
                }
                break;
            case RED_ENVELOP:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_RED_ENVELOP;
                } else {
                    viewType = TYPE_RECEIVE_RED_ENVELOP;
                }
                break;
            case NOTIFY:
                viewType = TYPE_NOTIFY;
                break;
            case UNKNOWN:
                viewType = TYPE_UNKNOWN;
                break;
            default:
                viewType = -1;
        }
        return viewType;
    }

    /**
     * 根据数据查询索引
     *
     * @param uiMessage
     * @return
     */
    public int findPosition(UIMessage uiMessage) {
        int position = -1;
        if (valueList != null && valueList.size() > 0) {
            for (UIMessage message : valueList) {
                if (message.getTimMessage().getMsgUniqueId() == uiMessage.getTimMessage().getMsgUniqueId()) {
                    position = valueList.indexOf(message);
                    break;
                }
            }
        }
        return position;
    }
}