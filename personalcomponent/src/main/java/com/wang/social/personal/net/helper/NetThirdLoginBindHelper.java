package com.wang.social.personal.net.helper;

import android.app.Activity;
import android.content.Context;

import com.frame.component.common.NetParam;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.socialize.SocializeUtil;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetThirdLoginBindHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    @Inject
    public NetThirdLoginBindHelper(IRepositoryManager mRepositoryManager, RxErrorHandler mErrorHandler) {
        this.mRepositoryManager = mRepositoryManager;
        this.mErrorHandler = mErrorHandler;
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

    public void netBindWeiXin(Activity activity, IView view) {
        netThirdLogin(activity, view, 1);
    }

    public void netBindWeiBo(Activity activity, IView view) {
        netThirdLogin(activity, view, 2);
    }

    public void netBindQQ(Activity activity, IView view) {
        netThirdLogin(activity, view, 3);
    }

    private void netThirdLogin(Activity activity, IView view, int type) {
        SocializeUtil.LoginListener loginListener = new SocializeUtil.LoginListener() {
            @Override
            public void onStart(int platform) {
                if (view != null) view.showLoading();
            }

            @Override
            public void onComplete(int platform, Map<String, String> data, String uid, String nickname, int sex, String headUrl) {
                if (view != null) view.hideLoading();
                netBindThirdLogin(view, String.valueOf(platform), uid);
            }

            @Override
            public void onError(int platform, Throwable t) {
                ToastUtil.showToastLong("绑定失败");
                if (view != null) view.hideLoading();
            }

            @Override
            public void onCancel(int platform) {
                if (view != null) view.hideLoading();
            }
        };
        if (type == 1) {
            SocializeUtil.wxLogin(activity, loginListener);
        } else if (type == 2) {
            SocializeUtil.sinaLogin(activity, loginListener);
        } else if (type == 3) {
            SocializeUtil.qqLogin(activity, loginListener);
        }
    }

    private void netBindThirdLogin(IView view, String platform, String uid) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).bindThirdLogin(platform, uid),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastLong(basejson.getMessage());
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
}
