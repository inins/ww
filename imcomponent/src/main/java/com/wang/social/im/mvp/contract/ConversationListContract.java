package com.wang.social.im.mvp.contract;

import com.frame.component.entities.dto.SettingInfoDTO;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMMessage;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:07
 * ============================================
 */
public interface ConversationListContract {

    interface View extends IView {

        /**
         * 初始化列表数据
         *
         * @param conversations
         */
        void initList(List<TIMConversation> conversations);

        /**
         * 修改最后一条消息
         *
         * @param message
         */
        void updateMessage(TIMMessage message);

        /**
         * 更新会话列表的消息
         *
         * @param messages
         */
        void updateMessages(List<TIMMessage> messages);

        /**
         * 会话删除
         *
         * @param uiConversation
         */
        void onDeleted(UIConversation uiConversation);

        /**
         * 显示没有好友时的状态
         */
        void showNobody();
    }

    interface Model extends IModel {

        /**
         * 获取好友列表
         *
         * @return
         */
        Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList();

        /**
         * 获取用户设置信息
         *
         * @return
         */
        Observable<BaseJson<SettingInfoDTO>> getUserSetting();
    }
}