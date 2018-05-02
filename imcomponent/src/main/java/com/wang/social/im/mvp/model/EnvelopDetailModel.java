package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;
import com.wang.social.im.mvp.model.api.EnvelopService;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.dto.EnvelopAdoptInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * 为{@link com.wang.social.im.mvp.presenter.CreateEnvelopPresenter}提供数据
 * <p>
 * Create by ChenJing on 2018-04-24 13:49
 * ============================================
 */
@ActivityScope
public class EnvelopDetailModel extends BaseModel implements EnvelopDetailContract.Model {

    @Inject
    public EnvelopDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<PageListDTO<EnvelopAdoptInfoDTO, EnvelopAdoptInfo>>> getAdoptList(long envelopId, int currentPage) {
        return mRepositoryManager
                .obtainRetrofitService(EnvelopService.class)
                .getEnvelopAdoptList("2.0.0", envelopId, currentPage, 20);
    }
}
