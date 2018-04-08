package com.wang.social.im.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.util.List;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:42
 * ======================================
 */
public interface ConversationContract {

    interface View extends IView{

        void showMessages(List<UIMessage> uiMessages);

        void showMessage(UIMessage uiMessage);

        void insertMessages(List<UIMessage> uiMessages);
    }

    interface Model extends IModel{

    }
}
