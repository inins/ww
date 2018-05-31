package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.IsShoppingRsp;
import com.frame.component.entities.dto.IsShoppingRspDTO;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetIsShoppingHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;
    ApiHelper mApiHelper;

    private NetIsShoppingHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
    }

    public static NetIsShoppingHelper newInstance() {
        return new NetIsShoppingHelper();
    }


    /**
     * 话题是否需要付费
     * @param view IView
     * @param objectId 话题id
     * @param callback 回调
     */
    public void isTopicShopping(IView view, int objectId,
                                OnIsShoppingCallback callback) {
        netIsShopping(view, objectId, "topic", callback);
    }


    /**
     * 趣晒是否需要付费
     * @param view IView
     * @param objectId 趣晒id
     * @param callback 回调
     */
    public void isTalkShopping(IView view, int objectId,
                                OnIsShoppingCallback callback) {
        netIsShopping(view, objectId, "talk", callback);
    }

    /**
     * 趣晒/话题是否需要付费
     * @param view IView
     * @param objectId 趣晒/话题id
     * @param objectType 类型（talk:趣晒，topic:话题）
     * @param callback 回调
     */
    private void netIsShopping(IView view,
                               int objectId, String objectType,
                               OnIsShoppingCallback callback) {
        Map<String, Object> param = new NetParam()
                .put("objectId", objectId)
                .put("objectType", objectType)
                .put("v", "2.0.0")
                .build();

        mApiHelper.execute(view,
                mRepositoryManager.obtainRetrofitService(CommonService.class)
                        .talkOrTopicIsShopping(param),
                new ErrorHandleSubscriber<IsShoppingRsp>() {
                    @Override
                    public void onNext(IsShoppingRsp isShoppingRsp) {
                        if (null != callback) {
                            callback.onIsShopping(isShoppingRsp);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                },
                disposable -> view.showLoading(),
                () -> view.hideLoading());
    }

    ////////////////////////////////////

    public interface OnIsShoppingCallback {
        void onIsShopping(IsShoppingRsp rsp);
    }
}
