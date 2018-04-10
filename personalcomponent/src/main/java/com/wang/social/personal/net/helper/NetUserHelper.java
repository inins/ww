package com.wang.social.personal.net.helper;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.component.common.NetParam;
import com.frame.mvp.IView;
import com.wang.social.personal.helper.MyApiHelper;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.entities.photo.Photo;
import com.wang.social.personal.mvp.entities.photo.PhotoListWrap;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetUserHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    @Inject
    public NetUserHelper(IRepositoryManager mRepositoryManager, RxErrorHandler mErrorHandler) {
        this.mRepositoryManager = mRepositoryManager;
        this.mErrorHandler = mErrorHandler;
    }

    public void loginTest() {
        Map<String, Object> param = new NetParam()
                .put("mobile", "18002247238")
                .put("password", "111111")
                .put("v", "2.0.0")
                .putSignature()
                .build();
        mRepositoryManager.obtainRetrofitService(UserService.class).login(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson<UserWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<UserWrap> base) {
                        UserWrap wrap = base.getData();
                        User user = wrap.getUserInfo();
                        AppDataHelper.saveUser(user);
                        AppDataHelper.saveToken(wrap.getToken());
                    }
                });
    }

    public void getUserInfoByUserId(IView view, int userId, OnUserApiCallBack callBack) {
        MyApiHelper.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).getUserInfoByUserId(userId),
                new ErrorHandleSubscriber<BaseJson<QrcodeInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<QrcodeInfo> baseJson) {
                        if (callBack != null) callBack.onSuccess(baseJson.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) callBack.onError(e);
                    }
                });
    }

    public interface OnUserApiCallBack{
        void onSuccess(QrcodeInfo qrcodeInfo);
        void onError(Throwable e);
    }
}
