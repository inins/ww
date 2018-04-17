package com.wang.social.im.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMMessage;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:07
 * ============================================
 */
public interface ConversationListContract {

    interface View extends IView{

        /**
         * 初始化列表数据
         * @param conversations
         */
        void initList(List<TIMConversation> conversations);

        /**
         * 修改最后一条消息
         * @param message
         */
        void updateMessage(TIMMessage message);
    }

    interface Model extends IModel{

    }
}
