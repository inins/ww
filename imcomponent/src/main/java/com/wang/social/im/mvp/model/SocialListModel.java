package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.SocialListContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleGroupInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 9:53
 * ============================================
 */
@ActivityScope
public class SocialListModel extends BaseModel implements SocialListContract.Model {

    @Inject
    public SocialListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<SimpleGroupInfoDTO, SimpleGroupInfo>>> getBeinGroups() {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getBeinGroups("2.0.0");
    }
}