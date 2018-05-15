package com.wang.social.im.mvp.ui.PersonalCard.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.ui.PersonalCard.contract.FriendListContract;
import com.wang.social.im.mvp.ui.PersonalCard.model.api.PersonalCardService;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.FriendListDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO.SearchUserInfoDTO;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.EntitiesUtil;
import com.frame.component.entities.PersonalInfo;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@FragmentScope
public class FriendListModel extends BaseModel implements FriendListContract.Model {

    @Inject
    public FriendListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    @Override
    public Observable<BaseJson<FriendListDTO>> getUserFriendList(int otherUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("otherUserId", otherUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getUserFriendList(param);
    }

    @Override
    public Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>> searchUser(
            String key, String phone,
            int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("key", EntitiesUtil.assertNotNull(key))
                .put("phone", EntitiesUtil.assertNotNull(phone))
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .chatListSearchUser(param);
    }
}
