package com.frame.component.ui.acticity.PersonalCard.contract;

import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import io.reactivex.Observable;

public interface PersonalCardContract {
    interface View extends IView {
        void toastShort(String msg);
        void onLoadUserInfoSuccess(PersonalInfo userInfo);
        void onLoadUserStatisticsSuccess(UserStatistics statistics);
        void onDeleteFriendSuccess();
        void onSetFriendRemarkSuccess(String remark);
        void onAgreeOrRejectApllySuccess(int type);
        void onAddFriendApplySuccess();
        void onSetFriendAvatarSuccess(String url);

        /**
         * 拉黑或取消拉黑成功
         * @param isBlack 是否黑名单
         */
        void onChangeMyBlackSuccess(boolean isBlack);
    }

    interface Model extends IModel {
        /**
         * 用户数据统计（我的/推荐/个人名片）
         * @param userId 用户ID
         */
        Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(int userId);

        /**
         * 用户信息加个人相册输出（我的/个人名片）
         * @param userId 用户ID,如果查询自己的名片信息不传
         */
        Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(int userId);

        /**
         * 添加好友申请
         * @param addUserId 被添加人ID
         * @param reason 申请理由
         */
        Observable<BaseJson> addFriendApply(int addUserId, String reason);

        /**
         * 同意、拒绝添加好友
         * @param friendUserId 好友id
         * @param msgId 消息id
         * @param type 类型（0：同意，1：拒绝）
         */
        Observable<BaseJson> agreeOrRejectAdd(int friendUserId, int msgId, int type);

        /**
         * 好友列表-好友设置备注
         * @param friendUserId 好友id
         * @param comment 备注
         */
        Observable<BaseJson> setFriendComment(int friendUserId, String comment);

        /**
         * 好友列表-好友设置头像
         * @param friendUserId 好友id
         * @param avatarUrl 备注头像url
         */
        Observable<BaseJson> setFriendAvatar(int friendUserId, String avatarUrl);

        /**
         * 删除好友关系
         * @param friendUserId 好友id
         */
        Observable<BaseJson> deleteFriend(int friendUserId);

        /**
         * 屏蔽或者取消屏蔽拉黑用户
         * @param blackUserId 用户ID
         * @param black 是否拉黑 (类型 1:拉黑 2：取消拉黑)
         */
        Observable<BaseJson> changeMyBlack(int blackUserId, boolean black);
    }
}
