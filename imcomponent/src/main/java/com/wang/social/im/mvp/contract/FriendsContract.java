package com.wang.social.im.mvp.contract;

import com.frame.component.entities.FriendInfo;
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
 * Create by ChenJing on 2018-05-08 9:39
 * ============================================
 */
public interface FriendsContract {

    interface View extends IView {

        /**
         * 显示好友列表
         *
         * @param friends
         */
        void showFriends(List<IndexFriendInfo> friends);

        /**
         * 好友删除完成
         *
         * @param friendInfo
         */
        void onFriendDeleted(IndexFriendInfo friendInfo);

        void showDialogLoading();

        void hideDialogLoading();
    }

    interface Model extends IModel {

        /**
         * 获取好友列表
         *
         * @return
         */
        Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList();

        /**
         * 删除好友
         *
         * @return
         */
        Observable<BaseJson> deleteFriend(String friendId);
    }
}