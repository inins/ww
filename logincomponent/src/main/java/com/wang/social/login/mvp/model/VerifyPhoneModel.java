package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.VerifyPhoneContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

import com.wang.social.login.BuildConfig;

@ActivityScope
public class VerifyPhoneModel extends BaseModel implements VerifyPhoneContract.Model {
    @Inject
    public VerifyPhoneModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     *
     * 修改/重置密码（前置验证）验证验证码
     * @param mobile
     * @param code
     * @return
     */
    @Override
    public Observable<BaseJson> preVerifyForForgetPassword(String mobile, String code) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("v", "2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .preVerifyForForgetPassword(param);
    }

    @Override
    public Observable<BaseJson> sendVerifyCode(String mobile, int type) {
            Map<String, Object> param = new NetParam()
                    .put("mobile", mobile)
                    .put("type", type)
                    .put("v", "2.0.0")
                    .putSignature()
                    .build();
            return mRepositoryManager
                    .obtainRetrofitService(LoginService.class)
                    .sendVerifyCode(param);
    }
}
