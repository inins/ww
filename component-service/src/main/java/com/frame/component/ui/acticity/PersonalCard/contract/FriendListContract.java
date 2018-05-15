package com.frame.component.ui.acticity.PersonalCard.contract;

import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.FriendListDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.SearchUserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
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

        /**
         * 根据关键字、标签和手机号搜索用户
         * @param key 关键字
         * @param phone 手机号码
         * @param size 每页条数
         * @param current 当前页码
         */
        Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>> searchUser(String key, String phone,
                                                                                      int current, int size);
    }
}
