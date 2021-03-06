package com.wang.social.im.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;
import com.wang.social.im.interfaces.ImCallBack;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.ui.PersonalCard.model.api.PersonalCardService;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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

    public static RepositoryHelper getInstance() {
        if (mInstance == null) {
            synchronized (RepositoryHelper.class) {
                if (mInstance == null) {
                    mInstance = new RepositoryHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送好友请求
     *
     * @param view
     * @param userId
     * @param reason
     * @param callBack
     */
    public void sendFriendlyApply(IView view, String userId, String reason, ImCallBack callBack) {
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
                        if (!callBack.onFail(e)) {
                            super.onError(e);
                        }
                    }
                });
    }

    /**
     * 设置好友备注
     * @param view
     * @param userId
     * @param remark
     * @param callBack
     */
    public void setFriendRemark(IView view, String userId, String remark, ImCallBack callBack) {
        Map<String, Object> param = NetParam.newInstance()
                .put("v", "2.0.0")
                .put("friendUserId", userId)
                .put("comment", remark)
                .build();
        mApiHelper.executeNone(view, mRepositoryManager
                        .obtainRetrofitService(PersonalCardService.class)
                        .setFriendComment(param),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        callBack.onSuccess(baseJson);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!callBack.onFail(e)) {
                            super.onError(e);
                        }
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                });
    }
}