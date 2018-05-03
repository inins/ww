package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.CreateSocialContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 20:26
 * ============================================
 */
@ActivityScope
public class CreateSocialPresenter extends BasePresenter<CreateSocialContract.Model, CreateSocialContract.View>{

    @Inject
    public CreateSocialPresenter(CreateSocialContract.Model model, CreateSocialContract.View view) {
        super(model, view);
    }
}