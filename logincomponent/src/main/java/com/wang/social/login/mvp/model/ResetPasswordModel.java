package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.ResetPasswordContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

import com.wang.social.login.BuildConfig;

@ActivityScope
public class ResetPasswordModel extends BaseModel implements ResetPasswordContract.Model {
    @Inject
    public ResetPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson> userForgetPassword(String mobile, String code, String password) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("password", password)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .userForgetPassword(param);
    }

    @Override
    public Observable<BaseJson> userSetPassword(String password) {
        Map<String, Object> param = new NetParam()
                .put("password", password)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .userSetPassword(param);
    }
}
