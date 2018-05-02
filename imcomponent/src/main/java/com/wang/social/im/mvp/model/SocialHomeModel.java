package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:45
 * ============================================
 */
@ActivityScope
public class SocialHomeModel extends GroupModel implements SocialHomeContract.Model {

    @Inject
    public SocialHomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<SocialDTO>> getSocialInfo(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getSocialInfo("2.0.0", socialId);
    }
}