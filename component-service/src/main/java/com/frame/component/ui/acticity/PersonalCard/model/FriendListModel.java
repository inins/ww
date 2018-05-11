package com.frame.component.ui.acticity.PersonalCard.model;

import com.frame.component.common.NetParam;
import com.frame.component.ui.acticity.PersonalCard.contract.FriendListContract;
import com.frame.component.ui.acticity.PersonalCard.model.api.PersonalCardService;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.FriendListDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;


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
}
