package com.wang.social.im.mvp.model;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.FunPointContract;
import com.wang.social.im.mvp.model.api.GroupService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.presenter.FunPointPresenter}
 * <p>
 * Create by ChenJing on 2018-05-17 10:13
 * ============================================
 */
@ActivityScope
public class FunPointModel extends BaseModel implements FunPointContract.Model {

    @Inject
    public FunPointModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<BaseListWrap<Funpoint>>> getFunPointList(String teamId, int currentPage, int pageSize) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getFunPointList(teamId, currentPage, pageSize);
    }
}