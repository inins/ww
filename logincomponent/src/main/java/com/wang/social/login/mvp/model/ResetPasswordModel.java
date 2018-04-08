package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class ResetPasswordModel extends BaseModel implements ResetPasswordContract.Model {
    @Inject
    public ResetPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson> userForgetPassword(String mobile, String code, String password) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .userForgetPassword(mobile, code, password);
    }
}
