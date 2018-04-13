package com.wang.social.personal.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.BaseListJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.personal.mvp.contract.LableContract;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.lable.LableWrap;
import com.wang.social.personal.mvp.model.api.UserService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@ActivityScope
public class LableModel extends BaseModel implements LableContract.Model {

    @Inject
    public LableModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<LableWrap>> getShowtag() {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getShowtag();
    }

    @Override
    public Observable<BaseJson<Object>> addShowtag(String tagIds) {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .addShowtag(tagIds);
    }

    @Override
    public Observable<BaseJson<LableWrap>> getSelftags(int parentId) {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getSelftags(parentId);
    }

    @Override
    public Observable<BaseJson<LableWrap>> getParentTags() {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getParentTags();
    }

    @Override
    public Observable<BaseJson<Object>> deltag(int tagId) {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .deltag(tagId);
    }
}