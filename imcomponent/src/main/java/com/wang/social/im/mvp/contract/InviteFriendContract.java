package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 14:46
 * ============================================
 */
public interface InviteFriendContract {

    interface View extends IView {

        /**
         * 显示好友列表
         *
         * @param friends
         */
        void showFriends(List<IndexFriendInfo> friends);

        void inviteComplete();
    }

    interface Model extends IModel {

        /**
         * 获取邀请好友列表
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getInviteFriendList(String socialId);

        /**
         * 发送邀请
         *
         * @param socialId
         * @param friends
         * @return
         */
        Observable<BaseJson> sendInvite(String socialId, List<IndexFriendInfo> friends);
    }
}