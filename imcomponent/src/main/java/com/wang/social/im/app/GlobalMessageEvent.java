package com.wang.social.im.app;

import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;

import java.util.List;
import java.util.Observable;

/**
 * ============================================
 * 全局消息监听
 * 服务器推送消息监听
 * <p>
 * Create by ChenJing on 2018-05-23 11:21
 * ============================================
 */
public class GlobalMessageEvent extends Observable implements TIMMessageListener {

    private static volatile GlobalMessageEvent mInstance;

    private GlobalMessageEvent() {
    }

    public static GlobalMessageEvent getmInstance() {
        if (mInstance == null) {
            synchronized (GlobalMessageEvent.class) {
                if (mInstance == null) {
                    mInstance = new GlobalMessageEvent();
                }
            }
        }
        return mInstance;
    }

    private void init() {
        TIMManager.getInstance().addMessageListener(this);
    }

    /**
     * 监听新消息
     *
     * @param list
     * @return
     */
    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (list != null && !list.isEmpty()) {
            for (TIMMessage message : list) {
                if (message.getSender().equals(IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT)) { //检测此条消息是否为服务器的推送消息
                    
                }
            }
        }
        return false;
    }
}
