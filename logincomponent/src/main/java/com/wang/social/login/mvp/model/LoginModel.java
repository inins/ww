package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.api.LoginService;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import java.util.Map;

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

    /**
     * 密码登录
     * @param mobile
     * @param password
     * @return
     */
    @Override
    public Observable<BaseJson<LoginInfoDTO>> passwordLogin(String mobile, String password) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("password", password)
                .put("v","2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .passwordLogin(param);
    }

    /**
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param code 短信验证码
     * @param adCode 区域编码
     * @return
     */
    @Override
    public Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(
            String mobile, String code, String adCode) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("adCode", adCode)
                .put("v","2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .verifyCodeLogin(param);
    }

    /**
     *
     * 手机号码加短信验证码注册用户
     * @param mobile 手机号
     * @param code 短信验证码
     * @param password 设置的密码
     * @param adCode 区域编码
     * @return
     */
    @Override
    public Observable<BaseJson> userRegister(String mobile, String code, String password, String adCode) {
        Map<String, Object> param = new NetParam()
                .put("mobile", mobile)
                .put("code", code)
                .put("password", password)
                .put("adCode", adCode)
                .put("v","2.0.0")
                .putSignature()
                .build();
        return mRepositoryManager
                .obtainRetrofitService(LoginService.class)
                .userRegister(param);
    }


    /**
     *
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param type 用途类型
     *
    用途类型
    （注册 type=1;
    找回密码 type=2;
    三方账号绑定手机 type=4;
    更换手机号 type=5;
    短信登录 type=6）
     * @return
     */
    @Override
    public Observable<BaseJson> sendVerifyCode(
            String mobile, int type) {
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

}