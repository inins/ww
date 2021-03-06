package com.wang.social.funpoint.mvp.model;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Tag;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.funpoint.mvp.contract.FunpointListContract;
import com.frame.component.entities.funpoint.Funpoint;
import com.wang.social.funpoint.mvp.model.api.FunpointService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@FragmentScope
public class FunpointListModel extends BaseModel implements FunpointListContract.Model {

    @Inject
    public FunpointListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<BaseListWrap<Funpoint>>> getFunpointList(int isCondition, int current, int size) {
        return mRepositoryManager
                .obtainRetrofitService(FunpointService.class)
                .getFunpointList(isCondition, current, size);
    }

//    @Override
//    public Observable<BaseJson<Object>> readFunpoint(int newsId) {
//        return mRepositoryManager
//                .obtainRetrofitService(FunpointService.class)
//                .readFunpoint(newsId);
//    }

    @Override
    public Observable<BaseJson<BaseListWrap<Tag>>> getRecommendTag() {
        return mRepositoryManager
                .obtainRetrofitService(FunpointService.class)
                .getRecommendTag();
    }
}