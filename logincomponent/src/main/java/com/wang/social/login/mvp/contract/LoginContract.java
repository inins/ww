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

        void gotoHome();
    }

    interface Model extends IModel{

        Observable<BaseJson<LoginInfoDTO>> login(String mobile, String password);
    }
}
