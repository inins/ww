package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.model.entities.TryToExit;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.TryToExitDTO;

import java.util.List;

import io.reactivex.Observable;


/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:00
 * ============================================
 */
public interface MemberListContract {

    interface View extends IView {

        void showMembers(int memberCount, int friendCount, MemberInfo master, List<MembersLevelOne> members);

        void onKickOutComplete(MemberInfo memberInfo);

        void onTryToExitSuccess(MemberInfo memberInfo, TryToExit result);
    }

    interface Model extends IModel {

        /**
         * 获取成员列表
         *
         * @param groupId
         * @return
         */
        Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(String groupId);

        /**
         * 踢出成员
         * @param groupId
         * @param memberUid
         * @return
         */
        Observable<BaseJson> kickOutMember(String groupId, String memberUid);

        /**
         * 尝试退出
         * @param groupId 趣聊/觅聊群id
         * @param memberId 退出用户id
         * @param selfQuit 是否自己退出( 0群主踢人，1用户自己退出)
         */
        Observable<BaseJson<TryToExitDTO>> tryToExit(String groupId, String memberId, boolean selfQuit);
    }
}