package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import io.reactivex.Observable;

public interface VerifyPhoneContract {
    interface View extends IView {
        void showToast(String msg);

        /**
         *  验证码验证成功
         */
        void onCheckVerifyCodeSuccess(String mobile, String code);
    }


    interface Model extends IModel {
        Observable<BaseJson> preVerifyForForgetPassword(
                String mobile, String code);
    }
}
