package com.frame.component.ui.acticity.PersonalCard.contract;

import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.FriendListDTO;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import io.reactivex.Observable;

public interface FriendListContract {
    interface View extends IView {
        void onLoadFriendListSuccess();
        void onLoadFriendListCompleted();
    }

    interface Model extends IModel {
        /**
         * 好友列表-他人名片查看
         * @param otherUserId 他人用户id
         * @param current 当前页数
         * @param size 每页条数
         */
        Observable<BaseJson<FriendListDTO>> getUserFriendList(int otherUserId, int current, int size);
    }
}
