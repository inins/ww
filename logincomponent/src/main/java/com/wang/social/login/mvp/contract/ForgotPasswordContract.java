package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import io.reactivex.Observable;

public interface ForgotPasswordContract {
    interface View extends IView {
        void showToast(String msg);

        /**
         * 获取验证码成功的回调
         */
        void onSendVerifyCodeSuccess();
    }


    interface Model extends IModel {
        Observable<BaseJson<LoginInfoDTO>> sendVerifyCode(
                String mobile, int type, String sign);
    }
}
