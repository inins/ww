package com.wang.social.funshow.mvp.model;

import com.frame.component.entities.BaseListWrap;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@FragmentScope
public class FunshowListModel extends BaseModel implements FunshowListContract.Model {

    @Inject
    public FunshowListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<BaseListWrap<Funshow>>> getFunshowList(int type, int current, int size) {
        return mRepositoryManager
                .obtainRetrofitService(FunshowService.class)
                .getFunshowList(type, current, size);
    }

    @Override
    public Observable<BaseJson<BaseListWrap<TopUser>>> getFunshowTopUserList(String from, int current, int size) {
        return mRepositoryManager
                .obtainRetrofitService(FunshowService.class)
                .getFunshowTopUserList(from, current, size);
    }

}