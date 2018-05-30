package com.wang.social.personal.net.helper;

import android.app.Activity;

import com.dongdongkeji.wangwangsocial.wxapi.WXPayEntryActivity;
import com.frame.component.common.NetParam;
import com.frame.component.utils.ChannelUtils;
import com.frame.component.utils.viewutils.AppUtil;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.personal.mvp.entities.recharge.PayInfo;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.utils.PayUtil;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/4.
 * 支付帮助类，提供2个方法分别发起支付宝和微信支付
 * 支付请求已经封装，支付回调统一在{@link WXPayEntryActivity}中处理
 */

public class NetPayHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetPayHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetPayHelper newInstance() {
        return new NetPayHelper();
    }

    public void netPayAli(Activity activity, int rechargeId, float price) {
        int versionCode = AppUtils.getAppVersionCode();
        int channelCode = ChannelUtils.getChannelCode();
        Map<String, Object> param = NetParam.newInstance()
                .put("price", price)
                .put("objectId", rechargeId + "")
                .put("objectType", "diamond")
                .put("payChannels", "aliPay")
                .put("versionCode", versionCode)
                .put("channelCode", channelCode)
                .put("v", "2.0.0")
                .putSignature()
                .build();
        ApiHelperEx.execute(activity instanceof IView ? (IView) activity : null, true,
                ApiHelperEx.getService(UserService.class).pay(param),
                new ErrorHandleSubscriber<BaseJson<PayInfo>>() {
                    @Override
                    public void onNext(BaseJson<PayInfo> basejson) {
                        PayInfo payInfo = basejson.getData();
                        PayUtil.payForApliy(activity, payInfo.getPayInfo(), new PayUtil.AlipayListener() {
                            @Override
                            public void paySuccess() {
                                WXPayEntryActivity.startPaySuccess(activity);
                            }

                            @Override
                            public void cancel() {
                                WXPayEntryActivity.startPayCancel(activity);
                            }

                            @Override
                            public void payFail() {
                                WXPayEntryActivity.startPayFail(activity);
                            }

                            @Override
                            public void paying() {
                                ToastUtil.showToastShort("支付过程中，请稍后...");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    public void netPayWX(IView view, int rechargeId, float price) {
        int versionCode = AppUtils.getAppVersionCode();
        int channelCode = ChannelUtils.getChannelCode();
        Map<String, Object> param = NetParam.newInstance()
                .put("price", price)
                .put("objectId", rechargeId + "")
                .put("objectType", "diamond")
                .put("payChannels", "weixinPay")
                .put("versionCode", versionCode)
                .put("channelCode", channelCode)
                .put("v", "2.0.0")
                .putSignature()
                .build();
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(UserService.class).pay(param),
                new ErrorHandleSubscriber<BaseJson<PayInfo>>() {
                    @Override
                    public void onNext(BaseJson<PayInfo> basejson) {
                        PayInfo payInfo = basejson.getData();
                        PayUtil.payForWx(payInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }
}
