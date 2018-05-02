package com.wang.social.im.mvp.contract;

import android.content.Context;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.model.entities.dto.EnvelopInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:42
 * ======================================
 */
public interface ConversationContract {

    interface View extends IView{

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

        /**
         * 获取上下文
         * @return
         */
        Context getContext();

        /**
         * 显示红包弹框
         * @param uiMessage
         * @param envelopInfo
         */
        void showEnvelopDialog(UIMessage uiMessage, EnvelopInfo envelopInfo);
    }

    interface Model extends IModel{

        /**
         * 获取红包详情
         * @param envelopId
         * @return
         */
        Observable<BaseJson<EnvelopInfoDTO>> getEnvelopInfo(long envelopId);
    }
}