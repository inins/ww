package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.VerifyPhoneContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class VerifyPhoneModel extends BaseModel implements VerifyPhoneContract.Model {
    @Inject
    public VerifyPhoneModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson> preVerifyForForgetPassword(String mobile, String code) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .preVerifyForForgetPassword(mobile, code);
    }
}
