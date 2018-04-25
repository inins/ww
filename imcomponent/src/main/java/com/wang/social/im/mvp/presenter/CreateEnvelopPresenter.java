package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 10:04
 * ============================================
 */
@ActivityScope
public class CreateEnvelopPresenter extends BasePresenter<CreateEnvelopContract.Model, CreateEnvelopContract.View> {

    @Inject
    public CreateEnvelopPresenter(CreateEnvelopContract.Model model, CreateEnvelopContract.View view) {
        super(model, view);
    }
}
