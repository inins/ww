package com.wang.social.im.mvp.model;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:08
 * ============================================
 */
@FragmentScope
public class ConversationListModel extends BaseModel implements ConversationListContract.Model{

    @Inject
    public ConversationListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getFriendList() {
        return mRepositoryManager
                .obtainRetrofitService(ChainService.class)
                .getFriendList("2.0.0");
    }
}