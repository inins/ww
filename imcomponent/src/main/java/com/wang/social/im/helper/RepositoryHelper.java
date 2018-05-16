package com.wang.social.im.helper;

import com.frame.component.api.CommonService;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;
import com.wang.social.im.interfaces.ImCallBack;
import com.wang.social.im.mvp.model.api.ChainService;

/**
 * ============================================
 * 通用的网络请求
 * <p>
 * Create by ChenJing on 2018-05-09 17:42
 * ============================================
 */
public class RepositoryHelper {

    private static RepositoryHelper mInstance;

    private static ApiHelper mApiHelper;
    private static IRepositoryManager mRepositoryManager;

    private RepositoryHelper() {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
    }

    public static void getInstance() {
        if (mInstance == null) {
            synchronized (RepositoryHelper.class) {
                if (mInstance == null) {
                    mInstance = new RepositoryHelper();
                }
            }
        }
    }

    /**
     * 发送好友请求
     * @param view
     * @param userId
     * @param reason
     * @param callBack
     */
    public static void sendFriendlyApply(IView view, String userId, String reason, ImCallBack callBack) {
        mApiHelper.executeNone(view,
                mRepositoryManager
                        .obtainRetrofitService(CommonService.class)
                        .sendFriendlyApply("2.0.0", userId, reason),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        callBack.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        callBack.onFail(e);
                    }
                });
    }
}
