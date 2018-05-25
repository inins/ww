package com.wang.social.im.mvp.contract;

import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.UIConversation;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-25 11:16
 * ============================================
 */
public interface ShareContract {

    interface View extends IView {

        /**
         * 显示联系人列表
         *
         * @param conversations
         */
        void showContacts(List<UIConversation> conversations);

        /**
         * 分享完成
         */
        void onShareComplete();
    }
}
