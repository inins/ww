package com.wang.social.im.mvp.ui.PersonalCard.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.ui.PersonalCard.contract.PersonalCardContract;
import com.wang.social.im.mvp.ui.PersonalCard.model.api.PersonalCardService;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.UserStatisticsDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class PersonalCardModel extends BaseModel implements PersonalCardContract.Model {

    @Inject
    public PersonalCardModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 用户数据统计（我的/推荐/个人名片）
     * @param userId 用户ID
     */
    @Override
    public Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(int userId) {
        Map<String, Object> param = new NetParam()
                .put("userId", userId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getUserStatistics(param);
    }

    /**
     * 用户信息加个人相册输出（我的/个人名片）
     * @param userId 用户ID,如果查询自己的名片信息不传
     */
    @Override
    public Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(int userId) {
        Map<String, Object> param = new NetParam()
                .put("userId", userId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getUserInfoAndPhotos(param);
    }

    @Override
    public Observable<BaseJson> addFriendApply(int addUserId, String reason) {
        Map<String, Object> param = new NetParam()
                .put("addUserId", addUserId)
                .put("reason", reason)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .addFriendApply(param);
    }

    @Override
    public Observable<BaseJson> agreeOrRejectAdd(int friendUserId, int msgId, int type) {
        Map<String, Object> param = new NetParam()
                .put("friendUserId", friendUserId)
                .put("msgId", msgId)
                .put("type", type)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .agreeOrRejectAdd(param);
    }

    @Override
    public Observable<BaseJson> setFriendComment(int friendUserId, String comment) {
        Map<String, Object> param = new NetParam()
                .put("friendUserId", friendUserId)
                .put("comment", comment)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .setFriendComment(param);
    }

    @Override
    public Observable<BaseJson> setFriendAvatar(int friendUserId, String avatarUrl) {
        Map<String, Object> param = new NetParam()
                .put("friendUserId", friendUserId)
                .put("avatarUrl", avatarUrl)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .setFriendAvatar(param);
    }

    @Override
    public Observable<BaseJson> deleteFriend(int friendUserId) {
        Map<String, Object> param = new NetParam()
                .put("friendUserId", friendUserId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .deleteFriend(param);
    }

    /**
     * 屏蔽或者取消屏蔽拉黑用户
     * @param blackUserId 用户ID
     * @param black 是否拉黑 (类型 1:拉黑 2：取消拉黑)
     */
    @Override
    public Observable<BaseJson> changeMyBlack(int blackUserId, boolean black) {
        Map<String, Object> param = new NetParam()
                .put("blackUserId", blackUserId)
                .put("type", black ? "1" : "2")
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .changeMyBlack(param);
    }


}
