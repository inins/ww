package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.entities.UIMessage;
import com.wang.social.im.mvp.ui.adapters.holders.BaseMessageViewHolder;

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
    private final int TYPE_RECEIVE_REDPACKET = 10;
    private final int TYPE_SEND_REDPACKET = 11;
    //通知消息
    private final int TYPE_NOTIFY = 12;

    private TIMConversationType mConversationType;

    public MessageListAdapter(TIMConversationType mConversationType) {
        this.mConversationType = mConversationType;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UIMessage currentMessage = valueList.get(position);
        ((BaseMessageViewHolder) holder).conversationType = mConversationType;
        if (position > 0) {
            UIMessage lastMessage = valueList.get(position - 1);
            if (currentMessage.getTimMessage().timestamp() - lastMessage.getTimMessage().timestamp() < 60 * 1000) {
                ((BaseMessageViewHolder) holder).showTimestamp = false;
            }
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
            case REDPACKET:
                if (message.getTimMessage().isSelf()) {
                    viewType = TYPE_SEND_REDPACKET;
                } else {
                    viewType = TYPE_RECEIVE_REDPACKET;
                }
                break;
            case NOTIFY:
                viewType = TYPE_NOTIFY;
                break;
            default:
                viewType = -1;
        }
        return viewType;
    }
}