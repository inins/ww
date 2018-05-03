package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.CreateTeamContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 14:06
 * ============================================
 */
@ActivityScope
public class CreateTeamPrensenter extends BasePresenter<CreateTeamContract.Model, CreateTeamContract.View>{

    @Inject
    public CreateTeamPrensenter(CreateTeamContract.Model model, CreateTeamContract.View view) {
        super(model, view);
    }
}
