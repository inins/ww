package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import io.reactivex.Observable;

public interface BindPhoneContract {
    interface View extends IView {
        void showToast(String msg);

        /**
         * 获取验证码成功的回调
         */
        void onSendVerifyCodeSuccess(String mobile);
        void onReplaceMobileSuccess();
    }


    interface Model extends IModel {
        Observable<BaseJson> sendVerifyCode(
                String mobile, int type);


        Observable<BaseJson> replaceMobile(
                String mobile, String code);
    }
}
