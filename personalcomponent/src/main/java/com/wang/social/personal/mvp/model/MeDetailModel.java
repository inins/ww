package com.wang.social.personal.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.photo.PhotoListWrap;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@ActivityScope
public class MeDetailModel extends BaseModel implements MeDetailContract.Model {

    @Inject
    public MeDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<QiniuTokenWrap>> getQiniuToken() {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getQiniuToken();
    }

    @Override
    public Observable<BaseJson<CommonEntity>> updateUserInfo(Map<String, Object> map) {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .updateUserInfo(map);
    }

    @Override
    public Observable<BaseJson<PhotoListWrap>> getPhotoList() {
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getPhotoList();
    }
}