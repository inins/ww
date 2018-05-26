package com.wang.social.im.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.FriendsContract;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.api.PersonalCardService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 9:40
 * ============================================
 */
@FragmentScope
public class FriendsModel extends BaseModel implements FriendsContract.Model {

    @Inject
    public FriendsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList() {
        return mRepositoryManager
                .obtainRetrofitService(ChainService.class)
                .getFriendList("2.0.0");
    }

    @Override
    public Observable<BaseJson> deleteFriend(String friendId) {
        Map<String, Object> param = new NetParam()
                .put("friendUserId", friendId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .deleteFriend(param);
    }
}
