package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.ForgotPasswordContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

import com.wang.social.login.BuildConfig;

import com.wang.social.login.BuildConfig;
@ActivityScope
public class ForgotPasswordModel extends BaseModel implements ForgotPasswordContract.Model {
    @Inject
    public ForgotPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson> sendVerifyCode(String mobile, int type) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("type", type)
                .put("v", BuildConfig.VERSION_NAME)
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .sendVerifyCode(param);
    }
}
