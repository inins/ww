package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * 趣聊/觅聊相关公共获取数据方法
 * <p>
 * Create by ChenJing on 2018-05-02 9:59
 * ============================================
 */
public interface GroupContract{

    interface GroupView extends IView{

        /**
         * 显示成员列表
         * @param members
         */
        void showMembers(List<MemberInfo> members);

        /**
         * 退出趣聊/觅聊或者解散趣聊/觅聊
         */
        void onExitOrDissolve();

        /**
         * 头像上传完成
         * @param url
         */
        void onPortraitUploaded(String url);
    }

    interface GroupModel extends IModel{

        /**
         * 获取成员列表
         * @param groupId
         * @return
         */
        Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMemberList(String groupId);

        /**
         * 修改趣聊/觅聊名片信息
         * @param groupId
         * @param nickname
         * @param portrait
         * @return
         */
        Observable<BaseJson> updateNameCard(String groupId, String nickname, String portrait, MessageNotifyType notifyType);

        /**
         * 解散趣聊/觅聊
         * @param groupId
         * @return
         */
        Observable<BaseJson> dissolveGroup(String groupId);

        /**
         * 退出趣聊/觅聊
         * @param groupId
         * @return
         */
        Observable<BaseJson> exitGroup(String groupId);
    }

}