package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.login.mvp.model.entities.dto.LoginInfoDTO;

import io.reactivex.Observable;

public interface ResetPasswordContract {
    interface View extends IView {
        void showToast(String msg);
    }


    interface Model extends IModel {
        Observable<BaseJson> userForgetPassword(
                String mobile, String code, String password);

        Observable<BaseJson> userSetPassword(
                String password);
    }
}
