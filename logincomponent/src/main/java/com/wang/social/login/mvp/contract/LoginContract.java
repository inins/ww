package com.wang.social.login.mvp.contract;

import android.app.Activity;

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
        Activity getActivity();
        void gotoTagSelection();
        void gotoMainPage();
    }

    interface Model extends IModel{

        Observable<BaseJson<LoginInfoDTO>> passwordLogin(
                String mobile, String password);

        Observable<BaseJson<LoginInfoDTO>> verifyCodeLogin(
                String mobile, String code, String adCode);

        Observable<BaseJson> userRegister(
                String mobile, String code, String password, String adCode);

        Observable<BaseJson> sendVerifyCode(
                String mobile, int type);

        Observable<BaseJson<LoginInfoDTO>> platformLogin(
                int platform, String uid, String nickname, String headUrl, int sex, String adCode);
    }
}
