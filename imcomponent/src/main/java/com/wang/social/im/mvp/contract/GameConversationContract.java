package com.wang.social.im.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 10:58
 * ============================================
 */
public interface GameConversationContract {

    interface View extends IView {

        /**
         * 添加新消息集合
         * @param uiMessages
         */
        void showMessages(List<UIMessage> uiMessages);

        /**
         * 添加一条新消息
         * @param uiMessage
         */
        void showMessage(UIMessage uiMessage);

        /**
         * 插入历史消息
         * @param uiMessages
         */
        void insertMessages(List<UIMessage> uiMessages);

        /**
         * 刷新一条消息
         * @param uiMessage
         */
        void refreshMessage(UIMessage uiMessage);

        void joinComplete();
    }

    interface Model extends IModel {

    }
}