package com.wang.social.login.mvp.model;

import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<LoginInfoDTO>> passwordLogin(String mobile, String password, String sign) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .passwordLogin(mobile, password, sign);
    }

    @Override
    public Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(
            String mobile, String code, String sign, String adCode) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .verifyCodeLogin(mobile, code, sign, adCode);
    }

    @Override
    public Observable<BaseJson> userRegister(String mobile, String code, String password, String adCode) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .userRegister(mobile, code, password, adCode);
    }


    @Override
    public Observable<BaseJson> sendVerifyCode(
            String mobile, int type, String sign) {
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .sendVerifyCode(mobile, type, sign);
    }

}