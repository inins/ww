package com.wang.social.personal.net.helper;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.component.common.NetParam;
import com.frame.mvp.IView;
import com.frame.http.api.ApiHelperEx;
import com.frame.utils.ToastUtil;
import com.frame.component.entities.UserWrap;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.mvp.entities.user.UserStatistic;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.Map;

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
        ApiHelperEx.execute(view, true,
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

    public void getUserStatistic(IView view, int userId, OnUserStatisticApiCallBack callBack) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).getUserStatistics(userId),
                new ErrorHandleSubscriber<BaseJson<UserStatistic>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<UserStatistic> baseJson) {
                        if (callBack != null) callBack.onSuccess(baseJson.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public interface OnUserApiCallBack {
        void onSuccess(QrcodeInfo qrcodeInfo);

        void onError(Throwable e);
    }

    public interface OnUserStatisticApiCallBack {
        void onSuccess(UserStatistic userStatistic);
    }
}
