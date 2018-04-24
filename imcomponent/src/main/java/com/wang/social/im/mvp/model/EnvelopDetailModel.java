package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;

import javax.inject.Inject;

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
}
