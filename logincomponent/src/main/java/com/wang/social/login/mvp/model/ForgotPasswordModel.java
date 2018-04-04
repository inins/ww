package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.ForgotPasswordContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class ForgotPasswordModel extends BaseModel implements ForgotPasswordContract.Model {
    @Inject
    public ForgotPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<LoginInfoDTO>> sendVerifyCode(String mobile, int type, String sign) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .sendVerifyCode(mobile, type, sign);
    }
}
