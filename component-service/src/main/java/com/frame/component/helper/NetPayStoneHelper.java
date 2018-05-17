package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.app.Constant;
import com.frame.component.common.NetParam;
import com.frame.component.entities.User;
import com.frame.component.entities.UserWrap;
import com.frame.component.helper.AppDataHelper;
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

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetPayStoneHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetPayStoneHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetPayStoneHelper newInstance() {
        return new NetPayStoneHelper();
    }

    public void netPayFunshow(IView view, int talkId, int stoneCount, OnStonePayCallback callback) {
        Map<String, Object> param = NetParam.newInstance()
                .put("price", stoneCount)
                .put("objectId", talkId + "")
                .put("objectType", "talk")
                .put("payChannels", "gemstone")
                .put("versionCode", "17")
                .put("channelCode", "1")
                .put("v", "2.0.0")
                .putSignature()
                .build();
        netPayStone(view, param, callback);
    }

    /**
     * 宝石支付
     *
     * @param view
     * @param price      支付价格
     * @param objectType 支付对象类型
     *                   {@link com.frame.component.app.Constant#PAY_OBJECT_TYPE_CREATE_SOCIAL}
     *                   {@link com.frame.component.app.Constant#PAY_OBJECT_TYPE_TALK}
     *                   {@link com.frame.component.app.Constant#PAY_OBJECT_TYPE_TOPIC}
     *                   {@link com.frame.component..app.Constant#PAY_OBJECT_TYPE_ADD_GROUP}
     * @param objectId   支付对象ID
     * @param callback
     */
    public void stonePay(IView view, int price, String objectType, String objectId, OnStonePayCallback callback) {
        Map<String, Object> param = NetParam.newInstance()
                .put("price", price)
                .put("objectId", objectId)
                .put("objectType", objectType)
                .put("payChannels", Constant.PAY_CHANNEL_STONE)
                .put("versionCode", "17")
                .put("channelCode", "1")
                .put("v", "2.0.0")
                .putSignature()
                .build();
        netPayStone(view, param, callback);
    }

    private void netPayStone(IView view, Map<String, Object> param, OnStonePayCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).payStone(param),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnStonePayCallback {
        void success();
    }

    public void netPayTopic(IView view, int topicId, int stoneCount, OnStonePayCallback callback) {
        Map<String, Object> param = NetParam.newInstance()
                .put("price", stoneCount)
                .put("objectId", topicId + "")
                .put("objectType", "topic")
                .put("payChannels", "gemstone")
                .put("versionCode", "17")
                .put("channelCode", "1")
                .put("v", "2.0.0")
                .putSignature()
                .build();
        netPayStone(view, param, callback);
    }

}
