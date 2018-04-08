package com.wang.social.personal.net.helper;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.component.common.NetParam;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.model.api.UserService;

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
        Map<String, Object> param = new NetParam().put("mobile", "18002247238")
                .put("password", "111111")
                .put("nonceStr", new Random().nextInt())
                .put("v","2.0.0")
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
}
