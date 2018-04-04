package com.wang.social.personal.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;

import java.util.Map;

import io.reactivex.Observable;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface MeDetailContract {

    interface View extends IView {

        void finishActivity();
    }

    interface Model extends IModel {

        Observable<BaseJson<QiniuTokenWrap>> getQiniuToken();

        Observable<BaseJson<CommonEntity>> updateUserInfo(Map<String, Object> map);
    }
}
