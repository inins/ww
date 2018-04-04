package com.wang.social.personal.net.helper;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.RxLifecycleUtils;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.model.api.UserService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
        mRepositoryManager.obtainRetrofitService(UserService.class).login("18002247238", "111111")
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
