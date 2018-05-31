package com.wang.social.im.mvp.contract;

import android.content.Context;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.AnonymousInfo;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.GroupGameCheckResult;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.model.entities.dto.AnonymousInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.EnvelopInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.GroupGameCheckResultDTO;
import com.wang.social.im.mvp.model.entities.dto.ShadowInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:42
 * ======================================
 */
public interface ConversationContract {

    interface View extends IView {

        /**
         * 添加新消息集合
         *
         * @param uiMessages
         */
        void showMessages(List<UIMessage> uiMessages);

        /**
         * 添加一条新消息
         *
         * @param uiMessage
         */
        void showMessage(UIMessage uiMessage);

        /**
         * 插入历史消息
         *
         * @param uiMessages
         */
        void insertMessages(List<UIMessage> uiMessages);

        /**
         * 刷新一条消息
         *
         * @param uiMessage
         */
        void refreshMessage(UIMessage uiMessage);

        /**
         * 获取上下文
         *
         * @return
         */
        Context getContext();

        /**
         * 显示红包弹框
         *
         * @param uiMessage
         * @param envelopInfo
         */
        void showEnvelopDialog(UIMessage uiMessage, EnvelopInfo envelopInfo);

        /**
         * @param funpoint
         */
        void showFunPoint(Funpoint funpoint);

        /**
         * 分身信息改变了
         *
         * @param shadowInfo
         */
        void onShadowChanged(ShadowInfo shadowInfo);

        /**
         * 隐藏加载状态
         */
        void hideMessageLoad();

        /**
         * 显示创建游戏弹框
         */
        void showCreateGameDialog();

        /**
         * 跳转到游戏房间
         *
         * @param roomId
         */
        void gotoGameRoom(String roomId);
    }

    interface Model extends IModel {

        /**
         * 获取红包详情
         *
         * @param envelopId
         * @return
         */
        Observable<BaseJson<EnvelopInfoDTO>> getEnvelopInfo(long envelopId);

        /**
         * 获取趣点列表
         *
         * @return
         */
        Observable<BaseJson<BaseListWrap<Funpoint>>> getFunPointList(String teamId);

        /**
         * 获取分身信息
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<ShadowInfoDTO>> getShadowInfo(String socialId);

        /**
         * 获取匿名信息
         *
         * @return
         */
        Observable<BaseJson<AnonymousInfoDTO>> getAnonymousInfo();

        /**
         * 修改分身状态
         *
         * @param socialId
         * @param isOpen
         * @return
         */
        Observable<BaseJson> updateShadowStatus(String socialId, boolean isOpen);

        /**
         * 检测是否有游戏
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<GroupGameCheckResultDTO>> checkHasGame(String socialId);

        /**
         * 获取觅聊详情
         *
         * @param teamId
         * @return
         */
        Observable<BaseJson<TeamInfoDTO>> getTeamInfo(String teamId);
    }
}