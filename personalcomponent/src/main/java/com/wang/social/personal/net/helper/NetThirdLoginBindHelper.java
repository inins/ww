package com.wang.social.personal.net.helper;

import android.app.Activity;

import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.frame.component.entities.BaseListWrap;
import com.wang.social.personal.mvp.entities.thirdlogin.BindHistory;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.socialize.SocializeUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

    public void netUnBindWeiXin(IView view) {
        netUnBindThirdLogin(view, String.valueOf(SocializeUtil.LOGIN_PLATFORM_WEIXIN));
    }

    public void netUnBindWeiBo(IView view) {
        netUnBindThirdLogin(view, String.valueOf(SocializeUtil.LOGIN_PLATFORM_SINA_WEIBO));
    }

    public void netUnBindQQ(IView view) {
        netUnBindThirdLogin(view, String.valueOf(SocializeUtil.LOGIN_PLATFORM_QQ));
    }

    //解除绑定
    private void netUnBindThirdLogin(IView view, String platform) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).unBindThirdLogin(platform),
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

    //获取绑定记录
    public void netBindList(IView view, OnBindListApiCallBack callBack) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).bindList(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<BindHistory>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<BindHistory>> basejson) {
                        BaseListWrap<BindHistory> wrap = basejson.getData();
                        if (wrap != null) {
                            List<BindHistory> bindHistories = wrap.getList();
                            if (callBack != null) callBack.onSuccess(bindHistories);
                        } else {
                            if (callBack != null) callBack.onSuccess(null);
                        }
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

    public interface OnBindListApiCallBack {
        void onSuccess(List<BindHistory> bindHistories);
    }
}
