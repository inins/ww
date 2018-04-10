package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.BindPhoneContract;
import com.wang.social.login.mvp.model.api.LoginService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class BindPhoneModel extends BaseModel implements BindPhoneContract.Model {
    @Inject
    public BindPhoneModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson> sendVerifyCode(String mobile, int type) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("type", type)
                .put("v","2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .sendVerifyCode(param);
    }

    @Override
    public Observable<BaseJson> replaceMobile(String mobile, String code) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("v","2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .replaceMobile(param);
    }
}
