package com.wang.social.login.mvp.contract;

import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import io.reactivex.Observable;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface LoginContract {

    interface View extends IView{
        void showToast(String msg);

        /**
         * 获取验证码成功的回调
         */
        void onSendVerifyCodeSuccess();
    }

    interface Model extends IModel{

        Observable<BaseJson<LoginInfoDTO>> passwordLogin(
                String mobile, String password);

        Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(
                String mobile, String code, String sign, String adCode);

        Observable<BaseJson> userRegister(
                String mobile, String code, String password, String adCode);

        Observable<BaseJson> sendVerifyCode(
                String mobile, int type, String sign);
    }
}
